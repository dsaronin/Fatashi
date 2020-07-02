package org.umoja4life.fatashi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2

class VPShellFragment : Fragment() {

    private var viewPager: ViewPager2? = null
    private var detailPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            MainActivity.currentPosition = position
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        return viewPager
    }

}