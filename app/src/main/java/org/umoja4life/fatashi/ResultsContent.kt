package org.umoja4life.fatashi

import android.text.SpannableStringBuilder
import androidx.core.text.bold
import androidx.core.text.scale
import org.umoja4life.fatashibackend.AnsiColor

private const val DEBUG = false
private const val LOG_TAG = "ResultsContent"

    // stripANSI -- simply removes ANSI codes from a string
fun String.stripANSI() : String {
    return this.replace(AnsiColor.ansiRegex,"")
}

    // rewrapANSI -- extension to strip ANSI codes & rewrap for Android
    // unused: color( R.color.colorSecondary900 ) { }
fun String.rewrapANSI() : SpannableStringBuilder {
    val sb = SpannableStringBuilder()   // init the Spannable
    val l = this.split(AnsiColor.ansiRegex)   // break into wrap/no-wrap segments
    var wrapNext = false     // used to alternate between wrap/no-wrap segments
    var s : String               // holder for a segment
    var i = 0               // index into list

        // primary list processing loop
    while (i in l.indices) {
        s = l[i++]  // pop item
        if ( i > l.size - 1  &&  s.isEmpty() ) break  // we hit the end of the list and a dud marker

        if ( s.isEmpty()  ||  wrapNext ) { // wrap this section of text
            if (s.isEmpty() ) s = l[i++]  // skip past blank formatting; get text segment
            sb.bold { scale(1.12F) { append(s) } }
            wrapNext = false
        }
        else {  // non-wrapped text
            sb.append(s)       // append the unwrapped segment
            wrapNext = true   // next segment will be wrapped
        }
    } // while

    return sb
}

private val sampleResult = listOf( "Karibu Fatashi! Tuanze...")
private val indexList: List<Int> = sampleResult.indices.toList()

data class ItemDetail(var entry: String, var def: String, var usage: String)

// "model" for dealing with results data

object ResultsContent {

    // An array of kamusi result items from search
    val RESULT_ITEMS: MutableList<ResultItem> = mutableListOf()

    init {
        buildResultItems( sampleResult)  // REEVALUATE and maybe remove from initial showing
   }

    fun itemsCount() = RESULT_ITEMS.size

    // buildResultItems -- main entry point for displaying a new list of results
    // which have come from the backend, via AndroidPlatform.listOut()
    // current version strips out any ANSI color wrapping in the string
    fun buildResultItems(resultList: List<String>, clearBuffer: Boolean = true) : Int  {
        if ( RESULT_ITEMS.isNotEmpty() && clearBuffer ) RESULT_ITEMS.clear()
        val index = RESULT_ITEMS.size  // updates added after this index

        for ( i in resultList.indices ) {
            val (entry, definition, usage) = parseFields(resultList[i])
            RESULT_ITEMS.add(
                ResultItem(i+index, resultList[i], entry,  definition, usage)
            )
        }
        return index - 1  // additions were added after this index
    }

    // TODO: move this into the project configuration file
    private val itemRegex = "^([^\\t]+)\\t-* ?([^\\t]+)\\t?-* ?([^\\t]*)\$"

    // parseFields  -- split a search result line into three fields
    // TODO: needs to be taken over by Kamusi Class functions

    fun parseFields(rawItem: String) : ItemDetail {
        val keyfrag = itemRegex.toRegex().find(rawItem)

            // if there was an issue matching/splitting to fields, at least
            // return the entire search result line as the entry default!
        return ItemDetail(
            keyfrag?.groupValues?.get(1) ?: rawItem,
            keyfrag?.groupValues?.get(2) ?: "",
            keyfrag?.groupValues?.get(3) ?: ""
        )
    }

    // An item representing a piece of content.
    data class ResultItem(
        val position: Int,
        val content: String,
        val entry: String,
        val definition: String,
        val usage: String
    ) {
        override fun toString(): String = content
    }  // class ResultItem

} // object ResultsContent

/*
    fun newQuery(  ) = buildResultItems( shuffleList() )

// shuffleList is for debugging & testing purposes
fun shuffleList(): List<String>  {
    val idx1 = indexList.random()
    var idx2 = (0..11).random() + idx1
    if (idx2 > indexList.size - 1 ) idx2 = indexList.size - 1
    return sampleResult.slice( idx1..idx2)
}
*/

