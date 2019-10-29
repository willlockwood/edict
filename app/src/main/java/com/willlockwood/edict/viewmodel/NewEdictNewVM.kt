package com.willlockwood.edict.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.willlockwood.edict.data.database.EdictDatabase
import com.willlockwood.edict.data.model.NewEdict
import com.willlockwood.edict.data.repository.NewEdictRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewEdictNewVM(application: Application) : AndroidViewModel(application) {

    private val newEdictDao = EdictDatabase.getDatabase(application, viewModelScope).newEdictDao()
    private val repository = NewEdictRepository(newEdictDao)

    fun getAllEdicts(): LiveData<List<NewEdict>> = repository.getAllNewEdicts()

    fun insertNewEdict(newEdict: NewEdict) = viewModelScope.launch(Dispatchers.IO) { repository.insertNewEdict(newEdict) }

}