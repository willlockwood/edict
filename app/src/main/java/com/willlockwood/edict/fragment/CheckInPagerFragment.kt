package com.willlockwood.edict.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.willlockwood.edict.R
import com.willlockwood.edict.adapter.CheckInEdictsPagerAdapter
import com.willlockwood.edict.viewmodel.EdictVM
import com.willlockwood.edict.viewmodel.ToolbarVM
import kotlinx.android.synthetic.main.fragment_check_in_edicts.*

class CheckInPagerFragment : Fragment() {

    private lateinit var viewPager: ViewPager
    private lateinit var pagerAdapter: CheckInEdictsPagerAdapter
    private lateinit var edictVM: EdictVM
    private lateinit var toolbarVM: ToolbarVM

    companion object {
        var blah = "blah"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setUpViewModels()
        setUpToolbar()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_check_in_edicts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPager = check_in_view_pager

        pagerAdapter = CheckInEdictsPagerAdapter(childFragmentManager, emptyList())
        viewPager.adapter = pagerAdapter
        viewPager.currentItem = 0

        edictVM.getActiveEdictSessions().observe(viewLifecycleOwner, Observer {
            // TODO: this "getEdictSessions().isEmpty()" is here as a way to achieve an "observeOnce()" effect. find a more secure way to do this
            if (it != null && pagerAdapter.getEdictSessions().isEmpty()) {
                pagerAdapter.setEdictSessions(it)
                val progressBar = check_in_progress_bar
                progressBar.max = pagerAdapter.count

                viewPager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener {
                    override fun onPageSelected(position: Int) {
                        progressBar.progress = position + 1
                    }
                    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
                    override fun onPageScrollStateChanged(state: Int) {}
                })
            }
        })

//        setUpButtons()
    }

//    private fun enableButtons(enabled: Boolean) {
//        success_btn.isEnabled = enabled
//        failure_btn.isEnabled = enabled
//    }
//
//    private fun setUpButtons() {
//        success_btn.setOnClickListener {
////            pagerAdapter.resolveCurrentSession(true)
//        }
//        failure_btn.setOnClickListener {
////            pagerAdapter.resolveCurrentSession(false)
//        }
//    }

    private fun setUpViewModels() {
        edictVM = ViewModelProviders.of(requireActivity()).get(EdictVM::class.java)
        toolbarVM = ViewModelProviders.of(requireActivity()).get(ToolbarVM::class.java)
    }

    private fun setUpToolbar() {
        toolbarVM.setCurrentLocation(ToolbarVM.AppLocation.CHECK_IN_FRAGMENT)
    }
}
