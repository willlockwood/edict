package com.willlockwood.edict.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.willlockwood.edict.data.model.Edict

@Dao
interface EdictDao {

    @Query("SELECT * from edicts")
    fun getAllEdicts(): LiveData<List<Edict>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEdict(edict: Edict)
}