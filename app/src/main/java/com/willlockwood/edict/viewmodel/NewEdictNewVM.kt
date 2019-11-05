package com.willlockwood.edict.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.willlockwood.edict.data.database.EdictDatabase
import com.willlockwood.edict.data.model.NewEdict
import com.willlockwood.edict.data.model.NewEdictSession
import com.willlockwood.edict.data.repository.NewEdictRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.threeten.bp.OffsetDateTime

class NewEdictNewVM(application: Application) : AndroidViewModel(application) {

    private val newEdictDao = EdictDatabase.getDatabase(application, viewModelScope).newEdictDao()
    private val newEdictSessionDao = EdictDatabase.getDatabase(application, viewModelScope).newEdictSessionDao()
    private val repository = NewEdictRepository(newEdictDao, newEdictSessionDao)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            loadSessions()
        }
    }

    private var unresolvedSessions = MutableLiveData<List<NewEdictSession>>()
    fun getUnresolvedSessions(): LiveData<List<NewEdictSession>?> = unresolvedSessions

    fun getAllSessions(): LiveData<List<NewEdictSession>> = repository.getAllSessions()

    fun getUncheckedSessions(): LiveData<List<NewEdictSession>> = repository.getUnresolvedSessions()

    fun getAllEdicts(): LiveData<List<NewEdict>> = repository.getAllNewEdicts()

//    fun insertEdictAndNewSession(edict: NewEdict) = viewModelScope.launch(Dispatchers.IO) {
//        edict.id = repository.insertEdictGetId(edict).toInt()
//        repository.insertEdictSessions(NewEdictSession.newInstance(edict))
//        loadSessions()
//    }

    private fun loadSessions() {
        val sessions = repository.getUnresolvedSessions().value
        val now = OffsetDateTime.now()
        val sessionsToResolve = sessions?.filter { it.checkInWindow.second!! < now }
        if (sessionsToResolve != null) {
            updateSessions(sessionsToResolve.map {
                it.resolveSession(false)
                it
            } )
        }
        val remainingSessions = sessions?.filter { it.checkInWindow.second!! > now }
        unresolvedSessions.postValue(remainingSessions)
    }

    fun insertNewEdict(newEdict: NewEdict) = viewModelScope.launch(Dispatchers.IO) { repository.insertNewEdict(newEdict) }

    fun updateSessions(sessions: List<NewEdictSession>) = viewModelScope.launch(Dispatchers.IO) { repository.updateSessions(sessions) }

}