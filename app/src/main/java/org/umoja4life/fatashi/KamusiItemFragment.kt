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
import android.transition.TransitionInflater
import android.view.View.OnLayoutChangeListener
import android.widget.TextView
import androidx.core.app.SharedElementCallback

private const val DEBUG = false
private const val LOG_TAG = "KamusiItemFragment"

// KamusiItemFragment -- representing a list of Kamusi Items

class KamusiItemFragment : Fragment() {

    var myAdapter : KamusiItemRecyclerViewAdapter? = null
    private var recyclerView: RecyclerView? = null

    // onCreateView callback -- for when view is created
    // tasks are to remember the RV.Adapter for later usage to refresh screen with new data
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
        prepareTransitions()
        postponeEnterTransition()

        return recyclerView
    }

    // updateFragmentResults  -- get a new search query, then update & refresh display
    // >>>>> THIS IS THE KEY POINT TO ACCESS THE KAMUSI BACKEND SEARCH ENGINE <<<<<<<<
    // publicly accessible, specifically from MainActivity at onClick for search

    fun updateFragmentResults( maulizo: String ) {
        if (DEBUG) Log.d(LOG_TAG, ">>> updateFragmentResults <<< ${myAdapter != null}: $maulizo")

        ResultsContent.newQuery( maulizo )  // have ResultsContent get us some new items to display
        myAdapter?.notifyDataSetChanged( )  // then tell the RV.Adapter that we need a refresh

    }

    private fun searchItemClicked(searchItem: ResultsContent.ResultItem, itemEntryView: TextView) {
        Toast.makeText(activity?.applicationContext, "$searchItem", Toast.LENGTH_SHORT).show()

        MainActivity.currentPosition = searchItem.position
        val transitioningView = itemEntryView

        getActivity()?.supportFragmentManager
            ?.beginTransaction()
            ?.setReorderingAllowed(true)
            ?.addSharedElement( transitioningView, transitioningView.transitionName )
            ?.replace(R.id.fragment_container, VPShellFragment(), VPShellFragment::class.java.simpleName )
            ?.addToBackStack(null)
            ?.commit()
    }

    // Scroll recyclerview to show clicked item from list; Important when navigating from the grid.

    private fun scrollToPosition() {

        recyclerView?.addOnLayoutChangeListener(object : OnLayoutChangeListener {

            override fun onLayoutChange(
                v: View, left: Int, top: Int, right: Int, bottom: Int,
                oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int
            ) {
                recyclerView?.removeOnLayoutChangeListener(this)

                val layoutManager = recyclerView?.layoutManager
                val viewAtPosition = layoutManager?.findViewByPosition(MainActivity.currentPosition)

                // Scroll to position if the view for the current position is null (not currently part of
                // layout manager children), or it's not completely visible.
                if (viewAtPosition == null ||
                    layoutManager.isViewPartiallyVisible(
                        viewAtPosition, false, true
                    )) {
                    recyclerView?.post { layoutManager?.scrollToPosition(MainActivity.currentPosition) }
                }
            }
        })
    } // fun

    // Prepare shared element transition to ViewPager2Shellfragment
    private fun prepareTransitions() {
        exitTransition = TransitionInflater.from(context)
            .inflateTransition(R.transition.list_exit_transition)


        // A similar mapping is set at the ImagePagerFragment with a setEnterSharedElementCallback.
        setExitSharedElementCallback(
            object : SharedElementCallback() {
                override fun onMapSharedElements(names: List<String>, sharedElements: MutableMap<String, View>) {
                    // Locate the KIViewHolder for the clicked position.
                    val selectedViewHolder =
                        recyclerView?.findViewHolderForAdapterPosition(MainActivity.currentPosition)
                            ?: return

                    // Map the first shared element name to the child ImageView.
                    sharedElements[names[0]] = selectedViewHolder.itemView.findViewById(R.id.itemEntry)
                }
            })
    } // fun
}  // class KamusiItemFragment