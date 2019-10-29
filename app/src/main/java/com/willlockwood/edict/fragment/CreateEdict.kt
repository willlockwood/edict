package com.willlockwood.edict.fragment


import android.annotation.TargetApi
import android.app.TimePickerDialog
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.willlockwood.edict.BR
import com.willlockwood.edict.R
import com.willlockwood.edict.activity.MainActivity
import com.willlockwood.edict.data.model.NewEdict
import com.willlockwood.edict.viewmodel.NewEdictNewVM
import com.willlockwood.edict.viewmodel.binding.CreateEdictBVM
import kotlinx.android.synthetic.main.fragment_create_edict.*
import java.util.*

class CreateEdict : Fragment(),
    CreateEdictActionDialog.ActionDialogListener,
    CreateEdictDaysDialog.DaysDialogListener,
    CreateEdictTimesDialog.TimesDialogListener,
    CreateEdictBVM.ViewListener
{

    private lateinit var binding: ViewDataBinding
    private lateinit var viewmodel: CreateEdictBVM
    private val newEdictVM: NewEdictNewVM by viewModels()
    private lateinit var newEdict: NewEdict
    private lateinit var extras: Bundle
    private lateinit var type: NewEdict.Type
    private lateinit var toolbar: ActionBar
    private var viewListener = this as CreateEdictBVM.ViewListener

    enum class DialogType { ACTION, DAYS, TIMES }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        extras = this.arguments!!
        type = extras.get("type") as NewEdict.Type
        newEdict = NewEdict(type = type, scalable = true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.fragment_create_edict, container, false)
        viewmodel = CreateEdictBVM(newEdict, viewListener)
        binding.setVariable(BR.viewmodel, viewmodel)
        binding.lifecycleOwner = this
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as MainActivity).doFabAction(MainActivity.FabAction.CLOSE_HIDE)

//        setUpViewModels()

        setUpToolbar()

        setUpClickListeners()

        setUpSpinners()
    }

    override fun onResume() {
        setUpToolbar()
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        when (type) {
            NewEdict.Type.RESTRICTION -> (requireActivity() as MainActivity).setStatusBarColor(R.color.restrictionColorDark)
            NewEdict.Type.ROUTINE -> (requireActivity() as MainActivity).setStatusBarColor(R.color.routineColorDark)
        }
        super.onResume()
    }

    override fun onPause() {
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR

        (requireActivity() as MainActivity).setStatusBarColor(R.color.colorPrimaryDark)
        super.onPause()
    }

