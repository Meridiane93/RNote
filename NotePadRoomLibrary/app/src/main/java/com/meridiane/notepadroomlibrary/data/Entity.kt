/**
 * data class Entity используется для создания сущности (таблицы) БД
 */

package com.meridiane.notepadroomlibrary.data

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

    @ColumnInfo(name = "date")
    val date: Long,

    @ColumnInfo(name = "time")
    val time: Int,

    @ColumnInfo(name = "dateString")
    val dateString: String
): Serializable
