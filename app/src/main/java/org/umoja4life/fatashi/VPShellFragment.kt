package org.umoja4life.fatashi

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.viewpager2.widget.ViewPager2

private const val DEBUG = false
private const val LOG_TAG = "VPShellFragment"

class VPShellFragment : Fragment(), LifecycleOwner {

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
                    if (DEBUG) Log.d(LOG_TAG, ">>>>> Fragment BackPressed <<<<<" )

                    // remove all FragmentStateAdapter zombie fragments
                    for (fragment in childFragmentManager.fragments) {
                        childFragmentManager.beginTransaction()
                            .remove(fragment)
                            .commitAllowingStateLoss()
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

    // activity as AppCompatActivity,

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewPager = inflater.inflate(R.layout.result_detail_pager, container, false) as ViewPager2
        viewPager?.adapter = ResultItemDetailAdapter(
            childFragmentManager,
            lifecycle,
            ResultsContent.itemsCount()
        )

        if (DEBUG) Log.d(LOG_TAG, ">>>>> onCreateView <<<<<  +++++++++++++ ${viewPager?.adapter}  ++++ $this" )

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