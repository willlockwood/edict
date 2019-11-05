package com.willlockwood.edict.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.willlockwood.edict.R
import com.willlockwood.edict.adapter.SessionAdapter
import com.willlockwood.edict.viewmodel.SessionsVM
import kotlinx.android.synthetic.main.fragment_sessions_page.*

class SessionsPage : Fragment() {

    private val sessionsVM: SessionsVM by viewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var sessionAdapter: SessionAdapter
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sessions_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()

        observeSessions()
    }

    private fun setUpRecyclerView() {
        recyclerView = session_recycler
        sessionAdapter = SessionAdapter(this.context!!, sessionsVM)
        recyclerView.adapter = sessionAdapter
        layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
    }

    private fun observeSessions() {
        sessionsVM.getAllSessions().observe(viewLifecycleOwner, Observer {
            sessionAdapter.setEdicts(it)
        })
    }
}