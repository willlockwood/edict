package com.willlockwood.edict.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.willlockwood.edict.data.model.EdictSession
import com.willlockwood.edict.fragment.CheckInPage

class CheckInEdictsPagerAdapter(fragmentManager: FragmentManager,
    private var edictSessions: List<EdictSession>
) : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private lateinit var edictSession: EdictSession
    private lateinit var fragment: CheckInPage

    override fun getPageTitle(position: Int): CharSequence? {
        return position.toString()
    }

    override fun getItem(position: Int): Fragment {
        this.fragment =  CheckInPage(edictSessions[position].edict, edictSessions[position], edictSessions, position)
        edictSession = edictSessions[position]
        return fragment
    }

    override fun getCount(): Int {
        return edictSessions.size
    }

    fun setEdictSessions(sessions: List<EdictSession>) {
        edictSessions = sessions
        notifyDataSetChanged()
    }
    fun getEdictSessions(): List<EdictSession> { return this.edictSessions }

}