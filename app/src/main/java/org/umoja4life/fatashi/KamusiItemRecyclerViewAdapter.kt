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
    private val clickListener: (String) -> Unit

) : RecyclerView.Adapter<KamusiItemRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
                    .from(parent.context)
                   .inflate(R.layout.result_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind( resultList[position].content, clickListener )
    }

    override fun getItemCount(): Int = resultList.size

    //**********************************************************************************
    //**********************************************************************************
    //**********************************************************************************
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val resultItemView: TextView = itemView.findViewById(R.id.result_item_content)

        fun bind(resultItem: String, myListener: (String) -> Unit) = with(itemView) {
            resultItemView.text = resultItem
            setOnClickListener { myListener(resultItem) }
        }

        override fun toString(): String = super.toString() + " '" + resultItemView.text + "'"
    }
    //**********************************************************************************
    //**********************************************************************************
    //**********************************************************************************
}