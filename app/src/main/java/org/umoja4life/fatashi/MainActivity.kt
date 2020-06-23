package org.umoja4life.fatashi

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import org.umoja4life.fatashi.dummy.ResultsContent

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
    }

    private val LOG_TAG = "searchRequest";



    // searchRequest -- invoked when the user taps the search button
    // read contents of search field and replace Fragment to display
    fun searchRequest(view: View) {

        val editText = findViewById<EditText>(R.id.search_request_input)
        val maulizo  = editText.text.toString()

        ResultsContent.newQuery( maulizo )

        Log.d(LOG_TAG, "${R.id.list_fragment.toString()}: $maulizo");

        supportFragmentManager.beginTransaction()
            .setTransition( FragmentTransaction.TRANSIT_NONE )
            .replace(R.id.list_fragment, KamusiItemFragment() )
            .addToBackStack( maulizo )
            .commit()
    }
}