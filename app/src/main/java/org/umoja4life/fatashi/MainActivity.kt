package org.umoja4life.fatashi

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_main.*
import org.umoja4life.basicio.*
import org.umoja4life.fatashibackend.KamusiFormat
import org.umoja4life.kamusimodel.KamusiViewModel
import org.umoja4life.util.showSnackbar


private const val DEBUG = true
private const val LOG_TAG = "MainActivity"
        const val DEFAULT_POSITION = 0  // default/starting position in list of kamusi results

// MainActivity -- APP starting point

class MainActivity : AppCompatActivity(), ActivityCompat.OnRequestPermissionsResultCallback  {

    private lateinit var myLayout: View
    val myViewModel = KamusiViewModel()

    // onCreate callback -- when Activity is first created
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)  // Inflate the contentView
        myLayout = fragment_container   // id for the main layout section

        setSupportActionBar( toolbar )  // Inflate the ActionBar: findViewById(R.id.toolbar)
        handleKeyboardSubmit( search_request_layout ) // findViewById( R.id.search_request_layout )

            // Floating Action Button Usage
        findViewById<FloatingActionButton>(R.id.fab_read)
            .setOnClickListener { view -> doSomeInput(view) }

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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (DEBUG) Log.d(LOG_TAG, ">>> onRequestPermissionsResult <<< ")

        if (requestCode == READ_PERMISSION_CODE) {
            // Request for read file storage permission.
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted. Start to do something
                myLayout.showSnackbar(R.string.read_permission_granted, Snackbar.LENGTH_SHORT)
                // ok to do something here <<<<<<<<<<<<<<<<<<<<
            } else {
                // Permission request was denied.
                myLayout.showSnackbar(R.string.read_permission_denied, Snackbar.LENGTH_SHORT)
                // exit app?? <<<<<<<<<<<<<
                // only enable built-in test data?
            }
        }
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

    private fun doSomeInput(view: View) {
        if (DEBUG) Log.d(LOG_TAG, ">>> doSomeInput <<< ")
        val perm = FileServices.hasReadPermission(this)
        // Snackbar.make(view, "Permission state: $perm", Snackbar.LENGTH_LONG)
        //    .setAction("Action", null).show()

        // (getActivity() as MainActivity).
        if (perm) {
            myViewModel.getKamusiFormatJson(
                "tempdict.json",
                onSuccess = {kf ->
                    Toast.makeText(this, kf.filename, Toast.LENGTH_LONG).show()
                    kf
                },
                onFail = {err -> Toast.makeText(this, err, Toast.LENGTH_LONG).show()}
            )
        }  // fi perm


    }

    // The view used to make the snackbar.
    // This should be contained within the view hierarchy you want to display the
    // snackbar. Generally it can be the view that was interacted with to trigger
    // the snackbar, such as a button that was clicked, or a card that was swiped.
    /*
        val contextView = findViewById<View>(R.id.context_view)

        Snackbar.make(contextView, R.string.text_label, Snackbar.LENGTH_SHORT)
        .show()

        ***************
    //    To add an action, use the setAction method on the object returned from make.
    //    Snackbars are automatically dismissed when the action is clicked.
    // To show a snackbar with a message and an action:

        Snackbar.make(contextView, R.string.text_label, Snackbar.LENGTH_LONG)
        .setAction(R.string.action_text) {
            // Responds to click on the action
        }
        .show()


        AlertDialog.Builder(myContext).create().apply {
            setTitle(myContext.getString(R.string.app_alert))
            setMessage(s)
            show()
        }  // apply


     */

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