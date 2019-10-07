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
import com.willlockwood.edict.BR
import com.willlockwood.edict.R
import com.willlockwood.edict.data.model.Edict
import com.willlockwood.edict.viewmodel.EdictVM

class ReviewPage(private val edictId: Int) : Fragment() {

    private lateinit var binding: ViewDataBinding
    private lateinit var edictVM: EdictVM

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_review_edict, container, false)
        val view = binding.root
        binding.setVariable(BR.edict, Edict()) // Use a dummy edict until the observation kicks in
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViewModels()
        observeEdictById(edictId)
    }

    private fun observeEdictById(edictId: Int) {
        edictVM.getLiveEdictById(edictId).observe(viewLifecycleOwner, Observer {
            binding.setVariable(BR.edict, it)
            binding.notifyPropertyChanged(BR.edict) // Replace the dummy edict
        })
    }

    private fun setUpViewModels() {
        edictVM = ViewModelProviders.of(this).get(EdictVM::class.java)
    }
}