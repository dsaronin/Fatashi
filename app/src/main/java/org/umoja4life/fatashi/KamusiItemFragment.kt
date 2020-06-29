package org.umoja4life.fatashi

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.*

private const val DEBUG = false
private const val LOG_TAG = "KamusiItemFragment"

// KamusiItemFragment -- representing a list of Kamusi Items

class KamusiItemFragment : Fragment() {

    var myAdapter : KamusiItemRecyclerViewAdapter? = null

    // onCreate callback -- for when fragment is first created
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    // onCreateView callback -- for when view is created
    // tasks are to remember the RV.Adapter for later usage to refresh screen with new data
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_item_list, container, false)

        // Set the adapter for only a LinearLayout (not Grid) Manager
        // establish the KamusiItem RV.Adapter (handles scrolling)
        if (view is RecyclerView) {
            with(view) {
                layoutManager = LinearLayoutManager(context)
                myAdapter = KamusiItemRecyclerViewAdapter(
                    ResultsContent.RESULT_ITEMS
                ) { searchItem : String -> searchItemClicked(searchItem) }
                adapter = myAdapter     // remember for later update usage
            }
        }
        return view
    }

    // updateFragmentResults  -- get a new search query, then update & refresh display
    // >>>>> THIS IS THE KEY POINT TO ACCESS THE KAMUSI BACKEND SEARCH ENGINE <<<<<<<<
    // publically accessible, specifically from MainActivity at onClick for search

    fun updateFragmentResults( maulizo: String ) {
        if (DEBUG) Log.d(LOG_TAG, ">>> updateFragmentResults <<< ${myAdapter != null}: $maulizo")

        ResultsContent.newQuery( maulizo )  // have ResultsContent get us some new items to display
        myAdapter?.notifyDataSetChanged( )  // then tell the RV.Adapter that we need a refresh

    }

    private fun searchItemClicked(searchItem : String) {
        Toast.makeText(getActivity()?.getApplicationContext(), "Clicked: $searchItem", Toast.LENGTH_LONG).show()
    }


    //**********************************************************************************
    //**********************************************************************************
    //**********************************************************************************
/********
    companion object {

        // Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            KamusiItemFragment().apply {
                // arguments = Bundle().apply {
                //    putInt(ARG_COLUMN_COUNT, columnCount)
                // }
            }
    }
********/
    //**********************************************************************************
    //**********************************************************************************
    //**********************************************************************************

}