package com.qpeterp.todolock.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class TodoData(
    @PrimaryKey val uuid: UUID = UUID.randomUUID(),
    @ColumnInfo(name = "todo") var todo: String,
    @ColumnInfo(name = "isChecked") var isChecked: Boolean
)