package org.umoja4life.fatashi

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.SharedElementCallback
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import 	androidx.viewpager.widget.PagerAdapter


class VPShellFragment : Fragment() {

    private var viewPager: ViewPager2? = null
    private var targetPosition: Int = DEFAULT_POSITION

    private var detailPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
         // CALLBACK: onPageSelected, entered each time a page changes
        override fun onPageSelected(position: Int) {
            MainActivity.currentPosition = position  // update currentPosition
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewPager = inflater.inflate(R.layout.result_detail_pager, container, false) as ViewPager2
        viewPager?.adapter = ResultItemDetailAdapter(activity as AppCompatActivity, ResultsContent.itemsCount() )
          // turn off smooth scroll to jump to desired page
        viewPager?.setCurrentItem(MainActivity.currentPosition, false)
        viewPager?.orientation = ViewPager2.ORIENTATION_VERTICAL
        viewPager?.registerOnPageChangeCallback(( detailPageChangeCallback ))

        targetPosition = arguments?.getInt("FATASHI_TARGET") ?: DEFAULT_POSITION

        return viewPager
    }

    // ***********************************************************************************
    // ***********************************************************************************
} // class VPShellFragment