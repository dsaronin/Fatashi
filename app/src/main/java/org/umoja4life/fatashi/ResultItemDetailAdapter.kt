package org.umoja4life.fatashi

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter


private const val DEBUG = false
private const val LOG_TAG = "ResultItemDetailAdapter"


class ResultItemDetailAdapter(activity: AppCompatActivity, val itemsCount: Int ) :
    FragmentStateAdapter(activity) {

    override fun getItemCount() = itemsCount

    override fun createFragment(position: Int) = DetailPagerFragment.getInstance(position)

}