package com.willlockwood.edict.data.repository

import androidx.lifecycle.LiveData
import com.willlockwood.edict.data.dao.NewEdictDao
import com.willlockwood.edict.data.dao.NewEdictSessionDao
import com.willlockwood.edict.data.model.NewEdict
import com.willlockwood.edict.data.model.NewEdictSession

class NewEdictRepository(
    private val newEdictDao: NewEdictDao,
//    private val edictDao: EdictDao,
    private val edictSessionDao: NewEdictSessionDao
) {

    fun getAllNewEdicts(): LiveData<List<NewEdict>> = newEdictDao.getAllNewEdicts()
    fun getAllSessions(): LiveData<List<NewEdictSession>> = edictSessionDao.getAllEdictSessions()
//    fun getEdictsByDeadlineType(deadlineType: String): List<Edict> = edictDao.getEdictsWithDeadlineType(deadlineType)
//
//    fun deleteEdict(edict: Edict) = edictDao.deleteEdict(edict)
//    fun updateEdict(edict: Edict) = edictDao.updateEdict(edict)
    fun insertNewEdict(newEdict: NewEdict) = newEdictDao.insertNewEdict(newEdict)
    fun insertEdictGetId(edict: NewEdict): Long = newEdictDao.insertEdictGetId(edict)
//
//    fun getLiveEdictById(id: Int): LiveData<Edict> = edictDao.getLiveEdictById(id)
//    suspend fun getEdictById(id: Int): Edict = edictDao.getEdictById(id)
//
    fun getUnresolvedSessions(): LiveData<List<NewEdictSession>> = edictSessionDao.getUnresolvedSessions()
    suspend fun insertEdictSessions(edictSession: NewEdictSession) = edictSessionDao.insertEdictSessions(edictSession)

    suspend fun updateSessions(sessions: List<NewEdictSession>) = edictSessionDao.updateEdictSessions(sessions)
//
//    fun getEdictSessionsById(id: Int): LiveData<List<EdictSession>> = edictSessionDao.getEdictSessionsByEdictId(id)

//    Unused so far
//    fun updateEdict(edict: Edict) = edictDao.updateEdict(edict)
//    fun updateEdictSessions(edictSession: EdictSession) = edictSessionDao.updateEdictSessions(edictSession)

//    suspend fun insertEdictSessions(edictSessions: List<EdictSession>) = edictSessionDao.insertEdictSessions(edictSessions)
}