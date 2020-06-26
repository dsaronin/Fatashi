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
                   .inflate(R.layout.fragment_item, parent, false)
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

        val col1View: TextView = itemView.findViewById(R.id.col1_content)

        fun bind(resultItem: String, myListener: (String) -> Unit) = with(itemView) {
            col1View.text = resultItem
            setOnClickListener { myListener(resultItem) }
        }

        override fun toString(): String = super.toString() + " '" + col1View.text + "'"
    }
    //**********************************************************************************
    //**********************************************************************************
    // val col2View: TextView = view.findViewById(R.id.col2_content)
    //**********************************************************************************
}