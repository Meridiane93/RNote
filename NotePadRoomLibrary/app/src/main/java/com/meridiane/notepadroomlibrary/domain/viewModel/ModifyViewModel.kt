/**
 * class ModifyViewModel  используется для сохранения интерфейса, а так же вызова методов Dao
 *  и редактирования отображения
 *
 *  class ModifyViewModelFactory инициализирует ModifyViewModel
 */

package com.meridiane.notepadroomlibrary.domain.viewModel

import androidx.lifecycle.*
import com.meridiane.notepadroomlibrary.data.Entity
import com.meridiane.notepadroomlibrary.data.RDataBase
import kotlinx.coroutines.launch

class ModifyViewModel(database: RDataBase): ViewModel() {

    private val dao = database.getDao()

    // запись данных в БД
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

class ModifyViewModelFactory(private val database: RDataBase) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {    // создаёт VM
        if (modelClass.isAssignableFrom(ModifyViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return ModifyViewModel(database) as T
        }
        throw IllegalArgumentException("Unknown ViewModelClass")
    }
}

