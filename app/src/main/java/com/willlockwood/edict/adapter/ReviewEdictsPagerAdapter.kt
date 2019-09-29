package com.willlockwood.edict.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.willlockwood.edict.data.model.EdictSession
import com.willlockwood.edict.fragment.ReviewPage

class ReviewEdictsPagerAdapter(fragmentManager: FragmentManager,
    private var edictSessions: List<EdictSession>
) : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getPageTitle(position: Int): CharSequence? {
        return position.toString()
    }

    override fun getItem(position: Int): Fragment {
        return ReviewPage(edictSessions[position].edict)
    }

    override fun getCount(): Int {
        return edictSessions.size
    }

    fun setEdictSessions(sessions: List<EdictSession>) {
        edictSessions = sessions
        notifyDataSetChanged()
    }
}