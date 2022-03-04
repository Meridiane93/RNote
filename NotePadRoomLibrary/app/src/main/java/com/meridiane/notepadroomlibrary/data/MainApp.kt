/**
 * class MainApp используется для инииализации VM
 */

package com.meridiane.notepadroomlibrary.data

import android.app.Application

class MainApp: Application() {
    val database by lazy { RDataBase.getDateBase(this) }
}