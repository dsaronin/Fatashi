package org.umoja4life.fatashi

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import org.umoja4life.fatashi.ResultsContent.ResultItem

private const val DEBUG = false
private const val LOG_TAG = "KamusiItemRecyclerView"

// KamusiItemRecyclerViewAdapter -- [RecyclerView.Adapter] that can display a [ResultItem]

class KamusiItemRecyclerViewAdapter(

    private val resultList: List<ResultItem>,
    private val clickListener: (ResultItem) -> Unit

) : RecyclerView.Adapter<KamusiItemRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
                    .from(parent.context)
                   .inflate(R.layout.result_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind( resultList[position], clickListener )
    }

    override fun getItemCount(): Int = resultList.size

    //**********************************************************************************
    //**********************************************************************************
    //**********************************************************************************
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val itemEntryView: TextView = itemView.findViewById(R.id.result_item_content_entry)
        private val itemDefinitionView: TextView = itemView.findViewById(R.id.result_item_content_definition)
        private val itemUsageView: TextView = itemView.findViewById(R.id.result_item_content_usage)

        // bind -- binds data to display view for an item

        fun bind(resultItem: ResultItem, myListener: (ResultItem) -> Unit) = with(itemView) {
            itemEntryView.text = resultItem.entry
            itemDefinitionView.text = resultItem.definition
            itemUsageView.text = resultItem.usage
            setOnClickListener { myListener( resultItem ) }
        }

        override fun toString(): String = super.toString() + " '" + itemEntryView.text + "'"
    }
    //**********************************************************************************
    //**********************************************************************************
    //**********************************************************************************
}