package com.willlockwood.edict.data.converter

import androidx.room.TypeConverter

object MapConverters {

    @TypeConverter
    @JvmStatic
    fun toMap(value: String?): Map<String, Int>? {
        if (value == "") { return emptyMap() }

        val pairStringList = value!!.split(";")
        var returnMap = emptyMap<String, Int>()

        if (pairStringList.isNotEmpty()) {
            for (pair in pairStringList) {
                returnMap = returnMap.plus(Pair(pair.split(",")[0], pair.split(",")[1].toInt()))
            }
        }
        return returnMap
    }

    @TypeConverter
    @JvmStatic
    fun fromMap(value: Map<String, Int>?): String? {
        var pairStringList = emptyList<String>()
        for (entry in value!!.entries) {
            pairStringList = pairStringList.plus("${entry.key},${entry.value}")
        }
        return pairStringList.joinToString(";")
    }
}