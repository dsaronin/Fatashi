package org.umoja4life.fatashi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

private const val DEBUG = false
private const val LOG_TAG = "DetailPagerFragment"
const val ARG_POSITION = "org.umoja4life.fatashi.item_position"

class DetailPagerFragment : Fragment() {

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
        return inflater.inflate(R.layout.result_item_detail, container, false)
    }

    // TODO: saving/restoring current position information

    // bind result item fields to TextView fields in layout
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val position = requireArguments().getInt(ARG_POSITION)
        val myDataItem = ResultsContent.RESULT_ITEMS[position]
        with(view) {
            val itemViewText = findViewById<TextView>(R.id.itemEntry)
            itemViewText.text = myDataItem.entry.stripANSI()
            itemViewText.transitionName = "transition$position"
            findViewById<TextView>(R.id.itemDefinition).text =
                myDataItem.definition.stripANSI().replace(";\\s*".toRegex(),System.getProperty("line.separator"))
            findViewById<TextView>(R.id.itemUsage).text = myDataItem.usage.rewrapANSI()
        }
    }

}