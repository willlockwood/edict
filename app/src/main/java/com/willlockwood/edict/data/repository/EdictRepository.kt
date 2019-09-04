package com.willlockwood.edict.data.repository

import androidx.lifecycle.LiveData
import com.willlockwood.edict.data.dao.EdictDao
import com.willlockwood.edict.data.model.Edict

class EdictRepository(
    private val edictDao: EdictDao
) {
    fun getAllEdicts(): LiveData<List<Edict>> = edictDao.getAllEdicts()
}