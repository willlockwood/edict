package com.willlockwood.edict.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.willlockwood.edict.data.model.EdictSession

@Dao
interface EdictSessionDao {

    @Query("SELECT * from edict_sessions")
    fun getAllEdictSessions(): LiveData<List<EdictSession>>

    @Query("SELECT * from edict_sessions where edict == :id")
    fun getEdictSessionsByEdictId(id: Int): LiveData<List<EdictSession>>

    @Query("SELECT * from edict_sessions where checked == 0")
    fun getActiveEdictSessions(): LiveData<List<EdictSession>>

    @Update
    fun updateEdictSession(edictSession: EdictSession)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEdictSessions(edictSession: EdictSession)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEdictSessions(edictSessions: List<EdictSession>)
}