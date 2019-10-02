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
import kotlinx.android.synthetic.main.fragment_all_edicts.*

class TodaysEdictSessionsPage : Fragment() {

    private lateinit var edictVM: EdictVM

    private lateinit var recyclerView: RecyclerView
    private lateinit var edictAdapter: EdictSessionAdapter
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_edicts_today_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpViewModels()

        setUpRecyclerView()

        observeEdictsForRecycler()

        setUpButtons()
    }

    private fun setUpViewModels() {
        edictVM = ViewModelProviders.of(this).get(EdictVM::class.java)
    }

    private fun setUpButtons() {
        fab.setOnClickListener { findNavController().navigate(R.id.action_homeFragment_to_checkInPagerFragment) }
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
    }
}