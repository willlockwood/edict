package com.willlockwood.edict.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.willlockwood.edict.data.database.EdictDatabase
import com.willlockwood.edict.data.model.NewEdict
import com.willlockwood.edict.data.model.NewEdictSession
import com.willlockwood.edict.data.repository.NewEdictRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CreateEdictVM(application: Application) : AndroidViewModel(application) {

    private val newEdictDao = EdictDatabase.getDatabase(application, viewModelScope).newEdictDao()
    private val newEdictSessionDao = EdictDatabase.getDatabase(application, viewModelScope).newEdictSessionDao()
    private val repository = NewEdictRepository(newEdictDao, newEdictSessionDao)

    fun insertEdictAndNewSession(edict: NewEdict) = viewModelScope.launch(Dispatchers.IO) {
        edict.id = repository.insertEdictGetId(edict).toInt()
        val session = NewEdictSession.newInstance(edict)
        repository.insertEdictSessions(session)
//        repository.insertEdictSessions(NewEdictSession.newInstance(edict))
    }
}