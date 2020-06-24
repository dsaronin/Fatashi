package org.umoja4life.fatashi

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import org.umoja4life.fatashi.dummy.ResultsContent.ResultItem

private const val DEBUG = true
private const val LOG_TAG = "KamusiItemRecyclerView"

/**
 * [RecyclerView.Adapter] that can display a [ResultItem]
 */
class KamusiItemRecyclerViewAdapter(
    private val values: List<ResultItem>
) : RecyclerView.Adapter<KamusiItemRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                   .inflate(R.layout.fragment_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]

        holder.col1View.text = item.content

        // holder.col1View.text = item.id
        // holder.col2View.text = item.content
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val col1View: TextView = view.findViewById(R.id.col1_content)
        val col2View: TextView = view.findViewById(R.id.col2_content)

        // content is now in first column
        override fun toString(): String {
            return super.toString() + " '" + col1View.text + "'"
        }
    }
}