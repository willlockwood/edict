package com.willlockwood.edict.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.willlockwood.edict.data.database.EdictDatabase
import com.willlockwood.edict.data.model.Edict
import com.willlockwood.edict.data.model.EdictSession
import com.willlockwood.edict.data.repository.EdictRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EdictVM(application: Application) : AndroidViewModel(application) {

    private val edictDao = EdictDatabase.getDatabase(application, viewModelScope).edictDao()
    private val edictCheckDao = EdictDatabase.getDatabase(application, viewModelScope).edictSessionDao()
    private val repository = EdictRepository(edictDao, edictCheckDao)

    fun getAllEdicts(): LiveData<List<Edict>> = repository.getAllEdicts()

    fun getLiveEdictById(id: Int): LiveData<Edict> = repository.getLiveEdictById(id)
    suspend fun getEdictById(id: Int): Edict = repository.getEdictById(id)

    fun insertEdictAndNewSession(edict: Edict) = viewModelScope.launch(Dispatchers.IO) {
        val newSession = edict.createEdictSession()
        val edictId = repository.insertEdictGetId(edict)
        newSession.edict = edictId.toInt()
        repository.insertEdictSessions(newSession)
    }

    private fun updateEdictAndSession(edict: Edict, edictSession: EdictSession) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertEdict(edict)
        repository.insertEdictSessions(edictSession)
    }

    fun getActiveEdictSessions(): LiveData<List<EdictSession>> = repository.getActiveEdictSessions()

    fun resolveEdictSession(edictSession: EdictSession, edict: Edict, success: Boolean) {
        edictSession.resolveSession(success)
        when (success) {
            true -> edict.addToStreak()
            false -> edict.resetStreak()
        }
        updateEdictAndSession(edict, edictSession)
    }

//    unused so far
//    fun insertEdict(edict: Edict) = viewModelScope.launch(Dispatchers.IO) { repository.insertEdict(edict) }
//    fun insertEdictGetId(edict: Edict): Long = repository.insertEdictGetId(edict)
//    fun updateEdict(edict: Edict) = viewModelScope.launch(Dispatchers.IO) { repository.updateEdict(edict) }
//    fun updateEdictSession(edictSession: EdictSession) = viewModelScope.launch(Dispatchers.IO) { repository.updateEdictSession(edictSession) }
//    fun getEdictSessionsById(id: Int) = repository.getEdictSessionsById(id)
//    fun insertEdictSessions(edictSession: EdictSession) = viewModelScope.launch(Dispatchers.IO) { repository.insertEdictSessions(edictSession) }
//    fun insertEdictSessions(edictSessions: List<EdictSession>) = viewModelScope.launch(Dispatchers.IO) { repository.insertEdictSessions(edictSessions) }

}