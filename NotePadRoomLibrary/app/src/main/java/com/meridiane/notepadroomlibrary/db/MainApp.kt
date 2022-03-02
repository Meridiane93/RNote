package com.meridiane.notepadroomlibrary.db

import android.app.Application

class MainApp: Application() {
    val database by lazy { RDataBase.getDateBase(this) }
}