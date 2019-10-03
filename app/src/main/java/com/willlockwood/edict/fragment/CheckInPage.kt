package com.willlockwood.edict.fragment

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
import androidx.viewpager.widget.ViewPager
import com.willlockwood.edict.BR
import com.willlockwood.edict.R
import com.willlockwood.edict.data.model.Edict
import com.willlockwood.edict.data.model.EdictSession
import com.willlockwood.edict.viewmodel.EdictVM
import kotlinx.android.synthetic.main.item_check_in_edict.*

class CheckInPage(private val edictId: Int, private val edictSession: EdictSession, private val edictSessions: List<EdictSession>, private val position: Int) : Fragment() {

    private lateinit var binding: ViewDataBinding
    private lateinit var edictVM: EdictVM
    private lateinit var edict: Edict
    private lateinit var viewPager: ViewPager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        viewPager = container as ViewPager

        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_check_in_edict, container, false)
        val view = binding.root
        edict = Edict()
        binding.setVariable(BR.edict, edict) // Use a dummy edict until the observation kicks in
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpViewModels()

        observeEdictById(edictId)

        setUpButtons()
    }

    private fun observeEdictById(edictId: Int) {
        edictVM.getLiveEdictById(edictId).observe(viewLifecycleOwner, Observer {
            if (it != null) {
                setEdict(it)
                binding.setVariable(BR.edict, it)
                binding.notifyPropertyChanged(BR.edict) // Replace the dummy edict
            }
        })
    }

    private fun setUpViewModels() {
        edictVM = ViewModelProviders.of(this).get(EdictVM::class.java)
    }

    private fun setUpButtons() {
        success_btn.setOnClickListener { resolveSession(edictSession, true) }
        failure_btn.setOnClickListener { resolveSession(edictSession, false) }
    }

    private fun resolveSession(edictSession: EdictSession, success: Boolean){
        val currentEdict = getEdict()
        edictVM.resolveEdictSession(edictSession, currentEdict, success)
        if (position != edictSessions.size - 1) {
            viewPager.setCurrentItem(position + 1, true)
        } else {
            findNavController().navigate(R.id.action_checkInEdicts_to_homeFragment)
        }
        disableButtons()
    }

    private fun disableButtons() {
        success_btn.isEnabled = false
        failure_btn.isEnabled = false
    }

    private fun getEdict(): Edict { return this.edict }
    private fun setEdict(edict: Edict) { this.edict = edict }

}