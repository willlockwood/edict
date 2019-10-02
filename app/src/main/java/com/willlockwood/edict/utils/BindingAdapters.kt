package com.willlockwood.edict.utils

import android.content.res.ColorStateList
import android.os.Build
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.NumberPicker
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.google.android.material.chip.Chip
import com.willlockwood.edict.R

object BindingAdapters {

    @BindingAdapter(value = ["visibilityBasedOnSelection"], requireAll = false)
    @JvmStatic
    fun setVisibilityBasedOnSelection(view: View, visibilityBasedOnSelection: String?){
        when {
            view.id == R.id.between_layout      && visibilityBasedOnSelection == "between" ->   view.visibility = View.VISIBLE
            view.id == R.id.after_txt           && visibilityBasedOnSelection == "after" ->     view.visibility = View.VISIBLE
            view.id == R.id.at_txt              && visibilityBasedOnSelection == "at" ->        view.visibility = View.VISIBLE
            view.id == R.id.on_layout           && visibilityBasedOnSelection == "on" ->        view.visibility = View.VISIBLE
            view.id == R.id.before_txt          && visibilityBasedOnSelection == "before" ->    view.visibility = View.VISIBLE
            view.id == R.id.when_txt            && visibilityBasedOnSelection == "when" ->      view.visibility = View.VISIBLE
            view.id == R.id.while_txt           && visibilityBasedOnSelection == "while" ->     view.visibility = View.VISIBLE
            view.id == R.id.every_layout        && visibilityBasedOnSelection == "every" ->     view.visibility = View.VISIBLE
            view.id == R.id.every_time_txt      && visibilityBasedOnSelection == "time" ->      view.visibility = View.VISIBLE
            view.id == R.id.every_number_layout && visibilityBasedOnSelection == "#" ->         view.visibility = View.VISIBLE
            view.id == R.id.number_layout       && visibilityBasedOnSelection == "#" ->         view.visibility = View.VISIBLE
            view.id == R.id.deadline_custom_time && visibilityBasedOnSelection == "at" ->         view.visibility = View.VISIBLE
            else -> view.visibility = View.GONE
        }
    }

    @BindingAdapter("errorTextFromStatus")
    @JvmStatic
    fun setErrorTextFromStatus(view: TextView, errorTextFromStatus: EdictHelper.ErrorStatus){
        view.text = when (errorTextFromStatus) {
            EdictHelper.ErrorStatus.ACTIVITY_STILL_DEFAULT ->  "Change the activity text. \"Do something\" is the default text."
            EdictHelper.ErrorStatus.BEFORE_NOT_BEFORE_AFTER -> "Make sure the second time entered comes after the first one"
            EdictHelper.ErrorStatus.EMPTY_ACTIVITY ->          "You must enter something in the activity box"
            EdictHelper.ErrorStatus.WHEN_STILL_DEFAULT ->      "Change the \"when\" text. \"something else happens\" is the default text."
            EdictHelper.ErrorStatus.WHILE_STILL_DEFAULT ->     "Change the \"while\" text. \"doing something else\" is the default text."
            else -> ""
        }
    }

    @BindingAdapter("visibilityFromBoolean")
    @JvmStatic
    fun setVisibilityFromBoolean(view: TextView, visibilityFromBoolean: Boolean){
        view.visibility = when (visibilityFromBoolean) {
            true -> View.VISIBLE
            false -> View.GONE
        }
    }

    @BindingAdapter("visibilityFromBoolean")
    @JvmStatic
    fun setVisibilityFromBoolean(view: ConstraintLayout, visibilityFromBoolean: Boolean){
        view.visibility = when (visibilityFromBoolean) {
            true -> View.VISIBLE
            false -> View.GONE
        }
    }

    @BindingAdapter("layoutVisibilityFromDetailType")
    @JvmStatic
    fun setLayoutVisibilityFromDetailType(view: LinearLayout, detailType: String){
        when {
            view.id == R.id.notify_start && detailType == "between" -> view.visibility = View.VISIBLE
            view.id == R.id.notify_start && detailType == "after" -> view.visibility = View.VISIBLE
            view.id == R.id.notify_end && detailType == "between" -> view.visibility = View.VISIBLE
            view.id == R.id.notify_end && detailType == "before" -> view.visibility = View.VISIBLE
            else -> view.visibility = View.GONE
        }
    }

    @BindingAdapter("invisibilityFromBoolean")
    @JvmStatic
    fun setInvisibilityFromBoolean(view: ConstraintLayout, invisibilityFromBoolean: Boolean){
        view.visibility = when (invisibilityFromBoolean) {
            false -> View.VISIBLE
            true -> View.GONE
        }
    }

    @BindingAdapter("uncheckedFromBoolean")
    @JvmStatic
    fun setUncheckedFromBoolean(view: Chip, uncheckedFromBoolean: Boolean) {
        if (uncheckedFromBoolean) {
            view.isChecked = false
        }
    }

