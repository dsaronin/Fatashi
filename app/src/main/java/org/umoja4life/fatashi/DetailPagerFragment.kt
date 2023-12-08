package org.umoja4life.fatashi

import android.os.Bundle
import android.util.Log
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
            if (DEBUG) Log.d(LOG_TAG, ">>> getInstance <<< ---------- [${ResultsContent.RESULT_ITEMS.size}] ------ ($position) ------ $myDetailPagerFragment")

            return myDetailPagerFragment
        }
    }

    // replacing deprecated kotlin-android-extensions
    private var _binding: ResultProfileBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    // onCreateView -- inflate the result_item_detail layout
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (DEBUG) Log.d(LOG_TAG, ">>> onCreateView <<< ============ [${ResultsContent.RESULT_ITEMS.size}]===== $this ======== ")

        // added for deprecation
        _binding = ResultItemDetailBinding.inflate(inflater, container, false)
        val view = binding.root
        return view

        // deprecated: return inflater.inflate(R.layout.result_item_detail, container, false)
    }

    // TODO: saving/restoring current position information

    // bind result item fields to TextView fields in layout
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (DEBUG) Log.d(LOG_TAG, ">>> onViewCreated <<< ========================= [${ResultsContent.RESULT_ITEMS.size}] ========= $this")
        val position = requireArguments().getInt(ARG_POSITION)
        if (DEBUG) Log.d(LOG_TAG, ">>> onViewCreated <<< ============================= ($position) ===== $this")

        val myDataItem = ResultsContent.RESULT_ITEMS[position]
        with(view) {
            val itemViewText = findViewById<TextView>(R.id.itemEntry)
            itemViewText.text = myDataItem.entry.stripANSI()
            itemViewText.transitionName = "transition$position"
            findViewById<TextView>(R.id.itemDefinition).text =
                myDataItem.definition.stripANSI().replace(";\\s*".toRegex(),System.getProperty("line.separator")!!)
            findViewById<TextView>(R.id.itemUsage).text = myDataItem.usage.rewrapANSI()
        }
    }

    // added due to kotlin-android-extensions deprecation
    override fun onDestroyView() {
        super.onDestroyView()
        if (DEBUG) Log.d(LOG_TAG, ">>> onDestroyView <<< ========================= [${ResultsContent.RESULT_ITEMS.size}]===== $this")
        _binding = null
    }

    /*

        override fun onAttach(context: Context) {
            super.onAttach(context)
            if (DEBUG) Log.d(LOG_TAG, ">>> onAttach <<< ========================= [${ResultsContent.RESULT_ITEMS.size}]===== $this")
        }

        override fun onPause() {
            super.onPause()
            if (DEBUG) Log.d(LOG_TAG, ">>> onPause <<< ========================= [${ResultsContent.RESULT_ITEMS.size}]===== $this")
        }

        override fun onActivityCreated(savedInstanceState: Bundle?) {
            super.onActivityCreated(savedInstanceState)
            if (DEBUG) Log.d(LOG_TAG, ">>> onActivityCreated <<< ========================= [${ResultsContent.RESULT_ITEMS.size}]===== $this")
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            if (DEBUG) Log.d(LOG_TAG, ">>> onCreate <<< ========================= [${ResultsContent.RESULT_ITEMS.size}]===== $this")
        }

        override fun onStart() {
            super.onStart()
            if (DEBUG) Log.d(LOG_TAG, ">>> onStart <<< ========================= [${ResultsContent.RESULT_ITEMS.size}]===== $this")
        }

        override fun onResume() {
            super.onResume()
            if (DEBUG) Log.d(LOG_TAG, ">>> onResume <<< ========================= [${ResultsContent.RESULT_ITEMS.size}]===== $this")
        }

        override fun onDetach() {
            super.onDetach()
            if (DEBUG) Log.d(LOG_TAG, ">>> onDetach <<< ========================= [${ResultsContent.RESULT_ITEMS.size}]===== $this")
        }

        override fun onStop() {
            super.onStop()
            if (DEBUG) Log.d(LOG_TAG, ">>> onStop <<< ========================= [${ResultsContent.RESULT_ITEMS.size}]===== $this")
        }

        override fun onDestroy() {
            super.onDestroy()
            if (DEBUG) Log.d(LOG_TAG, ">>> onDestroy <<< ========================= [${ResultsContent.RESULT_ITEMS.size}]===== $this")
        }
    */
}