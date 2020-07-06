package org.umoja4life.fatashi

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import org.umoja4life.fatashi.ResultsContent.ResultItem
import java.util.concurrent.atomic.AtomicBoolean

private const val DEBUG = false
private const val LOG_TAG = "KamusiItemRecyclerView"

// KamusiItemRecyclerViewAdapter -- [RecyclerView.Adapter] that can display a [ResultItem]

class KamusiItemRecyclerViewAdapter(

    private val fragment: KamusiItemFragment,
    private val resultList: List<ResultItem>,
    private val clickListener: (ResultItem) -> Unit

) : RecyclerView.Adapter<KamusiItemRecyclerViewAdapter.ViewHolder>() {

        // used as semaphore to control starting a view transition
    private val enterTransitionStarted = AtomicBoolean( false )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
                    .from(parent.context)
                   .inflate(R.layout.result_list_item, parent, false)
        val myHolder = ViewHolder(view)

            // setup a callback to check item load completion after clicked
        myHolder.onLoadCompleted = {fragment, position ->
            if (MainActivity.currentPosition == position  &&
                !enterTransitionStarted.getAndSet(true)) {
                fragment.startPostponedEnterTransition()
            }
        }
        return myHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(resultList[position], clickListener)
    }

    override fun getItemCount(): Int = resultList.size

    //**********************************************************************************
    //**********************************************************************************
    //**********************************************************************************
    inner class ViewHolder( itemView: View ) : RecyclerView.ViewHolder(itemView) {

        private val itemEntryView: TextView =
            itemView.findViewById(R.id.result_item_content_entry)
        private val itemDefinitionView: TextView =
            itemView.findViewById(R.id.result_item_content_definition)
        private val itemUsageView: TextView =
            itemView.findViewById(R.id.result_item_content_usage)

        lateinit var onLoadCompleted: (Fragment, Int) -> Unit

        // bind -- binds data to display view for an item

        fun bind(resultItem: ResultItem, myListener: (ResultItem) -> Unit) =
            with(itemView) {
                itemEntryView.text = resultItem.entry
                itemDefinitionView.text = resultItem.definition
                itemUsageView.text = resultItem.usage
                setOnClickListener {
                    myListener(resultItem)
                    VPShellFragment.setNextOnLoadCompleted(fragment, resultItem.position, onLoadCompleted)
                }
            }

        override fun toString(): String = super.toString() + " '" + itemEntryView.text + "'"
    }  // class ViewHolder
} // class KamusiItemRecyclerViewAdapter