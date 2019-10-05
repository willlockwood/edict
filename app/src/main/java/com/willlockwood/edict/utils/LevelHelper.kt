package com.willlockwood.edict.utils

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.willlockwood.edict.R
import kotlin.math.pow

object LevelHelper {

    enum class ColorType {
        NORMAL, LIGHT, DARK, TEXT
    }

    private fun getColorResId(level: Int, type: ColorType): Int {
        return when (type) {
            ColorType.NORMAL -> when (level) {
                0 -> R.color.edict_level_0
                1 -> R.color.edict_level_1
                2 -> R.color.edict_level_2
                3 -> R.color.edict_level_3
                4 -> R.color.edict_level_4
                5 -> R.color.edict_level_5
                6 -> R.color.edict_level_6
                7 -> R.color.edict_level_7
                else -> R.color.edict_level_7
            }
            ColorType.LIGHT -> when (level) {
                0 -> R.color.edict_level_0_light
                1 -> R.color.edict_level_1_light
                2 -> R.color.edict_level_2_light
                3 -> R.color.edict_level_3_light
                4 -> R.color.edict_level_4_light
                5 -> R.color.edict_level_5_light
                6 -> R.color.edict_level_6_light
                7 -> R.color.edict_level_7_light
                else -> R.color.edict_level_7_light
            }
            ColorType.DARK -> when (level) {
                0 -> R.color.edict_level_0_dark
                1 -> R.color.edict_level_1_dark
                2 -> R.color.edict_level_2_dark
                3 -> R.color.edict_level_3_dark
                4 -> R.color.edict_level_4_dark
                5 -> R.color.edict_level_5_dark
                6 -> R.color.edict_level_6_dark
                7 -> R.color.edict_level_7_dark
                else -> R.color.edict_level_7_dark
            }
            ColorType.TEXT -> when (level) {
                0 -> R.color.black
                1 -> R.color.black
                2 -> R.color.white
                3 -> R.color.white
                4 -> R.color.black
                5 -> R.color.white
                6 -> R.color.white
                7 -> R.color.white
                else -> R.color.white
            }

        }
    }

    private fun getIconResId(level: Int): Int {
        return when (level) {
            0 -> R.drawable.level_icon_trend_down_black_24dp
            1 -> R.drawable.level_icon_trend_up_black_24dp
            2 -> R.drawable.level_icon_snowflake_black_24dp
            3 -> R.drawable.level_icon_flame_black_24dp
            4 -> R.drawable.level_icon_lightning_black_24dp
            5 -> R.drawable.level_icon_heart_white_24dp
            6 -> R.drawable.level_icon_infinity_white_24dp
            7 -> R.drawable.ic_launcher_foreground
            else -> R.drawable.ic_launcher_foreground
        }
    }

    fun getTextColor(level: Int): Int {
        return when (level) {
            0 -> Color.BLACK
            1 -> Color.BLACK
            2 -> Color.WHITE
            3 -> Color.WHITE
            4 -> Color.BLACK
            5 -> Color.WHITE
            6 -> Color.WHITE
            7 -> Color.WHITE
            else -> Color.WHITE

        }
    }

    fun getLevelStreakMax(level: Int): Int {
        return 2f.pow(level).toInt()
    }

    fun getLevelStreakMin(level: Int): Int {
        return when (level) {
            0 -> 0
            else -> 2f.pow(level - 1).toInt()
        }
    }

    fun getColorStateList(context: Context, level: Int, type: ColorType): ColorStateList? {
        val resId = getColorResId(level, type)
        return ContextCompat.getColorStateList(context, resId)
    }

    fun getColor(context: Context, level: Int, type: ColorType): Int {
        val resId = getColorResId(level, type)
        return ContextCompat.getColor(context, resId)
    }

    fun getIconDrawable(context: Context, level: Int): Drawable {
        return ContextCompat.getDrawable(context, getIconResId(level))!!
    }
}

