package org.umoja4life.fatashi

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

private const val DEBUG = false
private const val LOG_TAG = "ResultItemDetailAdapter"

// activity: AppCompatActivity,
class ResultItemDetailAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle, val itemsCount: Int ) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount() = itemsCount

    override fun createFragment(position: Int) : Fragment {
        if (DEBUG) Log.d(LOG_TAG, ">>>>> createFragment <<<<<   ######## $this" )
        return DetailPagerFragment.getInstance(position)
    }

}