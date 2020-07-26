package org.umoja4life.fatashi

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2


private const val DEBUG = true
private const val LOG_TAG = "VPShellFragment"

class VPShellFragment : Fragment() {

    private var viewPager: ViewPager2? = null
    private var targetPosition: Int = DEFAULT_POSITION

    private var detailPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
         // CALLBACK: onPageSelected, entered each time a page changes
        override fun onPageSelected(position: Int) {
            MainActivity.currentPosition = position  // update currentPosition
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    Log.d(LOG_TAG, ">>>>> Fragment BackPressed <<<<<" )

                    // remove all FragmentStateAdapter zombie fragments
                    if (childFragmentManager.fragments != null) {
                        for (fragment in childFragmentManager.fragments) {
                            childFragmentManager.beginTransaction()
                                .remove(fragment)
                                .commitAllowingStateLoss()
                        }
                    }

                    // if you want onBackPressed() to be called as normal afterwards
                    if (isEnabled) {
                        isEnabled = false
                        requireActivity().onBackPressed()
                    }
                }  // fun handleOnBackPressed
            }  // lambda
        )  // addCallBack
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

    /* might be necessary in future.... if zombie FragmentStateAdapter fragments remain
    override fun onDestroyView() {
        if (childFragmentManager.fragments != null) {
            for (fragment in childFragmentManager.fragments) {
                childFragmentManager.beginTransaction().remove(fragment)
                    .commitAllowingStateLoss()
            }
        }
        super.onDestroyView()
    }
    */

    // ***********************************************************************************
    // ***********************************************************************************
} // class VPShellFragment