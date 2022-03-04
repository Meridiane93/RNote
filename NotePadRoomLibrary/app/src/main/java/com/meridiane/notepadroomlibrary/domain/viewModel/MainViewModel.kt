/**
 * class MainViewModel используется для сохранения интерфейса, а так же вызова методов Dao
 *  и редактирования отображения
 *
 * class MainViewModelFactory используется для инициализации MainViewModel, можно добавить use case
 */

package com.meridiane.notepadroomlibrary.domain.viewModel

import androidx.lifecycle.*
import com.meridiane.notepadroomlibrary.data.Entity
import com.meridiane.notepadroomlibrary.data.RDataBase
import kotlinx.coroutines.launch

class MainViewModel(database: RDataBase):ViewModel() {

    private val dao = database.getDao()

    // delete Entity
    fun deleteEntity(id:Int) = viewModelScope.launch {
        dao.deleteEntity(id)
    }
    // get Entity by date
    fun getAllEntityByDate(dateString:String) : LiveData<List<Entity>> {
        return dao.getAllEntityByDate(dateString).asLiveData()
    }

    val buttonDateTextGet: MutableLiveData<String> by lazy {
        MutableLiveData<String>("Задать дату")
    }
    // устанавливаем значение в тексте кнопки "Выбора даты" при повороте экрана
    fun buttonDateTextSet(): LiveData<String> {
        return buttonDateTextGet
    }
}

class MainViewModelFactory(private val database: RDataBase) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {    // создаёт VM
        if (modelClass.isAssignableFrom(MainViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(database) as T
        }
        throw IllegalArgumentException("Unknown ViewModelClass")
    }
}
