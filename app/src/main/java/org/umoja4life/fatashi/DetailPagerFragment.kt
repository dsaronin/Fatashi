package org.umoja4life.fatashi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import kotlinx.android.synthetic.main.result_item_detail.*

private const val DEBUG = false
private const val LOG_TAG = "DetailPagerFragment"
const val ARG_POSITION = "org.umoja4life.fatashi.item_position"

class DetailPagerFragment : Fragment() {
    private var viewPager: ViewPager2? = null

    // class-level to getInstance, prep the state arguments & return the Fragment
    companion object {
        fun getInstance(position: Int): Fragment {
            val myDetailPagerFragment = DetailPagerFragment()
            val myBundle = Bundle()

            myBundle.putInt(ARG_POSITION, position)
            myDetailPagerFragment.arguments = myBundle

            return myDetailPagerFragment
        }
    }

    // onCreateView -- inflate the result_item_detail layout
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewPager = inflater.inflate(R.layout.result_detail_pager, container, false) as ViewPager2
        viewPager?.adapter = ResultItemDetailAdapter(activity as AppCompatActivity, ResultsContent.itemsCount() )
        viewPager?.currentItem = 0 // MainActivity.currentPosition

        /*
        viewPager?.addOnPageChangeListener( object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected( position: Int) {
                // MainActivity.currentPosition = position
            }
        })*/

        return viewPager

    }


    // bind result item fields to TextView fields in layout
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val position = requireArguments().getInt(ARG_POSITION)

        itemEntry.text = ResultsContent.RESULT_ITEMS[position].entry
        itemDefinition.text = ResultsContent.RESULT_ITEMS[position].definition
        itemUsage.text = ResultsContent.RESULT_ITEMS[position].usage
    }

}