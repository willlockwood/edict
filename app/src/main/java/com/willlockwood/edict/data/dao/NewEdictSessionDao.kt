package com.willlockwood.edict.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.willlockwood.edict.data.model.NewEdictSession

@Dao
interface NewEdictSessionDao {

    @Query("SELECT * from new_edict_sessions")
    fun getAllEdictSessions(): LiveData<List<NewEdictSession>>

    @Query("SELECT * from new_edict_sessions where edict == :id")
    fun getEdictSessionsByEdictId(id: Int): LiveData<List<NewEdictSession>>

    @Query("SELECT * from new_edict_sessions where checked == 0")
    fun getUnresolvedSessions(): LiveData<List<NewEdictSession>>

    @Query("SELECT * from new_edict_sessions where checked == 0")
    fun getActiveEdictSessions(): LiveData<List<NewEdictSession>>

    @Update
    fun updateEdictSessions(edictSession: NewEdictSession)

    @Update
    fun updateEdictSessions(edictSessions: List<NewEdictSession>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEdictSessions(edictSession: NewEdictSession)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEdictSessions(edictSessions: List<NewEdictSession>)
}