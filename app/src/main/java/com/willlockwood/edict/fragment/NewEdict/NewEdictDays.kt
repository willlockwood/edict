package com.willlockwood.edict.fragment.NewEdict


import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.willlockwood.edict.BR
import com.willlockwood.edict.R
import com.willlockwood.edict.data.model.NewEdict
import com.willlockwood.edict.viewmodel.ToolbarVM
import com.willlockwood.edict.viewmodel.binding.NewNewEdictVM
import kotlinx.android.synthetic.main.fragment_new_edict_days.*

class NewEdictDays : Fragment() {

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
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.fragment_new_edict_days, container, false)
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
            vm.populateFieldsForFragment(NewNewEdictVM.NewEdictFragment.DAYS)
        }
    }

    private fun setUpToolbar() {
        val toolbar = (activity as AppCompatActivity).supportActionBar!!
        toolbar.setIcon(R.drawable.ic_day_black_24dp)
        when (newEdict.scope) {
            NewEdict.Scope.SOME_DAYS -> {
                toolbar.title = "On ..."
                toolbar.subtitle = "On ..."
            }
            NewEdict.Scope.VAR_DAYS -> {
                toolbar.title = "Every ... days"
                toolbar.subtitle = "Every ... days"
            }
        }

//        toolbarVM = ViewModelProviders.of(requireActivity()).get(ToolbarVM::class.java)
//        toolbarVM.setCurrentLocation(ToolbarVM.AppLocation.NEW_EDICT_DAYS)
    }

    private fun setUpButtons() {
        days_continue_btn.setOnClickListener {
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
            NewEdict.UserEditingOrCreating.CREATING -> findNavController().navigate(R.id.action_newEdictDays_to_newEdictType, newExtras)
        }
    }

    private fun hideKeyboard() {
        val imm: InputMethodManager = context!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view!!.windowToken, 0)
    }

}
