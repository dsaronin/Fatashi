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

    private val values: List<ResultItem>

) : RecyclerView.Adapter<KamusiItemRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
                    .from(parent.context)
                   .inflate(R.layout.fragment_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.col1View.text = values[position].content
    }

    override fun getItemCount(): Int = values.size

    //**********************************************************************************
    //**********************************************************************************
    //**********************************************************************************
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val col1View: TextView = view.findViewById(R.id.col1_content)

        // content is now in first column
        override fun toString(): String {
            return super.toString() + " '" + col1View.text + "'"
        }
    }
    //**********************************************************************************
    //**********************************************************************************
    // val col2View: TextView = view.findViewById(R.id.col2_content)
    //**********************************************************************************
}