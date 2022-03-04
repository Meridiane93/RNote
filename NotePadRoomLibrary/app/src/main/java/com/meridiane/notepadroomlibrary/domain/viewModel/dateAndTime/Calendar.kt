/**
 * class Calendar используется для получения текущей даты
 */

package com.meridiane.notepadroomlibrary.domain.viewModel.dateAndTime

import java.util.Calendar

class Calendar {
    private val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
}