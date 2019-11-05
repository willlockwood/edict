package com.willlockwood.edict.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.willlockwood.edict.data.database.EdictDatabase
import com.willlockwood.edict.data.model.NewEdict
import com.willlockwood.edict.data.repository.NewEdictRepository

class NewEdictVM(application: Application) : AndroidViewModel(application) {

    private val newEdictDao = EdictDatabase.getDatabase(application, viewModelScope).newEdictDao()
    private val newEdictSessionDao = EdictDatabase.getDatabase(application, viewModelScope).newEdictSessionDao()
    private val repository = NewEdictRepository(newEdictDao, newEdictSessionDao)

    fun getAllEdicts(): LiveData<List<NewEdict>> = repository.getAllNewEdicts()

}