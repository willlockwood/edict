package com.willlockwood.edict.fragment


import android.app.TimePickerDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.willlockwood.edict.BR
import com.willlockwood.edict.R
import com.willlockwood.edict.utils.EdictHelper
import com.willlockwood.edict.utils.TimeHelper
import com.willlockwood.edict.viewmodel.EdictVM
import com.willlockwood.edict.viewmodel.NewEdictVM
import com.willlockwood.edict.viewmodel.ToolbarVM
import kotlinx.android.synthetic.main.fragment_new_edict.*
import java.util.*


class NewEdictFragment : Fragment() {

    private lateinit var newEdictVM: NewEdictVM
    private lateinit var uploadVM: EdictVM
    private lateinit var toolbarVM: ToolbarVM

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        setUpViewModels()
        val binding: ViewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.fragment_new_edict, container, false)
        val view = binding.root
        binding.setVariable(BR.viewmodel, newEdictVM)
        return view
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpToolbar()

        setUpClickListeners()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun setUpClickListeners() {
        between_time_txt_1.setOnClickListener { clickTimePicker(it as TextView) }
        between_time_txt_2.setOnClickListener { clickTimePicker(it as TextView) }
        after_txt.setOnClickListener { clickTimePicker(it as TextView) }
        before_txt.setOnClickListener { clickTimePicker(it as TextView) }
        notify_at_text.setOnClickListener { clickTimePicker(it as TextView) }
        deadline_custom_time.setOnClickListener { clickTimePicker(it as TextView) }

        finalize_button.setOnClickListener {
            val edict = newEdictVM.getEdict()
            if (edict.getErrorStatus() == EdictHelper.ErrorStatus.READY) {
                newEdictVM.setCurrentlyFinalizing(true)
            } else {
                newEdictVM.setFinalizeButtonPressed(true)
            }
        }
        upload_button.setOnClickListener {
            val edict = newEdictVM.getEdict()
            uploadVM.insertEdictAndNewSession(edict)
            findNavController().navigate(R.id.action_newEdictFragment_to_homeFragment)
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
                text.text = TimeHelper.minutesToTimeString(minutes) }),
            hour, minute, false)
        tpd.show()
    }

    private fun setUpViewModels() {
        val sharedPref = activity!!.getSharedPreferences("com.willlockwood.edict_preferences", Context.MODE_PRIVATE)
        newEdictVM = NewEdictVM(sharedPref)
        uploadVM = ViewModelProviders.of(requireActivity()).get(EdictVM::class.java)
        toolbarVM = ViewModelProviders.of(requireActivity()).get(ToolbarVM::class.java)
    }

    private fun setUpToolbar() {
        toolbarVM.setToolbarTitle("Write a New Edict")
    }

}