//    private fun setUpViewModels() {
//        newEdictVM= ViewModelProviders.of(this).get(NewEdictNewVM::class.java)
//    }

    @TargetApi(Build.VERSION_CODES.N)
    private fun setUpClickListeners() {
        time_time_start.setOnClickListener          { clickTimePicker("start") }
        time_time_end.setOnClickListener            { clickTimePicker("end") }
        check_in_start_layout.setOnClickListener    { clickTimePicker("check_in_start") }
        check_in_end_layout.setOnClickListener      { clickTimePicker("check_in_end") }
        notify_at_txt2.setOnClickListener           { clickTimePicker("notify_at") }
    }

    private fun setUpSpinners() {
        notify_before_edict_end_spinner.adapter = ArrayAdapter(
            requireActivity(),
            R.layout.support_simple_spinner_dropdown_item,
            resources.getStringArray(R.array.deadline_before_entries_array)
        )
        notify_before_edict_end_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                viewmodel.setNotifyBeforeEnd(p2)
            }
        }
        notify_before_check_in_end_spinner.adapter = ArrayAdapter(
            requireActivity(),
            R.layout.support_simple_spinner_dropdown_item,
            resources.getStringArray(R.array.deadline_before_entries_array)
        )
        notify_before_check_in_end_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                viewmodel.setNotifyBeforeCheckInEnd(p2)
            }
        }
    }

    override fun openDialog(type: DialogType, scalable: Boolean?) {
        val edict = viewmodel.getNewEdict()
        val dialog = when (type) {
            DialogType.ACTION -> CreateEdictActionDialog(edict, scalable)
            DialogType.DAYS -> CreateEdictDaysDialog(edict)
            DialogType.TIMES -> CreateEdictTimesDialog(edict)
        }
        dialog.setTargetFragment(this as Fragment, type.ordinal)
        dialog.show(parentFragmentManager, type.name)
    }

    private fun setUpToolbar() {
        toolbar = (requireActivity() as AppCompatActivity).supportActionBar!!
        if (toolbar.isShowing) {
            toolbar.hide()
        }

        when (type) {
            NewEdict.Type.RESTRICTION -> (requireActivity() as MainActivity).setStatusBarColor(R.color.restrictionColorDark)
            NewEdict.Type.ROUTINE -> (requireActivity() as MainActivity).setStatusBarColor(R.color.routineColorDark)
        }

        toolbarFab.setOnClickListener {
            viewmodel.setDoneFabClicked()
            if (viewmodel.getDoneFabClicked()) {
                Toast.makeText(requireContext(), "done fab clicked", Toast.LENGTH_LONG).show()
                if (!(viewmodel.getDaysSubheaderError() || viewmodel.getActionSubheaderError() || viewmodel.getTimesSubheaderError())) {
                    val newEdict = viewmodel.getNewEdict()
                    newEdictVM.insertNewEdict(newEdict)
                    findNavController().navigate(R.id.action_createEdict_to_homeFragment)
//                    val fm = parentFragmentManager
//                    val trans = fm.beginTransaction()
//                    trans.remove(this)
//                    trans.commit()
//                    fm.popBackStack()
//                    (requireActivity() as MainActivity).supportActionBar!!.show()
                }
            }
        }
    }

    override fun setTimesType(type: NewEdict.TimeType) {
        viewmodel.setTimeType(type)
        if (type.name.toLowerCase().contains("text") || type == NewEdict.TimeType.WHEN || type == NewEdict.TimeType.WHILE || type == NewEdict.TimeType.AT) {
            (requireActivity() as MainActivity).toggleKeyboard(time_text_et)
        }
    }

    override fun setSwitchChecked(checked: Boolean) { scalable_switch.isChecked = checked }

    override fun setScalable(scalable: Boolean) { viewmodel.setScalable(scalable) }

    override fun setAction(action: String, unitVar: Int?, unit: String?) { viewmodel.setAction(action, unitVar, unit) }

    override fun setDaysType(scope: NewEdict.Scope) {
        viewmodel.setScope(scope)
        when (scope) {
            NewEdict.Scope.TEXT_DAYS -> { (requireActivity() as MainActivity).toggleKeyboard(days_on_txt) }
            NewEdict.Scope.VAR_DAYS -> { (requireActivity() as MainActivity).toggleKeyboard(var_days_et) }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun clickTimePicker(startOrEnd: String) {
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        val pickerStyle = when (type) {
            NewEdict.Type.RESTRICTION -> R.style.TimePicker_RED
            NewEdict.Type.ROUTINE -> R.style.TimePicker_Green
        }

        val tpd = TimePickerDialog(
            ContextThemeWrapper(requireActivity(), pickerStyle),
//            requireContext(),
            TimePickerDialog.OnTimeSetListener(
            function = { _, h, m ->
                val minutes = h * 60 + m
                when (startOrEnd) {
                    "start" -> viewmodel.setTimeStart(minutes)
                    "end" -> viewmodel.setTimeEnd(minutes)
                    "check_in_start" -> viewmodel.setCheckInStart(minutes)
                    "check_in_end" -> viewmodel.setCheckInEnd(minutes)
                    "notify_at" -> viewmodel.setNotifyAt(minutes)
                }
            }),
            hour, minute, false)
        tpd.show()
    }
}
