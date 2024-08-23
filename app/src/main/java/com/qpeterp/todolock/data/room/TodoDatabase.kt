package com.qpeterp.todolock.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TodoData::class], version = 1)
abstract class TodoDatabase: RoomDatabase() {
    abstract fun todoDao(): TodoDao

    companion object {
        private var instance: TodoDatabase? = null

        @Synchronized
        fun getInstance(context: Context): TodoDatabase? {
            if (instance == null) {
                synchronized(TodoDatabase::class){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        TodoDatabase::class.java,
                        "todo-database" // 만약에 안되면 얘 문제일 가능성 90%
                    ).build()
                }
            }
            return instance
        }
    }
}