package com.willlockwood.edict.utils

object TimeHelper {

    fun minutesToTimeString(min: Int?): String {
        if (min == null) return ""
        val minutes = getMinutesStrFromMinutes(min)
        val hours = getHoursStrFromMinutes(min)
        val amPM = getAmPmStrFromMinutes(min)
        return "$hours:$minutes $amPM"
    }
    fun getMinutesIntFromMinutes(min: Int): Int = min % 60
    private fun getMinutesStrFromMinutes(min: Int): String {
        return when {
            getMinutesIntFromMinutes(min) < 10 -> "0" + getMinutesIntFromMinutes(min).toString()
            else -> getMinutesIntFromMinutes(min).toString()
        }
    }
    fun getHoursIntFromMinutes(min: Int): Int = min / 60
    private fun getHoursStrFromMinutes(min: Int): String {
        val hourInt = (getHoursIntFromMinutes(min))
        return when {
            hourInt < 1 -> "12"
            hourInt > 12 -> "${hourInt - 12}"
            else -> "$hourInt"
        }
    }
    private fun getAmPmStrFromMinutes(min: Int): String {
        return when {
            min < 720 -> "AM"
            else -> "PM"
        }
    }
    fun getMinutesFromTimeString(time: String?): Int? {
        if (time == null || time == "") return null
        val hoursString = time.split(":")[0]
        val amPmAddition = when (time.split(" ")[1]) {
            "AM" -> 0
            else -> 12
        }
        val hours = when (hoursString){
            "12" -> amPmAddition
            else -> hoursString.toInt() + amPmAddition
        }
        val minutes = time.split(":")[1].split(" ")[0].toInt()
        return hours * 60 + minutes
    }
}