package com.meridiane.notepadroomlibrary.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "note")
data class Entity(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "content")
    val content: String,

    @ColumnInfo(name = "dateTimeStart")
    val date: Long,

    @ColumnInfo(name = "dateTimeEnd")
    val time: Int,

    @ColumnInfo(name = "dateString")
    val dateString: String
): Serializable
