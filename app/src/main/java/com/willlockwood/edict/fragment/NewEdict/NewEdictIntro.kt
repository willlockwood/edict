package com.willlockwood.edict.fragment.NewEdict

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.willlockwood.edict.R
import com.willlockwood.edict.data.model.NewEdict
import com.willlockwood.edict.viewmodel.ToolbarVM
import kotlinx.android.synthetic.main.fragment_new_edict_intro.*

class NewEdictIntro: Fragment() {

    private lateinit var binding: ViewDataBinding
    private lateinit var toolbarVM: ToolbarVM
    private var userState: NewEdict.UserEditingOrCreating = NewEdict.UserEditingOrCreating.CREATING

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.fragment_new_edict_intro, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpButtons()

        setUpToolbar()
    }

    private fun setUpButtons() {
        new_edict_intro_start.setOnClickListener {
            val extras = bundleOf("userState" to userState )
            findNavController().navigate(R.id.action_newEdictIntro_to_newEdictScope, extras)
        }
    }

    private fun setUpToolbar() {
        toolbarVM = ViewModelProviders.of(requireActivity()).get(ToolbarVM::class.java)
        toolbarVM.setCurrentLocation(ToolbarVM.AppLocation.NEW_EDICT_INTRO)
    }
}