/**
 * abstract class RDataBase используется для создания базы данных
 *
 * добавление используемых таблиц, версии, имени БД
 */

package com.meridiane.notepadroomlibrary.data

import android.content.Context
import androidx.room.Database

import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Entity::class],version = 1)
abstract class RDataBase: RoomDatabase() {
    abstract fun getDao():NoteDao

    companion object{
        @Volatile
        private var INSTANCE: RDataBase ?= null

        fun getDateBase(context: Context): RDataBase {
            return INSTANCE ?: synchronized(this){

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RDataBase::class.java,
                    "note_pad.db")
                    .build()

                instance
            }
        }
    }
}