package com.willlockwood.edict.fragment.NewEdict


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.willlockwood.edict.BR
import com.willlockwood.edict.R
import com.willlockwood.edict.data.model.NewEdict
import com.willlockwood.edict.viewmodel.ToolbarVM
import com.willlockwood.edict.viewmodel.binding.NewNewEdictVM


class NewEdictScope : Fragment() {

    private lateinit var binding: ViewDataBinding
    private lateinit var vm: NewNewEdictVM
    private lateinit var newEdict: NewEdict
    private lateinit var toolbarVM: ToolbarVM
    private lateinit var extras: Bundle
    private lateinit var userState: NewEdict.UserEditingOrCreating

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        extras = this.arguments!!
        userState = extras.get("userState") as NewEdict.UserEditingOrCreating
        newEdict = NewEdict().populateFromBundle(extras)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.fragment_new_edict_scope, container, false)
        vm = NewNewEdictVM(newEdict)
        binding.setVariable(BR.vm, vm)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateVM()

        setUpToolbar()

        observeScope()
    }

    private fun updateVM() {
        if (userState == NewEdict.UserEditingOrCreating.EDITING) {
            vm.populateFieldsForFragment(NewNewEdictVM.NewEdictFragment.SCOPE)
        }
    }

    private fun setUpToolbar() {
        val toolbar = (activity as AppCompatActivity).supportActionBar!!
        toolbar.setIcon(R.drawable.ic_day_black_24dp)
        toolbar.title = "Days in effect"
//        toolbarVM = ViewModelProviders.of(requireActivity()).get(ToolbarVM::class.java)
//        toolbarVM.setCurrentLocation(ToolbarVM.AppLocation.NEW_EDICT_SCOPE)
    }

    private fun observeScope() {
        vm.getLiveScope().observe(this, Observer {
            if (it != null) {
                newEdict = vm.getNewEdict()
                newEdict.scope = it
                when (newEdict.scope) {
                    NewEdict.Scope.DAILY -> {
                        newEdict.days = null
                        newEdict.daysText = null
                    }
                    NewEdict.Scope.WEEKLY -> {
                        newEdict.days = null
                        newEdict.daysText = null
                    }
                    else -> {}
                }
                navigateWithExtras(newEdict)
            }
        })
    }

    private fun navigateWithExtras(newEdict: NewEdict) {
        val newExtras = newEdict.toBundle()
        newExtras.putSerializable("userState", userState)
        when (newEdict.scope!!) {
            NewEdict.Scope.DAILY -> when (userState) {
                NewEdict.UserEditingOrCreating.EDITING -> findNavController().navigate(R.id.action_global_newEdictReview, newExtras)
                NewEdict.UserEditingOrCreating.CREATING -> findNavController().navigate(R.id.action_newEdictScope_to_newEdictType, newExtras)
            }
            NewEdict.Scope.WEEKLY -> when (userState) {
                NewEdict.UserEditingOrCreating.EDITING -> findNavController().navigate(R.id.action_global_newEdictReview, newExtras)
                NewEdict.UserEditingOrCreating.CREATING -> findNavController().navigate(R.id.action_newEdictScope_to_newEdictType, newExtras)
            }
            NewEdict.Scope.SOME_DAYS -> findNavController().navigate(R.id.action_newEdictScope_to_newEdictDays, newExtras)
            NewEdict.Scope.VAR_DAYS -> findNavController().navigate(R.id.action_newEdictScope_to_newEdictDays, newExtras)
        }
    }


}
