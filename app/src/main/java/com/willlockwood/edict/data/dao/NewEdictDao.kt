package com.willlockwood.edict.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.willlockwood.edict.data.model.NewEdict

@Dao
interface NewEdictDao {

    @Query("SELECT * from new_edicts")
    fun getAllNewEdicts(): LiveData<List<NewEdict>>

//    @Query("SELECT * from edicts where :deadlineType == deadlineType")
//    fun getEdictsWithDeadlineType(deadlineType: String): List<Edict>

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insertEdict(edicts: List<Edict>)

//    @Update
//    fun updateEdict(edict: Edict)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNewEdict(edict: NewEdict)

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insertEdictGetId(edict: Edict): Long

//    @Delete
//    fun deleteEdict(edict: Edict)
//
//    @Query("SELECT * from edicts where :id == id")
//    fun getLiveEdictById(id: Int): LiveData<Edict>
//
//    @Query("SELECT * from edicts where :id == id")
//    suspend fun getEdictById(id: Int): Edict
}