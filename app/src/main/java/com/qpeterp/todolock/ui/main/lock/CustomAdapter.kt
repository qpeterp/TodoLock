package com.qpeterp.todolock.ui.main.lock

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.qpeterp.todolock.data.room.TodoData
import com.qpeterp.todolock.databinding.ItemTodoBinding

class CustomAdapter(private var todoList : List<TodoData>): RecyclerView.Adapter<CustomAdapter.Holder>() {

    interface ItemClick {  //클릭이벤트추가부분
        fun onClick(view : View, todoData : TodoData)
    }
    var itemClick : ItemClick? = null  //클릭이벤트추가부분

    inner class Holder(binding: ItemTodoBinding): RecyclerView.ViewHolder(binding.root) {
        val todoContent = binding.todoContent
        val checkBox = binding.todoCheckBox
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemTodoBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.todoContent.text = todoList[position].todo
        holder.checkBox.isChecked = todoList[position].isChecked

        stateIsChecked(holder, position)

        holder.checkBox.setOnClickListener {
            itemClick?.onClick(it, todoList[position])
            stateIsChecked(holder, position)
        }
    }

    private fun stateIsChecked(holder: Holder, position: Int) {
        with(holder.todoContent) {
            if (todoList[position].isChecked) {
                setPaintFlags(getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG)
                setTextColor(android.graphics.Color.GRAY)
            } else {
                setPaintFlags(getPaintFlags() and Paint.STRIKE_THRU_TEXT_FLAG.inv())
                setTextColor(android.graphics.Color.WHITE)
            }
        }
    }

    fun updateData(newTodoList: List<TodoData>) {
        this.todoList = newTodoList
        notifyDataSetChanged()  // 데이터가 변경되었음을 어댑터에 알림
    }

    override fun getItemCount(): Int {
        return todoList.size
    }
}