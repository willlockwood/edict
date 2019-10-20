package com.willlockwood.edict.fragment.NewEdict


import android.app.Activity
import android.app.TimePickerDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.willlockwood.edict.BR
import com.willlockwood.edict.R
import com.willlockwood.edict.data.model.NewEdict
import com.willlockwood.edict.utils.TimeHelper
import com.willlockwood.edict.viewmodel.ToolbarVM
import com.willlockwood.edict.viewmodel.binding.NewNewEdictVM
import kotlinx.android.synthetic.main.fragment_new_edict_deadline.*
import java.util.*

class NewEdictDeadline : Fragment() {

    private lateinit var toolbarVM: ToolbarVM
    private lateinit var binding: ViewDataBinding
    private lateinit var vm: NewNewEdictVM
    private lateinit var newEdict: NewEdict
    private lateinit var extras: Bundle
    private lateinit var userState: NewEdict.UserEditingOrCreating

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        extras = this.arguments!!
        userState = extras.get("userState") as NewEdict.UserEditingOrCreating
        newEdict = NewEdict().populateFromBundle(extras)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val sharedPreferences = activity!!.getSharedPreferences("com.willlockwood.edict_preferences", Context.MODE_PRIVATE)

        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.fragment_new_edict_deadline, container, false)
        vm = NewNewEdictVM(newEdict)
        vm.setSharedPreferences(sharedPreferences)
        binding.setVariable(BR.vm, vm)
        binding.lifecycleOwner = this
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateVM()

        setUpToolbar()

        setUpButtons()
    }

    private fun updateVM() {
        vm.updateNullCheckInEnd()
        vm.updateNullCheckInStart()
    }

    private fun setUpToolbar() {
        toolbarVM = ViewModelProviders.of(requireActivity()).get(ToolbarVM::class.java)
        toolbarVM.setCurrentLocation(ToolbarVM.AppLocation.NEW_EDICT_DEADLINE)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun setUpButtons() {

        start_time.setOnClickListener { clickTimePicker(it as TextView) }
        end_time.setOnClickListener { clickTimePicker(it as TextView) }

        continue_btn.setOnClickListener {
            val newEdict = vm.getNewEdict()
            navigateWithExtras(newEdict)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun clickTimePicker(text: TextView) {
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        val tpd = TimePickerDialog(requireContext(), TimePickerDialog.OnTimeSetListener(
            function = { _, h, m ->
                val minutes = h * 60 + m
                val timeString = TimeHelper.minutesToTimeString(minutes)
                text.text = timeString
            }),
            hour, minute, false)
        tpd.show()
    }

    private fun navigateWithExtras(newEdict: NewEdict) {
        hideKeyboard()
        val newExtras = newEdict.toBundle()
        newExtras.putSerializable("userState", userState)
        findNavController().navigate(R.id.action_newEdictDeadline_to_newEdictReminders, newExtras)
    }

    private fun hideKeyboard() {
        val imm: InputMethodManager = context!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view!!.windowToken, 0)
    }

}
