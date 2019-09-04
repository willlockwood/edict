package com.willlockwood.edict.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Edict(
    var type: String,
    var parameterType: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}