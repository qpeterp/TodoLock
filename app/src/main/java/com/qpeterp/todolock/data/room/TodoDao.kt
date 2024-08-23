package com.qpeterp.todolock.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TodoDao {
    @Query("SELECT * FROM TodoData")
    fun getAll(): List<TodoData>

    @Query("DELETE FROM TodoData")
    fun deleteAll()

    @Insert
    fun insertTodo(vararg todo: TodoData)

    @Update
    fun updateTodo(vararg todo: TodoData)

    @Delete
    fun deleteTodo(vararg todo: TodoData)
}