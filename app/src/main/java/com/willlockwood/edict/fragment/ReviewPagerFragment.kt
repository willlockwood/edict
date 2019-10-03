package com.willlockwood.edict.fragment


import android.animation.ArgbEvaluator
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.willlockwood.edict.R
import com.willlockwood.edict.adapter.ReviewEdictsPagerAdapter
import com.willlockwood.edict.viewmodel.EdictVM
import com.willlockwood.edict.viewmodel.ToolbarVM
import kotlinx.android.synthetic.main.fragment_review_edicts.*
import kotlin.math.pow

class ReviewPagerFragment : Fragment() {

    private lateinit var viewPager: ViewPager
    private lateinit var pagerAdapter: ReviewEdictsPagerAdapter
    private lateinit var edictVM: EdictVM
    private lateinit var toolbarVM: ToolbarVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setUpViewModels()
        setUpToolbar()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_review_edicts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpViewPager()
    }

    private fun setUpViewPager() {
        viewPager = review_edicts_view_pager
        pagerAdapter = ReviewEdictsPagerAdapter(childFragmentManager, emptyList())

        viewPager.setPageTransformer(true, ViewPagerCardTransformer())
        viewPager.offscreenPageLimit = 15
        viewPager.adapter = pagerAdapter
        viewPager.currentItem = 0

        edictVM.getActiveEdictSessions().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                pagerAdapter.setEdictSessions(it)
                val progressBar = review_progress_bar
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
    }

    private fun setUpViewModels() {
        edictVM = ViewModelProviders.of(requireActivity()).get(EdictVM::class.java)
        toolbarVM = ViewModelProviders.of(requireActivity()).get(ToolbarVM::class.java)
    }

    private fun setUpToolbar() {
        toolbarVM.setCurrentLocation(ToolbarVM.AppLocation.REVIEW_FRAGMENT)
    }

    class ViewPagerCardTransformer : ViewPager.PageTransformer {

        private fun addSeries(position: Float, x: Float, y: Float): Float {
            val part = (1 - x.pow(position)) / (1 - x)
            return part * y
        }

        @RequiresApi(Build.VERSION_CODES.M)
        override fun transformPage(page: View, position: Float) {

            val colorFactor = 0.8f
            val shadeOfWhite= ArgbEvaluator().evaluate(colorFactor.pow(position), page.context.getColor(R.color.colorPrimary), page.context.getColor(R.color.white)) as Int

            val distanceScale = 0.4f
            val totalDistanceHorizontal = page.width * distanceScale
            val totalDistanceVertical = page.height * distanceScale
            val scaleFactor = 0.65f
            val yHorizontal = totalDistanceHorizontal - totalDistanceHorizontal * scaleFactor
            val yVertical = totalDistanceVertical - totalDistanceVertical * scaleFactor
            val distanceFromLeft = addSeries(position, scaleFactor, yHorizontal)
            val distanceFromBottom = addSeries(position, scaleFactor, yVertical)

            if (position >= 0) {
                page.setBackgroundColor(shadeOfWhite)
                page.translationX = (-page.width * position) + distanceFromLeft
                page.translationY = -distanceFromBottom
                page.scaleX = scaleFactor.pow(position + 1)
                page.scaleY = scaleFactor.pow(position + 1)
            }
        }
    }


}
