package com.qpeterp.todolock.ui.main.lock

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.qpeterp.todolock.data.room.TodoData
import com.qpeterp.todolock.databinding.ItemTodoBinding

class CustomAdapter(private var todoList : List<TodoData>): RecyclerView.Adapter<CustomAdapter.Holder>() {

    interface ItemClick {  //클릭이벤트추가부분
        fun onClick(view : TextView, todoData : TodoData)
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

        holder.checkBox.setOnClickListener {
            itemClick?.onClick(holder.todoContent, todoList[position])
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