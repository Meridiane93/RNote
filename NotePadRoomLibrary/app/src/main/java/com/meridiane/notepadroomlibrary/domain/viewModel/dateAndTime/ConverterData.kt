/**
 * class ConverterData используется для конвертации данных в БД и обратно
 */

package com.meridiane.notepadroomlibrary.domain.viewModel.dateAndTime

import java.text.SimpleDateFormat
import java.util.*

class ConverterData {

    // конвертируем миллисекунды в стандартный вид даты
    fun convertMillis(mil: Long): String {
        val dateLong = java.util.Calendar.getInstance()
        dateLong.timeInMillis = mil
        val days = dateLong.get(java.util.Calendar.DAY_OF_MONTH)
        val months = dateLong.get(java.util.Calendar.MONTH)
        val years = dateLong.get(java.util.Calendar.YEAR)
        return "$days/${months + 1}/$years"
    }

    // парсинг строки и преобразуем в миллисекунды
    fun convertString(str: String): Long {
        val simpleFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)
        val parseString = simpleFormat.parse(str)!!
        return parseString.time
    }

    fun converterTimeMillis(int: Int): Int = if (int == 0) 0 else int * 3600000

    fun converterTimeInt(int: Int): Int = if (int == 0) 0 else int / 3600000
}