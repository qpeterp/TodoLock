package com.qpeterp.todolock.specific.lock

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import com.qpeterp.todolock.R
import com.qpeterp.todolock.common.Constant

class OverlayService : Service() {

    private lateinit var windowManager: WindowManager
    private lateinit var overlayView: View

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

        val button = overlayView.findViewById<Button>(R.id.lock_button)
        button.setOnClickListener {
            handleLockState()
        }

        windowManager.addView(overlayView, params)
    }

    private fun handleLockState() {
        val pref = getSharedPreferences("lockState", MODE_PRIVATE)
        val editor = pref.edit()
        editor.putBoolean("lockState", false)
        editor.apply()
        Log.d(Constant.TAG, "OverlayService handleLockState: button is clicked lockState : ${pref.getBoolean("lockState", true)}")
        stopForeground(true)
        if (::overlayView.isInitialized) {
            windowManager.removeView(overlayView)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopForeground(true)
        if (::overlayView.isInitialized) {
            windowManager.removeView(overlayView)
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}