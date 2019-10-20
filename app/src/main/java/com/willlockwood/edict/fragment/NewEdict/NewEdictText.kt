package com.willlockwood.edict.fragment.NewEdict


import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.willlockwood.edict.BR
import com.willlockwood.edict.R
import com.willlockwood.edict.data.model.NewEdict
import com.willlockwood.edict.viewmodel.binding.NewNewEdictVM
import com.willlockwood.edict.viewmodel.ToolbarVM
import kotlinx.android.synthetic.main.fragment_new_edict_text.*

class NewEdictText : Fragment() {

    private lateinit var toolbarVM: ToolbarVM
    private lateinit var binding: ViewDataBinding
    private lateinit var vm: NewNewEdictVM
    private lateinit var newEdict: NewEdict
    private lateinit var extras: Bundle
    private lateinit var userState: NewEdict.UserEditingOrCreating

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        extras = this.arguments!!
        userState = extras.getSerializable("userState") as NewEdict.UserEditingOrCreating
        newEdict = NewEdict().populateFromBundle(extras)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.fragment_new_edict_text, container, false)
        vm = NewNewEdictVM(newEdict)
        binding.setVariable(BR.vm, vm)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateVM()

        setUpToolbar()

        setUpButtons()
    }

    private fun updateVM() {
        if (userState == NewEdict.UserEditingOrCreating.EDITING) {
            vm.populateFieldsForFragment(NewNewEdictVM.NewEdictFragment.TEXT)
        }
    }

    private fun setUpToolbar() {
        toolbarVM = ViewModelProviders.of(requireActivity()).get(ToolbarVM::class.java)
        toolbarVM.setCurrentLocation(ToolbarVM.AppLocation.NEW_EDICT_TEXT)
    }

    private fun setUpButtons() {
        refresh_example_btn.setOnClickListener {
            vm.getNextRuleExample()
        }

        time_continue_btn.setOnClickListener {
            val newEdict = vm.getNewEdict()
            navigateWithExtras(newEdict)
            hideKeyboard()
        }
    }

    private fun navigateWithExtras(newEdict: NewEdict) {
        val newExtras = newEdict.toBundle()
        newExtras.putSerializable("userState", userState)
        when (userState) {
            NewEdict.UserEditingOrCreating.EDITING -> findNavController().navigate(R.id.action_global_newEdictReview, newExtras)
            NewEdict.UserEditingOrCreating.CREATING -> findNavController().navigate(R.id.action_newEdictText_to_newEdictTime, newExtras)
        }
    }

    private fun hideKeyboard() {
        val imm: InputMethodManager = context!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view!!.windowToken, 0)
    }
}
