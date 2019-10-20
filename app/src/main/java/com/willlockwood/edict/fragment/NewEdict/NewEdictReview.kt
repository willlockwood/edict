package com.willlockwood.edict.fragment.NewEdict


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.willlockwood.edict.BR
import com.willlockwood.edict.R
import com.willlockwood.edict.data.model.NewEdict
import com.willlockwood.edict.viewmodel.ToolbarVM
import com.willlockwood.edict.viewmodel.binding.NewNewEdictVM
import kotlinx.android.synthetic.main.fragment_new_edict_review.*

class NewEdictReview : Fragment() {

    private lateinit var toolbarVM: ToolbarVM
    private lateinit var binding: ViewDataBinding
    private lateinit var vm: NewNewEdictVM
    private lateinit var newEdict: NewEdict
    private lateinit var extras: Bundle
    private var userState: NewEdict.UserEditingOrCreating = NewEdict.UserEditingOrCreating.EDITING

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        extras = this.arguments!!
        newEdict = NewEdict().populateFromBundle(extras)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val sharedPreferences = activity!!.getSharedPreferences("com.willlockwood.edict_preferences", Context.MODE_PRIVATE)

        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.fragment_new_edict_review, container, false)
        vm = NewNewEdictVM(newEdict)
        vm.setSharedPreferences(sharedPreferences)
        binding.setVariable(BR.vm, vm)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpToolbar()

        setUpButtons()
    }

    private fun setUpButtons() {
        edict_layout.setOnClickListener {               navigateWithExtras(R.id.action_newEdictReview_to_newEdictType) }
        time_layout.setOnClickListener {                navigateWithExtras(R.id.action_newEdictReview_to_newEdictTime) }
        review_days_active_layout.setOnClickListener {  navigateWithExtras(R.id.action_newEdictReview_to_newEdictScope) }
        check_in_layout.setOnClickListener {            navigateWithExtras(R.id.action_newEdictReview_to_newEdictDeadline) }
        reminder_layout.setOnClickListener {            navigateWithExtras(R.id.action_newEdictReview_to_newEdictReminders) }
    }

    private fun setUpToolbar() {
        toolbarVM = ViewModelProviders.of(requireActivity()).get(ToolbarVM::class.java)
        toolbarVM.setCurrentLocation(ToolbarVM.AppLocation.NEW_EDICT_REVIEW)
    }

    private fun navigateWithExtras(destination: Int) {
        val newEdict = vm.getNewEdict()
        val newExtras = newEdict.toBundle()
        newExtras.putSerializable("userState", userState)
        findNavController().navigate(destination, newExtras)
    }


}
