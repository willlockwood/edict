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

    fun insertEdict(edict: Edict) = edictDao.insertEdict(edict)
    fun insertEdict2(edict: Edict): Long = edictDao.insertEdict2(edict)

    fun updateEdict(edict: Edict) = edictDao.updateEdict(edict)

    fun updateEdictSession(edictSession: EdictSession) = edictSessionDao.updateEdictSession(edictSession)

    fun getLiveEdictById(id: Int): LiveData<Edict> = edictDao.getLiveEdictById(id)
    suspend fun getEdictById(id: Int): Edict = edictDao.getEdictById(id)

    fun getActiveEdictSessions(): LiveData<List<EdictSession>> = edictSessionDao.getActiveEdictSessions()

    fun getEdictSessionsById(id: Int): LiveData<List<EdictSession>> = edictSessionDao.getEdictSessionsByEdictId(id)

    suspend fun insertEdictSessions(edictSession: EdictSession) = edictSessionDao.insertEdictSessions(edictSession)
    suspend fun insertEdictSessions(edictSessions: List<EdictSession>) = edictSessionDao.insertEdictSessions(edictSessions)

}