package org.umoja4life.fatashi

import android.app.Activity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_main.*

private const val DEBUG = false
private const val LOG_TAG = "MainActivity"
private const val DEFAULT_POSITION = 0

// MainActivity -- APP starting point

class MainActivity : AppCompatActivity()  {

    // onCreate callback -- when Activity is first created
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)  // Inflate the contentView
        setSupportActionBar( toolbar )  // Inflate the ActionBar: findViewById(R.id.toolbar)
        handleKeyboardSubmit( search_request_layout ) // findViewById( R.id.search_request_layout )

        if (savedInstanceState != null ) {  // means changing orientation
            currentPosition = savedInstanceState.getInt( KEY_POSITION, DEFAULT_POSITION )
        }
        else {  // initiate KamusiItemFragment
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, KamusiItemFragment())
                .commit()
        }
    }

    // onSaveInstanceState callback -- when activity is being put on hold; save currentPosition
    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)

        outState.putInt( KEY_POSITION, currentPosition )
    }

    // handleKeyboardSubmit -- setup the listener for keyboard SEARCH-submits
    // setup a listener for keyboard-based SEARCH submit button

    private fun handleKeyboardSubmit( view: TextInputLayout )  {
        if (DEBUG) Log.d(LOG_TAG, ">>> kbd listen <<< (${view.editText != null})" )

        view.editText?.setOnEditorActionListener { _, actionId, _ ->

            if (DEBUG) Log.d(LOG_TAG, ">>> kbd TRIGGER <<< $actionId" )

            // treat same as onClick button
            if (actionId == EditorInfo.IME_ACTION_SEARCH) searchRequest(view)
            true
        }
    }

    // hideKeyboard  -- make the soft keyboard disappear onClick

    private fun hideKeyboard(view: View) {
        (getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)
        .hideSoftInputFromWindow(view.windowToken, 0)
    }

    // searchRequest -- invoked when the user taps the search button
    // read contents of search field and replace Fragment to display
    // >>>>>> onClick response from the search icon: activity_main.xml <<<<<<<<<<<\
    // our job here is to grab the search-field-input text string, then
    // pass it on to the Fragment handling it.

    fun searchRequest(view: View) {
            // housekeeping stuff before refreshing search results
        hideKeyboard(view)  // vanish keyboard from the screen
        currentPosition = DEFAULT_POSITION      // reset current position

        val maulizo  = search_request_input.text.toString()  // R.id.search_request_input

        var myfragment  = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (DEBUG) Log.d(LOG_TAG, ">>> SearchRequest <<< ${myfragment != null}: $maulizo");

        if ( myfragment is VPShellFragment ) {  // oops, still on the detail view fragment
            supportFragmentManager.popBackStackImmediate()  // pop back to KamusiItemFragment
                // now get the fragment supporting that view
            myfragment  = supportFragmentManager.findFragmentById(R.id.fragment_container)
        }

            // TODO: Model has to do the actual search on maulizo query
            // send query to fragment to update results
        (myfragment as KamusiItemFragment).updateFragmentResults(maulizo)
    }

    // ************************************************************************
    // ************************************************************************
    // ************************************************************************
    companion object {

            // currentPosition remembers selected result item out of list of items
            // used to remember state throughout transitions
        var currentPosition = DEFAULT_POSITION

        private const val KEY_POSITION = "org.umoja4life.fatashi.key.currentPosition"
    }
} // class MainActivity