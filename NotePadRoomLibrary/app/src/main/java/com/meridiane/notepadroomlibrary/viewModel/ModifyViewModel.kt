package com.meridiane.notepadroomlibrary.viewModel

import androidx.lifecycle.*
import com.meridiane.notepadroomlibrary.db.Entity
import com.meridiane.notepadroomlibrary.db.RDataBase
import kotlinx.coroutines.launch

class ModifyViewModel(database: RDataBase): ViewModel() {

    private val dao = database.getDao()

    fun insertEntity(entity:Entity) = viewModelScope.launch{
        dao.insertEntity(entity)
    }

    fun updateEntity(entity: Entity) = viewModelScope.launch {
        dao.updateEntity(entity)
    }
}

