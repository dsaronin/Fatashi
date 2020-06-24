package org.umoja4life.fatashi

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity


private const val DEBUG = false
private const val LOG_TAG = "MainActivity"

// MainActivity -- APP starting point

class MainActivity : AppCompatActivity()  {

    // onCreate callback -- when Activity is first created
    // Inflate the contentView
    // Inflate the ActionBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
    }

    // hideKeyboard  -- make the soft keyboard disappear onClick

    private fun hideKeyboard(view: View) {
        val inputMethodManager: InputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    // searchRequest -- invoked when the user taps the search button
    // read contents of search field and replace Fragment to display
    // >>>>>> onClick response from the search icon: activity_main.xml <<<<<<<<<<<\
    // our job here is to grab the search-field-input text string, then
    // pass it on to the Fragment handling it.

    fun searchRequest(view: View) {

        val maulizo  = findViewById<EditText>(R.id.search_request_input).text.toString()

        val myfragment: KamusiItemFragment?
                = supportFragmentManager
                  .findFragmentById(R.id.list_fragment) as KamusiItemFragment?

        if (DEBUG) Log.d(LOG_TAG, ">>> SearchRequest <<< ${R.id.list_fragment.toString()} ${myfragment != null}: $maulizo");

        hideKeyboard(view)  // vanish keyboard from the screen
        myfragment?.updateFragmentResults(maulizo)  // send query to fragment to update results
    }
}