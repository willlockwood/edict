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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.willlockwood.edict.BR
import com.willlockwood.edict.R
import com.willlockwood.edict.adapter.EdictFragmentSessionAdapter
import com.willlockwood.edict.data.model.Edict
import com.willlockwood.edict.viewmodel.EdictFragmentVM
import com.willlockwood.edict.viewmodel.EdictVM
import com.willlockwood.edict.viewmodel.ToolbarVM
import kotlinx.android.synthetic.main.fragment_edict.*


class EdictFragment : Fragment() {

    private lateinit var binding: ViewDataBinding
    private lateinit var edictVM: EdictVM
    private lateinit var toolbarVM: ToolbarVM
    private lateinit var edict: Edict
    private lateinit var viewmodel: EdictFragmentVM
    private var edictId: Int = -1

    private lateinit var recyclerView: RecyclerView
    private lateinit var sessionAdapter: EdictFragmentSessionAdapter
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            edictId = it.getInt("edictId", -1)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.fragment_edict, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpViewModels()

        observeEdictById()

        setUpRecyclerView()

        setUpDeleteButton()
    }

    private fun setUpRecyclerView() {
        recyclerView = sessions_recycler
        sessionAdapter = EdictFragmentSessionAdapter(context!!)
        recyclerView.adapter = sessionAdapter
        layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
    }

    private fun setUpDeleteButton() {
        give_up_text.setOnClickListener {
            val edict = viewmodel.getEdict()
            edictVM.deleteEdict(edict)
            findNavController().navigate(R.id.action_edictFragment_to_homeFragment)
        }
    }

    private fun setUpViewModels() {
        edictVM = ViewModelProviders.of(requireActivity()).get(EdictVM::class.java)
        toolbarVM = ViewModelProviders.of(requireActivity()).get(ToolbarVM::class.java)
    }

    private fun observeEdictById() {
        edictVM.getLiveEdictById(edictId).observe(viewLifecycleOwner, Observer {
            if (it != null) {
                this.edict = it
                observeEdictSessions(it)
            }
        })

    }
    fun observeEdictSessions(edict: Edict) {
        edictVM.getEdictSessionsById(edict.id).observe(viewLifecycleOwner, Observer {
            viewmodel = EdictFragmentVM(edict, it)
            binding.setVariable(BR.viewmodel, viewmodel)
            sessionAdapter.setEdicts(it)
        })
    }

}
