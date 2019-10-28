package com.willlockwood.edict.fragment

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.willlockwood.edict.R
import com.willlockwood.edict.activity.MainActivity
import com.willlockwood.edict.data.model.NewEdict
import kotlinx.android.synthetic.main.dialog_create_edict_times.*

class CreateEdictTimesDialog(
    private var newEdict: NewEdict
): DialogFragment() {

    // Interfaces and Listeners
    private lateinit var listener: TimesDialogListener
    interface TimesDialogListener {
        fun setTimesType(type: NewEdict.TimeType) {}
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_create_edict_times, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setInitialTimeType()

        setUpClickListeners()

        val v = dialog!!.window!!.decorView
        v.setBackgroundResource(R.color.transparent)
    }

    override fun onDismiss(dialog: DialogInterface) {
//        (requireActivity() as MainActivity).hideKeyboard(view!!)
        super.onDismiss(dialog)
    }

    private fun setInitialTimeType() {
        val radioButton = when (newEdict.timeType) {
            NewEdict.TimeType.ALL_DAY ->        all_the_time_rb
            NewEdict.TimeType.AFTER_TIME ->     after_time_rb
            NewEdict.TimeType.AFTER_TEXT ->     after_text_rb
            NewEdict.TimeType.BEFORE_TIME ->    before_time_rb
            NewEdict.TimeType.BEFORE_TEXT ->    before_text_rb
            NewEdict.TimeType.BETWEEN_TIME ->   between_time_rb
            NewEdict.TimeType.BETWEEN_TEXT ->   between_text_rb
            NewEdict.TimeType.WHEN ->           when_rb
            NewEdict.TimeType.WHILE ->          while_rb
            else -> null
        }
        radioButton?.isChecked = true
    }

    private fun setUpClickListeners() {
        listener = targetFragment as TimesDialogListener

        all_the_time_rb.setOnClickListener  { setTimeType(NewEdict.TimeType.ALL_DAY) }
        after_time_rb.setOnClickListener    { setTimeType(NewEdict.TimeType.AFTER_TIME) }
        before_time_rb.setOnClickListener   { setTimeType(NewEdict.TimeType.BEFORE_TIME) }
        between_time_rb.setOnClickListener  { setTimeType(NewEdict.TimeType.BETWEEN_TIME) }
        after_text_rb.setOnClickListener    { setTimeType(NewEdict.TimeType.AFTER_TEXT) }
        before_text_rb.setOnClickListener   { setTimeType(NewEdict.TimeType.BEFORE_TEXT) }
        between_text_rb.setOnClickListener  { setTimeType(NewEdict.TimeType.BETWEEN_TEXT) }
        when_rb.setOnClickListener          { setTimeType(NewEdict.TimeType.WHEN) }
        while_rb.setOnClickListener         { setTimeType(NewEdict.TimeType.WHILE) }
    }

    private fun setTimeType(type: NewEdict.TimeType) {
        listener.setTimesType(type)
        (requireActivity() as MainActivity).hideKeyboard(view!!)
        this.dismiss()
    }
}


