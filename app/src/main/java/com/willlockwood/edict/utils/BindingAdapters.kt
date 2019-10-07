package com.willlockwood.edict.utils

import android.os.Build
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Guideline
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.google.android.material.chip.Chip
import com.google.android.material.floatingactionbutton.FloatingActionButton


object BindingAdapters {

    @BindingAdapter(value = ["visibilityBasedOnSelection"], requireAll = false)
    @JvmStatic
    fun setVisibilityBasedOnSelection(view: View, visibilityBasedOnSelection: String?){
        when {
            view.id == com.willlockwood.edict.R.id.between_layout      && visibilityBasedOnSelection == "between" ->   view.visibility = View.VISIBLE
            view.id == com.willlockwood.edict.R.id.after_txt           && visibilityBasedOnSelection == "after" ->     view.visibility = View.VISIBLE
            view.id == com.willlockwood.edict.R.id.at_txt              && visibilityBasedOnSelection == "at" ->        view.visibility = View.VISIBLE
            view.id == com.willlockwood.edict.R.id.on_layout           && visibilityBasedOnSelection == "on" ->        view.visibility = View.VISIBLE
            view.id == com.willlockwood.edict.R.id.before_txt          && visibilityBasedOnSelection == "before" ->    view.visibility = View.VISIBLE
            view.id == com.willlockwood.edict.R.id.when_txt            && visibilityBasedOnSelection == "when" ->      view.visibility = View.VISIBLE
            view.id == com.willlockwood.edict.R.id.while_txt           && visibilityBasedOnSelection == "while" ->     view.visibility = View.VISIBLE
            view.id == com.willlockwood.edict.R.id.every_layout        && visibilityBasedOnSelection == "every" ->     view.visibility = View.VISIBLE
            view.id == com.willlockwood.edict.R.id.every_time_txt      && visibilityBasedOnSelection == "time" ->      view.visibility = View.VISIBLE
            view.id == com.willlockwood.edict.R.id.every_number_layout && visibilityBasedOnSelection == "#" ->         view.visibility = View.VISIBLE
            view.id == com.willlockwood.edict.R.id.number_layout       && visibilityBasedOnSelection == "#" ->         view.visibility = View.VISIBLE
            view.id == com.willlockwood.edict.R.id.deadline_custom_time && visibilityBasedOnSelection == "at" ->         view.visibility = View.VISIBLE
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
            view.id == com.willlockwood.edict.R.id.notify_start && detailType == "between" -> view.visibility = View.VISIBLE
            view.id == com.willlockwood.edict.R.id.notify_start && detailType == "after" -> view.visibility = View.VISIBLE
            view.id == com.willlockwood.edict.R.id.notify_end && detailType == "between" -> view.visibility = View.VISIBLE
            view.id == com.willlockwood.edict.R.id.notify_end && detailType == "before" -> view.visibility = View.VISIBLE
            else -> view.visibility = View.GONE
        }
    }

    @BindingAdapter("srcIconFromLevel")
    @JvmStatic
    fun setSrcIconFromLevel(view: ImageView, level: Int){
        view.setImageDrawable(LevelHelper.getIconDrawable(view.context, level))
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
    fun setIconChipStyle(view: Chip, level: Int){
        view.chipIcon = LevelHelper.getIconDrawable(view.context, level)
        view.chipBackgroundColor = LevelHelper.getColorStateList(view.context, level, LevelHelper.ColorType.NORMAL)
    }

    @BindingAdapter("backgroundColorFromLevel")
    @JvmStatic
    fun setBackgroundColorFromLevel(view: View, level: Int){
        view.setBackgroundColor(LevelHelper.getColor(view.context, level, LevelHelper.ColorType.NORMAL))
    }

    @BindingAdapter("backgroundColorFromLevel")
    @JvmStatic
    fun setBackgroundColorFromLevel(view: ConstraintLayout, level: Int){
        view.setBackgroundColor(LevelHelper.getColor(view.context, level, LevelHelper.ColorType.NORMAL))
    }

    @BindingAdapter("cardBackgroundColorFromLevel")
    @JvmStatic
    fun setCardBackgroundColorFromLevel(view: CardView, level: Int){
        view.setCardBackgroundColor(LevelHelper.getColor(view.context, level, LevelHelper.ColorType.NORMAL))
    }

    @BindingAdapter("backgroundColorLightFromLevel")
    @JvmStatic
    fun setBackgroundColorLightFromLevel(view: ConstraintLayout, level: Int){
        view.setBackgroundColor(LevelHelper.getColor(view.context, level, LevelHelper.ColorType.LIGHT))
    }

    @BindingAdapter("backgroundColorDarkFromLevel")
    @JvmStatic
    fun setBackgroundColorDarkFromLevel(view: View, level: Int){
        view.setBackgroundColor(LevelHelper.getColor(view.context, level, LevelHelper.ColorType.DARK))
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @BindingAdapter("progressTintDarkFromLevel")
    @JvmStatic
    fun setProgressTintDarkFromLevel(view: ProgressBar, level: Int){
        view.progressBackgroundTintList = LevelHelper.getColorStateList(view.context, level, LevelHelper.ColorType.LIGHT)
        view.progressTintList = LevelHelper.getColorStateList(view.context, level, LevelHelper.ColorType.DARK)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @BindingAdapter("backgroundColorTintDarkFromLevel")
    @JvmStatic
    fun setBackgroundColorTintDarkFromLevel(view: FloatingActionButton, level: Int){
        view.backgroundTintList = LevelHelper.getColorStateList(view.context, level, LevelHelper.ColorType.DARK)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @BindingAdapter("iconColorFromLevel")
    @JvmStatic
    fun setIconColorFromLevel(view: FloatingActionButton, level: Int){
        // TODO: maybe start putting all these different color-related binding adapters into ViewModels instead of all jammed in here. just have a binding VM return colorStateLists to the xml attributes, etc.
        view.imageTintList = LevelHelper.getColorStateList(view.context, level, LevelHelper.ColorType.TEXT)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @BindingAdapter("iconColorFromLevel")
    @JvmStatic
    fun setIconColorFromLevel(view: ImageView, level: Int) {
        view.imageTintList = LevelHelper.getColorStateList(view.context, level, LevelHelper.ColorType.TEXT)
    }

    @BindingAdapter("textColorFromLevel")
    @JvmStatic
    fun setTextColorFromLevel(view: TextView, level: Int) {
        view.setTextColor(LevelHelper.getTextColor(level))
    }

    @BindingAdapter("checkedIconFromBoolean")
    @JvmStatic
    fun setCheckedIconFromBoolean(view: ImageView, success: Boolean?){
        if (success != null) {
            if (success) {
                view.setImageDrawable(ContextCompat.getDrawable(view.context, com.willlockwood.edict.R.drawable.ic_check_black_24dp))
            } else {
                view.setImageDrawable(ContextCompat.getDrawable(view.context, com.willlockwood.edict.R.drawable.ic_close_black_24dp))
            }
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

    @BindingAdapter("guidePercent")
    @JvmStatic
    fun setGuidePercent(guideline: Guideline, percent: Float) {
        val params = guideline.layoutParams as ConstraintLayout.LayoutParams
        params.guidePercent = percent
        guideline.layoutParams = params
    }
}