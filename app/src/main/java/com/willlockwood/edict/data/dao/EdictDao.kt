package com.willlockwood.edict.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.willlockwood.edict.data.model.Edict

@Dao
interface EdictDao {

    @Query("SELECT * from edicts")
    fun getAllEdicts(): LiveData<List<Edict>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEdict(edict: Edict)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEdictGetId(edict: Edict): Long

    @Update
    fun updateEdict(edict: Edict)

    @Query("SELECT * from edicts where :id == id")
    fun getLiveEdictById(id: Int): LiveData<Edict>

    @Query("SELECT * from edicts where :id == id")
    suspend fun getEdictById(id: Int): Edict
}