package org.umoja4life.fatashi


import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

private const val DEBUG = false
private const val LOG_TAG = "KamusiItemFragment"

// KamusiItemFragment -- representing a list of Kamusi Items

class KamusiItemFragment : Fragment() {

    var myAdapter : KamusiItemRecyclerViewAdapter? = null
    private var recyclerView: RecyclerView? = null

    // onCreateView callback -- for when view is created
    // remember the RV.Adapter for later usage to refresh screen with new data
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        recyclerView = inflater.inflate(R.layout.search_result_list, container, false) as RecyclerView
        recyclerView?.layoutManager = LinearLayoutManager(context)
        myAdapter = KamusiItemRecyclerViewAdapter(
            this,
            ResultsContent.RESULT_ITEMS
        ) { searchItem : ResultsContent.ResultItem, itemEntryView: TextView -> searchItemClicked(searchItem,itemEntryView) }
        recyclerView?.adapter = myAdapter     // remember for later update usage

        return recyclerView
    }

    // >>>>> THIS IS THE KEY POINT TO ACCESS THE KAMUSI BACKEND SEARCH ENGINE <<<<<<<<
    // publicly accessible, specifically from MainActivity at onClick for search
    // updateFragmentResults  -- get a new search query, then update & refresh display

    fun updateFragmentResults( maulizo: List<String>, clearBuffer: Boolean ) {
        if (DEBUG) Log.d(LOG_TAG, ">>> updateFragmentResults <<< ${maulizo.size}, flag: $clearBuffer")

        // have ResultsContent get us some new items to display
        val index = ResultsContent.buildResultItems( maulizo, clearBuffer )

        // then tell the RV.Adapter that we need a refresh
        if (clearBuffer) {  // replaced previous dataset
            myAdapter?.notifyDataSetChanged()
        }
        else {  // updated existing dataset
            myAdapter?.notifyItemRangeInserted( index, maulizo.size )
        }
    }

    // Prepare shared element transition to ViewPager2Shellfragment
    private fun prepareTransitions(newFragment: Fragment, position: Int) {
            // set starting fragment transition
        sharedElementReturnTransition =
            TransitionInflater
                .from(context)
                .inflateTransition(R.transition.list_shared_element_transition)

        exitTransition =
            TransitionInflater
                .from(context)
                .inflateTransition(R.transition.list_exit_transition)


        // set destination Fragment transitions
        newFragment.sharedElementEnterTransition =
            TransitionInflater
                .from(context)
                .inflateTransition(R.transition.list_shared_element_transition)

        newFragment.enterTransition =
            TransitionInflater
                .from(context)
                .inflateTransition(R.transition.list_exit_transition)

        val bundle = Bundle()
        bundle.putInt("FATASHI_TARGET", position)
        newFragment.arguments = bundle

    } // fun

    // CALLBACK -- when list item is clicked
    private fun searchItemClicked(searchItem: ResultsContent.ResultItem, itemEntryView: TextView) {
        if (DEBUG) Toast.makeText(activity?.applicationContext, "$searchItem", Toast.LENGTH_SHORT).show()

        val vp2Fragment = VPShellFragment()
        prepareTransitions(vp2Fragment, searchItem.position)
        MainActivity.currentPosition = searchItem.position

        activity?.supportFragmentManager
            ?.beginTransaction()
            ?.setReorderingAllowed(true)
            ?.addSharedElement( itemEntryView, "transition$searchItem.position" )
            ?.replace(R.id.fragment_container, vp2Fragment, VPShellFragment::class.java.simpleName )
            ?.addToBackStack(null)
            ?.commit()
    }
}  // class KamusiItemFragment

