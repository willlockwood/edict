package com.willlockwood.edict.data.repository

import androidx.lifecycle.LiveData
import com.willlockwood.edict.data.dao.NewEdictDao
import com.willlockwood.edict.data.model.NewEdict

class NewEdictRepository(
    private val newEdictDao: NewEdictDao
//    private val edictDao: EdictDao,
//    private val edictSessionDao: EdictSessionDao
) {

    fun getAllNewEdicts(): LiveData<List<NewEdict>> = newEdictDao.getAllNewEdicts()
//    fun getEdictsByDeadlineType(deadlineType: String): List<Edict> = edictDao.getEdictsWithDeadlineType(deadlineType)
//
//    fun deleteEdict(edict: Edict) = edictDao.deleteEdict(edict)
//    fun updateEdict(edict: Edict) = edictDao.updateEdict(edict)
    fun insertNewEdict(newEdict: NewEdict) = newEdictDao.insertNewEdict(newEdict)
//    fun insertEdictGetId(edict: Edict): Long = edictDao.insertEdictGetId(edict)
//
//    fun getLiveEdictById(id: Int): LiveData<Edict> = edictDao.getLiveEdictById(id)
//    suspend fun getEdictById(id: Int): Edict = edictDao.getEdictById(id)
//
//    fun getActiveEdictSessions(): LiveData<List<EdictSession>> = edictSessionDao.getActiveEdictSessions()
//
//    suspend fun insertEdictSessions(edictSession: EdictSession) = edictSessionDao.insertEdictSessions(edictSession)
//
//    fun getEdictSessionsById(id: Int): LiveData<List<EdictSession>> = edictSessionDao.getEdictSessionsByEdictId(id)

//    Unused so far
//    fun updateEdict(edict: Edict) = edictDao.updateEdict(edict)
//    fun updateEdictSession(edictSession: EdictSession) = edictSessionDao.updateEdictSession(edictSession)

//    suspend fun insertEdictSessions(edictSessions: List<EdictSession>) = edictSessionDao.insertEdictSessions(edictSessions)
}