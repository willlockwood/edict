package com.willlockwood.edict.fragment

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.DialogFragment
import com.willlockwood.edict.R
import com.willlockwood.edict.activity.MainActivity
import com.willlockwood.edict.data.model.NewEdict
import kotlinx.android.synthetic.main.dialog_create_edict_action.*
import kotlinx.android.synthetic.main.dialog_number_picker.cancel_btn
import kotlinx.android.synthetic.main.dialog_number_picker.done_btn

class CreateEdictActionDialog(
    private var newEdict: NewEdict,
    private var nextScalable: Boolean?
): DialogFragment() {

    // Interfaces and Listeners
    private lateinit var listener: ActionDialogListener
    interface ActionDialogListener {
        fun setSwitchChecked(checked: Boolean)
        fun setScalable(scalable: Boolean) {}
        fun setAction(action: String, unitVar: Int? = null, unit: String? = null) {}
    }

    private var action: String = ""
    private var number: String = ""
    private var unit: String = ""
    private var scalable: Boolean? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_create_edict_action, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listener = targetFragment as ActionDialogListener

        if (newEdict.action != null) action = newEdict.action!!
        if (newEdict.unitVar != null) number = newEdict.unitVar.toString()
        if (newEdict.unit != null) unit = newEdict.unit!!

        scalable = nextScalable ?: newEdict.scalable

        setUpVisibility()

        setUpTexts(action, number, unit)

        setUpClickListeners()

        val v = dialog!!.window!!.decorView
        v.setBackgroundResource(R.color.transparent)
    }

    override fun onDismiss(dialog: DialogInterface) {
        (requireActivity() as MainActivity).hideKeyboard(view!!)
        super.onDismiss(dialog)
    }

    private fun setUpVisibility() {
        when (scalable) {
            false -> {
                var_et.visibility = View.GONE
                unit_et.visibility = View.GONE
            }
        }
    }

    private fun setUpTexts(action: String?, number: String, unit: String?) {
        // Set texts to current values
        action_et.setText(action)
        var_et.setText(number)
        unit_et.setText(unit)

        // Set hints
        val hints = newEdict.getActionHints()
        action_et.hint = hints.first
        var_et.hint = hints.second
        unit_et.hint = hints.third

        // Set up header
        val header = when (newEdict.type) {
            NewEdict.Type.RESTRICTION -> "Restricted action:"
            NewEdict.Type.ROUTINE -> "Routine action:"
            null -> ""
        }
        header_txt.text = header

        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

    private fun setUpClickListeners() {
        cancel_btn.setOnClickListener {
            if (newEdict.scalable != scalable) {
                listener.setSwitchChecked(newEdict.scalable!!)
            }
            this.dismiss()
        }
        done_btn.setOnClickListener {
            val action = action_et.text?.toString()
            val unitVar = var_et.text?.toString()
            val unit = unit_et.text?.toString()
            when (scalable) {
                true -> {
                    if (action != "" && action != null && unitVar != "" && unitVar != null && unit != "" && unit != null ) {
                        listener.setScalable(scalable!!)
                        listener.setAction(action, unitVar.toInt(), unit)
                        this.dismiss()
                    }
                }
                false -> {
                    if (action != "" && action != null) {
                        listener.setScalable(scalable!!)
                        listener.setAction(action)
                        this.dismiss()
                    }
                }
            }
        }
    }


}


