package com.willlockwood.edict.fragment.NewEdict


import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.willlockwood.edict.BR
import com.willlockwood.edict.R
import com.willlockwood.edict.data.model.NewEdict
import com.willlockwood.edict.fragment.NumberPickerDialog
import com.willlockwood.edict.viewmodel.ToolbarVM
import com.willlockwood.edict.viewmodel.binding.NewEdictNotificationsBVM
import com.willlockwood.edict.viewmodel.binding.NewNewEdictVM
import kotlinx.android.synthetic.main.fragment_new_edict_notifications.*

class NewEdictReminders : Fragment(), NumberPickerDialog.NumberPickerDialogListener {
    private lateinit var toolbarVM: ToolbarVM

    private lateinit var binding: ViewDataBinding
    private lateinit var vm: NewNewEdictVM
    private lateinit var viewModel: NewEdictNotificationsBVM
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

        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.fragment_new_edict_notifications, container, false)
        vm = NewNewEdictVM(newEdict)
        vm.setSharedPreferences(sharedPreferences)
        viewModel = NewEdictNotificationsBVM(newEdict)
        binding.setVariable(BR.vm, vm)
        binding.setVariable(BR.viewModel, viewModel)
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
    }

    private fun setUpToolbar() {
        (activity as AppCompatActivity).supportActionBar?.title = "Edict Reminders"
    }

    private fun setUpButtons() {
        check_in_end_time_layout.setOnClickListener {
            var dialog = NumberPickerDialog(13)
            dialog.setTargetFragment(this, 0)
            dialog.show(parentFragmentManager, "fragment_edit_number")
        }

        continue_btn.setOnClickListener {
            val edict = viewModel.getNewEdict()
            navigateWithExtras(edict)
        }
    }

    override fun setMinutes(value: Int) {
        viewModel.setCheckInEndNotificationVar(value)
    }

    private fun navigateWithExtras(newEdict: NewEdict) {
        val newExtras = newEdict.toBundle()
        newExtras.putSerializable("userState", userState)
        findNavController().navigate(R.id.action_global_newEdictReview, newExtras)
    }
}
