package org.umoja4life.fatashi

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import org.umoja4life.fatashi.ResultsContent.ResultItem
import org.umoja4life.fatashi.databinding.ResultListItemBinding

private const val DEBUG = false
private const val LOG_TAG = "KamusiItemRecyclerView"

// KamusiItemRecyclerViewAdapter -- [RecyclerView.Adapter] that can display a [ResultItem]

class KamusiItemRecyclerViewAdapter(

    private val fragment: KamusiItemFragment,
    private val resultList: List<ResultItem>,
    private val clickListener: (ResultItem, TextView) -> Unit

) : RecyclerView.Adapter<KamusiItemRecyclerViewAdapter.KIViewHolder>() {


    // replacing deprecated kotlin-android-extensions
    private var _binding: ResultListItemBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KIViewHolder {

        // added for deprecation
        _binding = ResultListItemBinding.inflate(inflater, container, false)
        val view = binding.root
        // deprecated:  val view = LayoutInflater.from(parent.context).inflate(R.layout.result_list_item, parent, false)

       return KIViewHolder(view)
    }

    override fun onBindViewHolder(holder: KIViewHolder, position: Int) {
        holder.bind(resultList[position], clickListener)
    }

    override fun getItemCount(): Int = resultList.size

    // added due to kotlin-android-extensions deprecation
    override fun onDestroyViewHolder() {
        super.onDestroyViewHolder()
        _binding = null
    }


    //**********************************************************************************
    //**********************************************************************************
    //**********************************************************************************
    inner class KIViewHolder(itemView: View ) : RecyclerView.ViewHolder(itemView) {

        private val itemEntryView: TextView =
            itemView.findViewById(R.id.result_item_content_entry)
        private val itemDefinitionView: TextView =
            itemView.findViewById(R.id.result_item_content_definition)
        private val itemUsageView: TextView =
            itemView.findViewById(R.id.result_item_content_usage)

        // bind -- binds data to display view for an item

        fun bind(resultItem: ResultItem, myListener: (ResultItem, TextView) -> Unit) =
            with(itemView) {

                // stripANSI from title divider lines
                itemEntryView.text = if (""">+""".toRegex().containsMatchIn(resultItem.entry) ) {
                    resultItem.entry.stripANSI()
                }
                else resultItem.entry.rewrapANSI()

                itemEntryView.transitionName = "transition$resultItem.position"
                itemDefinitionView.text = resultItem.definition.rewrapANSI()
                itemUsageView.text = resultItem.usage.rewrapANSI()
                setOnClickListener { myListener(resultItem, itemEntryView) }
            }

        override fun toString(): String = super.toString() + " '" + itemEntryView.text + "'"
    }  // class KIViewHolder
} // class KamusiItemRecyclerViewAdapter