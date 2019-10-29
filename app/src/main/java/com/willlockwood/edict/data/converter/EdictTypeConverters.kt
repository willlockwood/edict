package com.willlockwood.edict.data.converter

import androidx.room.TypeConverter
import com.willlockwood.edict.data.model.NewEdict
import org.threeten.bp.DayOfWeek

object EdictTypeConverters {

    @TypeConverter
    @JvmStatic
    fun toEdictType(value: String): NewEdict.Type { return NewEdict.Type.valueOf(value) }

    @TypeConverter
    @JvmStatic
    fun fromEdictType(value: NewEdict.Type): String? { return value.name }

    @TypeConverter
    @JvmStatic
    fun toEdictScope(value: String): NewEdict.Scope { return NewEdict.Scope.valueOf(value) }

    @TypeConverter
    @JvmStatic
    fun fromEdictScope(value: NewEdict.Scope): String? { return value.name }

    @TypeConverter
    @JvmStatic
    fun toEdictTimeType(value: String): NewEdict.TimeType { return NewEdict.TimeType.valueOf(value) }

    @TypeConverter
    @JvmStatic
    fun fromEdictTimeType(value: NewEdict.TimeType): String? { return value.name }

    @TypeConverter
    @JvmStatic
    fun toEdictDays(value: String?): List<DayOfWeek> {
        value ?: return emptyList()
        if (value == "") return emptyList()
        val blah = value
        val blah2 = blah
        return value.split(";").map { DayOfWeek.valueOf(it) }
    }

    @TypeConverter
    @JvmStatic
    fun fromEdictDays(value: List<DayOfWeek>): String {
//        value ?: return null
        if (value.isEmpty()) return ""
        return value.joinToString(";") { it.name }
    }

    @TypeConverter
    @JvmStatic
    fun toEdictNotifications(value: String?): List<Pair<NewEdict.NotificationType, Int?>> {
        value ?: return emptyList()
        return value.split(";").map {
            val first= it.split(",")[0]
            val second = it.split(",")[1]
            Pair(
                NewEdict.NotificationType.valueOf(first),
                if (second == "") { null } else { second.toInt() }
            )
        }
    }

    @TypeConverter
    @JvmStatic
    fun fromEdictNotifications(value: List<Pair<NewEdict.NotificationType, Int?>>): String {
        return when (value.size) {
            0 -> ""
            else -> value.joinToString(";") { "${it.first.name},${it.second ?: ""}" }
        }
    }
}