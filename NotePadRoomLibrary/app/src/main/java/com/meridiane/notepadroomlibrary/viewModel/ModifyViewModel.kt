package com.meridiane.notepadroomlibrary.viewModel

import androidx.lifecycle.*
import com.meridiane.notepadroomlibrary.db.Entity
import com.meridiane.notepadroomlibrary.db.RDataBase
import kotlinx.coroutines.launch

class ModifyViewModel(database: RDataBase): ViewModel() {

    private val dao = database.getDao()

    // запись жанных в БД
    fun insertEntity(entity:Entity) = viewModelScope.launch{
        dao.insertEntity(entity)
    }

    // обновить данные в БД
    fun updateEntity(entity: Entity) = viewModelScope.launch {
        dao.updateEntity(entity)
    }

    // значение полученное при клике на "Задать время"
    val sendDbTime: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>(100)
    }
    // устанавливаем значение в тексте кнопки "Выбора времени" при повороте экрана
    fun sendDbTime(): LiveData<Int> {
        return sendDbTime
    }

    //значение полученное при клике на "Задать дату"
    val sendDbDate: MutableLiveData<String> by lazy {
        MutableLiveData<String>("Выбрать дату заметки")
    }
    // устанавливаем значение в тексте кнопки "Выбора даты" при повороте экрана
    fun sendDbDate(): LiveData<String> {
        return sendDbDate
    }
}

