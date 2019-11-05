package com.willlockwood.edict.data.repository

import androidx.lifecycle.LiveData
import com.willlockwood.edict.data.dao.EdictDao
import com.willlockwood.edict.data.dao.EdictSessionDao
import com.willlockwood.edict.data.model.Edict
import com.willlockwood.edict.data.model.EdictSession

class EdictRepository(
    private val edictDao: EdictDao,
    private val edictSessionDao: EdictSessionDao
) {

    fun getAllEdicts(): LiveData<List<Edict>> = edictDao.getAllEdicts()
    fun getEdictsByDeadlineType(deadlineType: String): List<Edict> = edictDao.getEdictsWithDeadlineType(deadlineType)

    fun deleteEdict(edict: Edict) = edictDao.deleteEdict(edict)
    fun updateEdict(edict: Edict) = edictDao.updateEdict(edict)
    fun insertEdict(edict: Edict) = edictDao.insertEdict(edict)
    fun insertEdictGetId(edict: Edict): Long = edictDao.insertEdictGetId(edict)

    fun getLiveEdictById(id: Int): LiveData<Edict> = edictDao.getLiveEdictById(id)
    suspend fun getEdictById(id: Int): Edict = edictDao.getEdictById(id)

    fun getActiveEdictSessions(): LiveData<List<EdictSession>> = edictSessionDao.getActiveEdictSessions()

    suspend fun insertEdictSessions(edictSession: EdictSession) = edictSessionDao.insertEdictSessions(edictSession)

    fun getEdictSessionsById(id: Int): LiveData<List<EdictSession>> = edictSessionDao.getEdictSessionsByEdictId(id)

//    Unused so far
//    fun updateEdict(edict: Edict) = edictDao.updateEdict(edict)
//    fun updateEdictSessions(edictSession: EdictSession) = edictSessionDao.updateEdictSessions(edictSession)

//    suspend fun insertEdictSessions(edictSessions: List<EdictSession>) = edictSessionDao.insertEdictSessions(edictSessions)
}