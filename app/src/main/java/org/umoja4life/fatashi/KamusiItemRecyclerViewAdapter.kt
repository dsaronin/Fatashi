package org.umoja4life.fatashi

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KIViewHolder {
        // added for deprecation
        return KIViewHolder(
            ResultListItemBinding.inflate( LayoutInflater.from(parent.context), parent, false)
        )
        // deprecated:  val view = LayoutInflater.from(parent.context).inflate(R.layout.result_list_item, parent, false)
       // return KIViewHolder(view)
    }

    override fun onBindViewHolder(holder: KIViewHolder, position: Int) {
        holder.bind(resultList[position], clickListener)
    }

    override fun getItemCount(): Int = resultList.size

    //**********************************************************************************
    //**********************************************************************************
    //**********************************************************************************
    inner class KIViewHolder(binding: ResultListItemBinding ) : RecyclerView.ViewHolder(binding.root) {

        private val itemEntryView: TextView = binding.resultItemContentEntry
        private val itemDefinitionView: TextView = binding.resultItemContentDefinition
        private val itemUsageView: TextView = binding.resultItemContentUsage

        // deprecated items replaced (corresponding to above three assignments)
        // itemView.findViewById(R.id.result_item_content_entry)
        // itemView.findViewById(R.id.result_item_content_definition)
        // itemView.findViewById(R.id.result_item_content_usage)

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