package org.umoja4life.fatashi

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_item.view.*

class MyAdapter(val dataItems: List<String>, val clickListener: (String) -> Unit) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(parent.inflate(R.layout.view_item))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(dataItems[position], clickListener)

    override fun getItemCount() = dataItems.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.itemTitle

        fun bind(item: String, myListener: (String) -> Unit) = with(itemView) {
            title.text = item
            setOnClickListener { myListener(item) }
        }
    }
}

/***
 * MainActivity to process clicks or Fragment?
 *
    private fun searchItemClicked(searchItem : ResultItem) {
        Toast.makeText(this, "Clicked: $searchItem", Toast.LENGTH_LONG).show()
    }

    onCreateView()
    rv_parts.adapter = PartAdapter(testData, { searchItem : ResultItem -> searchItemClicked(searchItem) })
 */


/****
 * SNIPPETS
 val view = findViewById(R.id.welcomeMessage)
 view.setOnClickListener { v -> navigateWithView(v) }

 or redefined as a lambda:
    fun setOnClickListener(listener: (view: View) -> Unit){}
 */
