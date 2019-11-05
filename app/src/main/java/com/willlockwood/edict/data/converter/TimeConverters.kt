package com.willlockwood.edict.data.converter

import androidx.room.TypeConverter
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.format.DateTimeFormatter

object TimeConverters {

    private val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

    @TypeConverter
    @JvmStatic
    fun toOffsetDateTime(value: String?): OffsetDateTime? {
        return value?.let {
            return formatter.parse(value, OffsetDateTime::from)
        }
    }

    @TypeConverter
    @JvmStatic
    fun fromOffsetDateTime(date: OffsetDateTime?): String? {
        return date?.format(formatter)
    }

    @TypeConverter
    @JvmStatic
    fun fromTimePair(pair: Pair<OffsetDateTime?, OffsetDateTime?>): String {
        return "${fromOffsetDateTime(pair.first)};${fromOffsetDateTime(pair.second)}"
    }

    @TypeConverter
    @JvmStatic
    fun toTimePair(value: String): Pair<OffsetDateTime?, OffsetDateTime?> {
        val times = value.split(";").map { toOffsetDateTime(it) }
        return Pair(times[0], times[1])
    }
}