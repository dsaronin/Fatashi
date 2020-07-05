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

    private var detailPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
         // CALLBACK: onPageSelected, entered eachtime a page changes
        override fun onPageSelected(position: Int) {
            MainActivity.currentPosition = position  // update currentPosition
            if (nextOnLoadCompleted != null) nextOnLoadCompleted(lastFragment, targetPosition)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewPager = inflater.inflate(R.layout.result_detail_pager, container, false) as ViewPager2
        viewPager?.adapter = ResultItemDetailAdapter(activity as AppCompatActivity, ResultsContent.itemsCount() )
        viewPager?.currentItem = MainActivity.currentPosition
        viewPager?.orientation = ViewPager2.ORIENTATION_VERTICAL
        viewPager?.registerOnPageChangeCallback(( detailPageChangeCallback ))

        prepareSharedElementTransition()

        // Avoid a postponeEnterTransition on orientation change, and postpone only of first creation.
        if (savedInstanceState == null) postponeEnterTransition()

        return viewPager
    }

    /**
     * Prepares the shared element transition from and back to the grid fragment.
     */
    private fun prepareSharedElementTransition() {
        val transition = TransitionInflater.from(context)
            .inflateTransition(R.transition.list_shared_element_transition)
        sharedElementEnterTransition = transition

        // A similar mapping is set at the GridFragment with a setExitSharedElementCallback.
        setEnterSharedElementCallback(
            object : SharedElementCallback() {
                override fun onMapSharedElements(
                    names: List<String>, sharedElements: MutableMap<String, View>
                ) {
                    // Locate the image view at the primary fragment (the ImageFragment that is currently
                    // visible). To locate the fragment, call instantiateItem with the selection position.
                    // At this stage, the method will simply return the fragment at the position and will
                    // not create a new one.
                    val currentFragment = (viewPager?.adapter as PagerAdapter).instantiateItem(
                        viewPager!!, MainActivity.currentPosition) as Fragment
                    val view = currentFragment.view ?: return

                    // Map the first shared element name to the child ImageView.
                    sharedElements[names[0]] = view.findViewById(R.id.result_item_content_entry)
                }
            })
    }

    // ***********************************************************************************
    // ***********************************************************************************
    // CLASS_LEVEL: to control callback for Fragment transitioning from LIST to DETAIL views
    companion object {

        lateinit var nextOnLoadCompleted : (Fragment, Int) -> Unit
        lateinit var lastFragment : Fragment
        var targetPosition : Int = 0

        fun setNextOnLoadCompleted( prevFragment: Fragment, nextPosition: Int, anOnLoadCompletedFunction: (Fragment, Int) -> Unit ) {
            nextOnLoadCompleted = anOnLoadCompletedFunction
            lastFragment = prevFragment
            targetPosition = nextPosition
        }
    }  // companion object
} // class VPShellFragment