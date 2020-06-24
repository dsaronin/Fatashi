package org.umoja4life.fatashi

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.umoja4life.fatashi.dummy.ResultsContent

private const val DEBUG = true
private const val LOG_TAG = "KamusiItemFragment"

// KamusiItemFragment representing a list of Kamusi Items

class KamusiItemFragment : Fragment() {

    var myAdapter : KamusiItemRecyclerViewAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_item_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = LinearLayoutManager(context)
                myAdapter = KamusiItemRecyclerViewAdapter( ResultsContent.RESULT_ITEMS )
                adapter = myAdapter     // remember for later update usage
            }
        }
        return view
    }

    fun updateFragmentResults( maulizo: String ) {
        if (DEBUG) Log.d(LOG_TAG, ">>> updateFragmentResults <<< ${myAdapter != null}: $maulizo");

        ResultsContent.newQuery( maulizo )
        myAdapter?.notifyDataSetChanged( )

        // ResultsContent.shuffleList()    // shuffle list to show changed display
    }

    companion object {

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            KamusiItemFragment().apply {
                // arguments = Bundle().apply {
                //    putInt(ARG_COLUMN_COUNT, columnCount)
                // }
            }
    }
}