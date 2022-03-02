package com.meridiane.notepadroomlibrary.viewModel

import androidx.lifecycle.*
import com.meridiane.notepadroomlibrary.db.Entity
import com.meridiane.notepadroomlibrary.db.RDataBase
import kotlinx.coroutines.launch

class MainViewModel(database: RDataBase):ViewModel() {

    private val dao = database.getDao()

    fun deleteEntity(id:Int) = viewModelScope.launch {
        dao.deleteEntity(id)
    }

    fun getAllEntityByDate(dateString:String) : LiveData<List<Entity>> {
        return dao.getAllEntityByDate(dateString).asLiveData()
    }
}