    @BindingAdapter("iconChipStyle")
    @JvmStatic
    fun setIconChipStyle(view: Chip, edictLevel: Int){
        when (edictLevel) {
            0 -> view.chipIcon = ContextCompat.getDrawable(view.context, R.drawable.level_icon_trend_down_black_24dp)
            1 -> view.chipIcon = ContextCompat.getDrawable(view.context, R.drawable.level_icon_trend_up_black_24dp)
            2 -> view.chipIcon = ContextCompat.getDrawable(view.context, R.drawable.level_icon_snowflake_white_24dp)
            3 -> view.chipIcon = ContextCompat.getDrawable(view.context, R.drawable.level_icon_flame_white_24dp)
            4 -> view.chipIcon = ContextCompat.getDrawable(view.context, R.drawable.level_icon_lightning_black_24dp)
            5 -> view.chipIcon = ContextCompat.getDrawable(view.context, R.drawable.level_icon_heart_white_24dp)
            6 -> view.chipIcon = ContextCompat.getDrawable(view.context, R.drawable.level_icon_infinity_white_24dp)
            7 -> view.chipIcon = ContextCompat.getDrawable(view.context, R.drawable.ic_launcher_foreground)
            else -> view.chipIcon = ContextCompat.getDrawable(view.context, R.drawable.level_icon_lightning_black_24dp)
        }
        when (edictLevel) {
            0 -> view.chipBackgroundColor = ColorStateList.valueOf(ContextCompat.getColor(view.context, R.color.edict_level_0))
            1 -> view.chipBackgroundColor = ColorStateList.valueOf(ContextCompat.getColor(view.context, R.color.edict_level_1))
            2 -> view.chipBackgroundColor = ColorStateList.valueOf(ContextCompat.getColor(view.context, R.color.edict_level_2))
            3 -> view.chipBackgroundColor = ColorStateList.valueOf(ContextCompat.getColor(view.context, R.color.edict_level_3))
            4 -> view.chipBackgroundColor = ColorStateList.valueOf(ContextCompat.getColor(view.context, R.color.edict_level_4))
            5 -> view.chipBackgroundColor = ColorStateList.valueOf(ContextCompat.getColor(view.context, R.color.edict_level_5))
            6 -> view.chipBackgroundColor = ColorStateList.valueOf(ContextCompat.getColor(view.context, R.color.edict_level_6))
            7 -> view.chipBackgroundColor = ColorStateList.valueOf(ContextCompat.getColor(view.context, R.color.edict_level_7))
            else -> view.chipBackgroundColor = ColorStateList.valueOf(ContextCompat.getColor(view.context, R.color.edict_level_4))
        }
    }

    @BindingAdapter("backgroundColorFromLevel")
    @JvmStatic
    fun setBackgroundColorFromLevel(view: View, edictLevel: Int){
        when (edictLevel) {
            0 -> view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.edict_level_0))
            1 -> view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.edict_level_1))
            2 -> view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.edict_level_2))
            3 -> view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.edict_level_3))
            4 -> view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.edict_level_4))
            5 -> view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.edict_level_5))
            6 -> view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.edict_level_6))
            7 -> view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.edict_level_7))
        }
    }

    @BindingAdapter("backgroundColorFromLevel")
    @JvmStatic
    fun setBackgroundColorFromLevel(view: ConstraintLayout, edictLevel: Int){
        when (edictLevel) {
            0 -> view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.edict_level_0))
            1 -> view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.edict_level_1))
            2 -> view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.edict_level_2))
            3 -> view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.edict_level_3))
            4 -> view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.edict_level_4))
            5 -> view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.edict_level_5))
            6 -> view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.edict_level_6))
            7 -> view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.edict_level_7))
        }
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    @BindingAdapter("backgroundIconFromLevel")
    @JvmStatic
    fun setBackgroundIconFromLevel(view: ImageView, edictLevel: Int){
        when (edictLevel) {
            0 -> view.background = ContextCompat.getDrawable(view.context, R.drawable.level_icon_trend_down_black_24dp)
            1 -> view.background = ContextCompat.getDrawable(view.context, R.drawable.level_icon_trend_up_black_24dp)
            2 -> view.background = ContextCompat.getDrawable(view.context, R.drawable.level_icon_snowflake_black_24dp)
            else -> view.background = ContextCompat.getDrawable(view.context, R.drawable.level_icon_flame_black_24dp)
        }
    }

    @BindingAdapter("visibilityBasedOnText")
    @JvmStatic
    fun setVisibilityBasedOnText(view: View, data: String){
        if (data == "") {
            view.visibility = View.GONE
        } else {
            view.visibility = View.VISIBLE
        }
    }

    @BindingAdapter("values")
    @JvmStatic
    fun customSetValues(view: NumberPicker, data: Array<String>){
        view.displayedValues = null
        view.minValue = 0
        view.maxValue = data.size - 1
        view.displayedValues = data
        view.value = 0
    }
}