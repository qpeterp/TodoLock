package com.qpeterp.todolock.specific.lock

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.qpeterp.todolock.R
import com.qpeterp.todolock.common.Constant
import com.qpeterp.todolock.data.room.TodoData
import com.qpeterp.todolock.ui.main.lock.CustomAdapter
import com.qpeterp.todolock.ui.main.todo.TodoViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OverlayService : Service() {

    private lateinit var windowManager: WindowManager
    private lateinit var overlayView: View
    private lateinit var adapter: CustomAdapter
    private lateinit var todoViewModel: TodoViewModel

    private lateinit var handler: Handler
    private lateinit var runnable: Runnable
    private val holdTime = 5000L

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate() {
        super.onCreate()

        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager

        // XML 레이아웃을 오버레이로 표시
        val inflater = LayoutInflater.from(this)
        overlayView = inflater.inflate(R.layout.activity_lock, null)

        // 플래그 설정 조정
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else
                WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
            PixelFormat.TRANSLUCENT
        )

        todoViewModel = TodoViewModel(this)
        initView()

        handler = Handler(Looper.getMainLooper())
        val button = overlayView.findViewById<Button>(R.id.lock_button)

        button.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    runnable = Runnable {
                        Log.d(Constant.TAG, "Overlay onCreate: longClick checkedcheckedcheckedcheckedchecked")
                        handleLockState()
                    }
                    handler.postDelayed(runnable, holdTime)
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    handler.removeCallbacks(runnable)
                }
            }
            true
        }

        windowManager.addView(overlayView, params)
    }

    private fun initView() {
        val recyclerView = overlayView.findViewById<RecyclerView>(R.id.recyclerView)

        val todoList: List<TodoData> = todoViewModel.todoList.value
        Log.d(Constant.TAG, "OverlayService initView: todoList : $todoList")

        adapter = CustomAdapter(todoList)

        // CoroutineScope를 사용하여 비동기적으로 StateFlow를 수집
        CoroutineScope(Dispatchers.Main).launch {
            todoViewModel.todoList.collect { todoList ->
                Log.d(Constant.TAG, "OverlayService initView: todoList : $todoList")
                adapter.updateData(todoList)  // 어댑터에 새로운 데이터 전달
            }
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter.itemClick = object : CustomAdapter.ItemClick {
            override fun onClick(view: View, todoData: TodoData) {
                todoViewModel.updateTodo(todoData)
            }
        }
    }

    private fun handleLockState() {
        val pref = getSharedPreferences("lockState", MODE_PRIVATE)
        val editor = pref.edit()
        editor.putBoolean("lockState", false)
        editor.apply()
        stopOverlayService()
        Log.d(Constant.TAG, "OverlayService handleLockState: button is clicked lockState : ${pref.getBoolean("lockState", true)}")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    private fun stopOverlayService() {
        val intent = Intent(this, OverlayService::class.java)
        stopService(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopForeground(true)
        if (::overlayView.isInitialized) {
            windowManager.removeView(overlayView)
            Log.d(Constant.TAG, "Overlay view removed in onDestroy")
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}