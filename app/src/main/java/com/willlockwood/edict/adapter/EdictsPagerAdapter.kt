package com.willlockwood.edict.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.willlockwood.edict.fragment.AllEdictsPage
import com.willlockwood.edict.fragment.SessionsPage
import com.willlockwood.edict.fragment.TodaysEdictSessionsPage

class EdictsPagerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getPageTitle(position: Int): CharSequence? {
        return position.toString()
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> SessionsPage()
            1 -> AllEdictsPage()
            else -> TodaysEdictSessionsPage()
        }
    }

    override fun getCount(): Int {
        return 2
    }
}