package com.willlockwood.edict.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.willlockwood.edict.R
import com.willlockwood.edict.activity.MainActivity
import com.willlockwood.edict.adapter.EdictsPagerAdapter
import com.willlockwood.edict.viewmodel.EdictVM
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    private val edictVM: EdictVM by viewModels()

    private lateinit var viewPager: ViewPager
    private lateinit var pagerAdapter: EdictsPagerAdapter
    private lateinit var tabLayout: TabLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpToolbar()

        setUpPager()
    }

    private fun setUpToolbar() {
        (requireActivity() as AppCompatActivity).supportActionBar!!.show()
        (requireActivity() as MainActivity).doFabAction(MainActivity.FabAction.UNHIDE)
    }

    private fun setUpPager() {
        viewPager = edicts_view_pager
        tabLayout = tabs

        pagerAdapter = EdictsPagerAdapter(childFragmentManager)
        viewPager.adapter = pagerAdapter
        viewPager.currentItem = 1

        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab) {}
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }
        })
    }
}