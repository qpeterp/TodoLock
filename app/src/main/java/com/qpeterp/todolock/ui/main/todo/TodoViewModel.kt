package com.qpeterp.todolock.ui.main.todo

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qpeterp.todolock.data.room.TodoData
import com.qpeterp.todolock.data.room.TodoDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class TodoViewModel(context: Context): ViewModel() {
    // TodoData 목록을 관리할 StateFlow
    private val _todoList = MutableStateFlow<List<TodoData>>(emptyList())
    val todoList: StateFlow<List<TodoData>> =_todoList

    private val db = TodoDatabase.getInstance(context)
    val todoToUpdate = mutableStateOf(
        TodoData(uuid = UUID.fromString("aa1a1aa1-a1aa-11aa-aa11-11aa11aa1111"), todo = "", isChecked = false)
    )

    init {
        loadTodos()
    }

    private fun loadTodos() {
        viewModelScope.launch(Dispatchers.IO) {
            val todos = db?.todoDao()?.getAll() ?: emptyList()
            _todoList.value = todos
        }
    }

    fun addTodo(todo: String) {
        viewModelScope.launch(Dispatchers.IO) {
            db?.todoDao()?.insertTodo(TodoData(todo = todo, isChecked = false))
            loadTodos()
        }
    }

    fun updateTodo(todo: TodoData) {
        viewModelScope.launch(Dispatchers.IO) {
            db?.todoDao()?.updateTodo(todo)
            loadTodos()
        }
    }

    fun deleteTodo(todo: TodoData) {
        viewModelScope.launch(Dispatchers.IO) {
            db?.todoDao()?.deleteTodo(todo)
            loadTodos()
        }
    }
}