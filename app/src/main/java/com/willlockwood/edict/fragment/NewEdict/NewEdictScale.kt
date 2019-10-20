package com.willlockwood.edict.fragment.NewEdict


import android.os.Bundle
import android.os.CountDownTimer
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

class NewEdictScale : Fragment() {

    private lateinit var toolbarVM: ToolbarVM
    private lateinit var binding: ViewDataBinding
    private lateinit var vm: NewNewEdictVM
    private lateinit var newEdict: NewEdict
    private lateinit var extras: Bundle
    private lateinit var userState: NewEdict.UserEditingOrCreating
    private lateinit var countDownTimer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        extras = this.arguments!!
        userState = extras.getSerializable("userState") as NewEdict.UserEditingOrCreating
        newEdict = NewEdict().populateFromBundle(extras)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.fragment_new_edict_scale, container, false)
        vm = NewNewEdictVM(newEdict)
        binding.setVariable(BR.vm, vm)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateVM()

        setUpToolbar()

        setUpChangingText()

        observeScope()
    }

    private fun updateVM() {
        if (userState == NewEdict.UserEditingOrCreating.EDITING) {
            vm.populateFieldsForFragment(NewNewEdictVM.NewEdictFragment.SCALE)
        }
    }

    private fun setUpToolbar() {
        (activity as AppCompatActivity).supportActionBar?.title = "Scale"
//        toolbarVM = ViewModelProviders.of(requireActivity()).get(ToolbarVM::class.java)
//        toolbarVM.setCurrentLocation(ToolbarVM.AppLocation.NEW_EDICT_SCALE)
    }

    private fun setUpChangingText() {
        var numTimes = 1
        countDownTimer = object : CountDownTimer(60000 * 15, 1000.toLong()) {
            override fun onFinish() {}
            override fun onTick(p0: Long) {
                numTimes += 1
                vm.setScalableNum(numTimes % 5)
            }
        }
        countDownTimer.start()
    }

    private fun observeScope() {
        vm.getLiveScalable().observe(this, Observer {
            if (it != null) {
                newEdict = vm.getNewEdict()
                newEdict.scalable = it
                navigateWithExtras(newEdict)
            }
        })
    }

    private fun navigateWithExtras(newEdict: NewEdict) {
        val newExtras = newEdict.toBundle()
        newExtras.putSerializable("userState", userState)
        countDownTimer.cancel()
        findNavController().navigate(R.id.action_newEdictScale_to_newEdictText, newExtras)
    }
}
