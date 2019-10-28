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
import kotlinx.android.synthetic.main.dialog_create_edict_days.*

class CreateEdictDaysDialog(
    private var newEdict: NewEdict
): DialogFragment() {

    // Interfaces and Listeners
    private lateinit var listener: DaysDialogListener
    interface DaysDialogListener {
        fun setDaysType(scope: NewEdict.Scope) {}
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_create_edict_days, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listener = targetFragment as DaysDialogListener

        setUpInitialCheck()

        setUpClickListeners()

        val v = dialog!!.window!!.decorView
        v.setBackgroundResource(R.color.transparent)
    }

    override fun onDismiss(dialog: DialogInterface) {
//        (requireActivity() as MainActivity).hideKeyboard(view!!)
        super.onDismiss(dialog)
    }

    private fun setUpInitialCheck() {
        when (newEdict.scope) {
            NewEdict.Scope.DAILY ->     daily_rb.isChecked = true
            NewEdict.Scope.WEEKLY ->    weekly_rb.isChecked = true
            NewEdict.Scope.SOME_DAYS -> on_days_rb.isChecked = true
            NewEdict.Scope.TEXT_DAYS -> on_text_days_rb.isChecked = true
            NewEdict.Scope.VAR_DAYS ->  var_days_rb.isChecked = true
        }
    }

    private fun setUpClickListeners() {
        daily_rb.setOnClickListener             { setScope(NewEdict.Scope.DAILY) }
        weekly_rb.setOnClickListener            { setScope(NewEdict.Scope.WEEKLY) }
        on_days_rb.setOnClickListener           { setScope(NewEdict.Scope.SOME_DAYS) }
        on_text_days_rb.setOnClickListener      { setScope(NewEdict.Scope.TEXT_DAYS) }
        var_days_rb.setOnClickListener          { setScope(NewEdict.Scope.VAR_DAYS) }
    }

    private fun setScope(type: NewEdict.Scope) {
        listener.setDaysType(type)
        (requireActivity() as MainActivity).hideKeyboard(view!!)
        this.dismiss()
    }
}


