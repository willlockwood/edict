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
import com.willlockwood.edict.viewmodel.ToolbarVM


class EdictFragment : Fragment() {

    private lateinit var binding: ViewDataBinding
    private lateinit var edictVM: EdictVM
    private lateinit var toolbarVM: ToolbarVM
    private lateinit var edict: Edict
    private var edictId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            edictId = it.getInt("edictId", -1)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.fragment_edict, container, false)
        val view = binding.root
        edict = Edict()
        binding.setVariable(BR.edict, edict)
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpViewModels()

        observeEdictById()
    }

    private fun setUpViewModels() {
        edictVM = ViewModelProviders.of(requireActivity()).get(EdictVM::class.java)
        toolbarVM = ViewModelProviders.of(requireActivity()).get(ToolbarVM::class.java)
    }

    private fun observeEdictById() {
        edictVM.getLiveEdictById(edictId).observe(viewLifecycleOwner, Observer {
            if (it != null) {
                this.edict = it
                binding.setVariable(BR.edict, it)
                binding.notifyPropertyChanged(BR.edict) // Replace the dummy edict
            }
        })
    }

}
