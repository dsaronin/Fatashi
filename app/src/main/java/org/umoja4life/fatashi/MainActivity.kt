package org.umoja4life.fatashi

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import org.umoja4life.fatashi.dummy.ResultsContent

private const val DEBUG = true
private const val LOG_TAG = "MainActivity"


class MainActivity : AppCompatActivity()  {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
    }

    // searchRequest -- invoked when the user taps the search button
    // read contents of search field and replace Fragment to display
    fun searchRequest(view: View) {

        val editText = findViewById<EditText>(R.id.search_request_input)
        val maulizo  = editText.text.toString()

        val myfragment: KamusiItemFragment? = supportFragmentManager.findFragmentById(R.id.list_fragment) as KamusiItemFragment?

        if (DEBUG) Log.d(LOG_TAG, ">>> SearchRequest <<< ${R.id.list_fragment.toString()} ${myfragment != null}: $maulizo");
        myfragment?.updateFragmentResults(maulizo)
    }
}