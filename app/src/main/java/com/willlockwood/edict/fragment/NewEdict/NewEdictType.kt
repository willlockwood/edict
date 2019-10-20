package com.willlockwood.edict.fragment.NewEdict


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.willlockwood.edict.BR
import com.willlockwood.edict.R
import com.willlockwood.edict.data.model.NewEdict
import com.willlockwood.edict.viewmodel.ToolbarVM
import com.willlockwood.edict.viewmodel.binding.NewNewEdictVM

class NewEdictType : Fragment() {

    private lateinit var toolbarVM: ToolbarVM
    private lateinit var binding: ViewDataBinding
    private lateinit var vm: NewNewEdictVM
    private lateinit var extras: Bundle
    private lateinit var newEdict: NewEdict
    private lateinit var userState: NewEdict.UserEditingOrCreating

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        extras = this.arguments!!
        userState = extras.getSerializable("userState") as NewEdict.UserEditingOrCreating
        newEdict = NewEdict().populateFromBundle(extras)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.fragment_new_edict_type, container, false)
        vm = NewNewEdictVM(newEdict)
        binding.setVariable(BR.vm, vm)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateVM()

        setUpToolbar()

        observeType()
    }

    private fun updateVM() {
        if (userState == NewEdict.UserEditingOrCreating.EDITING) {
            vm.populateFieldsForFragment(NewNewEdictVM.NewEdictFragment.TYPE)
        }
    }

    private fun observeType() {
        vm.getLiveType().observe(this, Observer {
            if (it != null) {
                newEdict = vm.getNewEdict()
                newEdict.type = it
                navigateWithExtras(newEdict)
            }
        })
    }

    private fun setUpToolbar() {
        toolbarVM = ViewModelProviders.of(requireActivity()).get(ToolbarVM::class.java)
        toolbarVM.setCurrentLocation(ToolbarVM.AppLocation.NEW_EDICT_TYPE)
    }

    private fun navigateWithExtras(newEdict: NewEdict) {
        val newExtras = newEdict.toBundle()
        newExtras.putSerializable("userState", userState)
        findNavController().navigate(R.id.action_newEdictType_to_newEdictScale, newExtras)
    }


}
