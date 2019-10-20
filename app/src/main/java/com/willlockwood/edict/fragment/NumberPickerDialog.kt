package com.willlockwood.edict.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.willlockwood.edict.R
import kotlinx.android.synthetic.main.dialog_number_picker.*

class NumberPickerDialog(value: Int): DialogFragment() {

    interface NumberPickerDialogListener {
        fun setMinutes(value: Int) {}
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_number_picker, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listener = targetFragment as NumberPickerDialogListener

        val values = arrayOf("5 min.", "10 min.", "15 min.", "30 min.", "45 min.", "60 min.", "90 min.")
        minutes_picker.displayedValues = values
        minutes_picker.minValue = 0
        minutes_picker.maxValue = values.size - 1
        minutes_picker.value = 2

        cancel_btn.setOnClickListener { this.dismiss() }
        done_btn.setOnClickListener {
            val minutes = when (minutes_picker.value) {
                0 -> 5
                1 -> 10
                2 -> 15
                3 -> 30
                4 -> 45
                5 -> 60
                6 -> 90
                else -> 0
            }
            listener.setMinutes(minutes)
            this.dismiss()
        }
    }
}


