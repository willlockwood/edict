package com.willlockwood.edict.preferences

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import androidx.preference.DialogPreference
import com.willlockwood.edict.R
import com.willlockwood.edict.utils.TimeHelper


class TimePreference(ctx: Context, attrs: AttributeSet) : DialogPreference(ctx, attrs) {

    private var time: Int? = null

    init {
        positiveButtonText = "Set"
        negativeButtonText = "Cancel"
    }

    fun getTime(): Int? = time
    fun setTime(time: Int) {
        this.time = time
        persistInt(time)
        summary = TimeHelper.minutesToTimeString(time)
    }

    private var dialogLayoutResId: Int = R.layout.pref_dialog_time
    override fun getDialogLayoutResource(): Int {
        return dialogLayoutResId
    }

    override fun onSetInitialValue(restorePersistedValue: Boolean, defaultValue: Any?) {
        if (restorePersistedValue) {
            if (defaultValue == null) {
                setTime(getPersistedInt(0))
            } else {
                setTime(getPersistedInt(defaultValue as Int))
            }
        } else {
            setTime(defaultValue as Int)
        }
    }
    override fun onGetDefaultValue(a: TypedArray?, index: Int): Any {
        return a!!.getInt(index, 0)
    }

}