package com.willlockwood.edict.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.willlockwood.edict.R
import com.willlockwood.edict.adapter.EdictSessionAdapter
import com.willlockwood.edict.viewmodel.EdictVM
import com.willlockwood.edict.viewmodel.ToolbarVM
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    private lateinit var edictVM: EdictVM
    private lateinit var toolbarVM: ToolbarVM

    private lateinit var recyclerView: RecyclerView
//    private lateinit var edictAdapter: EdictAdapter
    private lateinit var edictAdapter: EdictSessionAdapter
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpViewModels()

        setUpRecyclerView()

        observeEdictsForRecycler()

        setUpToolbar()

        setUpButtons()
    }


    private fun setUpViewModels() {
        edictVM = ViewModelProviders.of(requireActivity()).get(EdictVM::class.java)
        toolbarVM = ViewModelProviders.of(requireActivity()).get(ToolbarVM::class.java)
    }

    private fun setUpToolbar() {
        toolbarVM.setCurrentLocation(ToolbarVM.AppLocation.HOME_FRAGMENT)
    }

    private fun setUpButtons() {
        add_edict_button.setOnClickListener { findNavController().navigate(R.id.action_homeFragment_to_newEdictFragment) }
        review_button.setOnClickListener { findNavController().navigate(R.id.action_homeFragment_to_reviewPagerFragment) }
        check_in_button.setOnClickListener { findNavController().navigate(R.id.action_homeFragment_to_checkInPagerFragment) }
    }

    private fun setUpRecyclerView() {
        recyclerView = edict_recycler
        edictAdapter = EdictSessionAdapter(this.context!!, edictVM)
        recyclerView.adapter = edictAdapter
        layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
    }

    private fun observeEdictsForRecycler() {
        edictVM.getActiveEdictSessions().observe(viewLifecycleOwner, Observer {
            edictAdapter.setEdicts(it)
        })

//        edictVM.getAllEdicts().observe(viewLifecycleOwner, Observer {
//            edictAdapter.setEdicts(it)
//        })
    }

//    private fun setUpRecyclerView2() {
//        recyclerView = edict_recycler
//        edictAdapter = EdictAdapter(this.context!!)
//        recyclerView.adapter = edictAdapter
//        layoutManager = LinearLayoutManager(context)
//        recyclerView.layoutManager = layoutManager
//    }
}