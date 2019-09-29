package com.willlockwood.edict.utils

import android.view.View
import android.widget.NumberPicker
import android.widget.TextView
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

    @BindingAdapter("uncheckedFromBoolean")
    @JvmStatic
    fun setUncheckedFromBoolean(view: Chip, uncheckedFromBoolean: Boolean) {
        if (uncheckedFromBoolean) {
            view.isChecked = false
        }
    }

//    @BindingAdapter("highlightOnError")
//    @JvmStatic
//    fun setHighlightOnError(view: View, highlightOnError: Edict.Status){
//        when {
//            view.id == R.id.activity_text && highlightOnError == Edict.Status.ACTIVITY_STILL_DEFAULT -> (view as EditText).background =
//        }
//    }

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