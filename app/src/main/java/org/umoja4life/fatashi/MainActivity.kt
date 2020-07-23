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
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_main.*
import org.umoja4life.basicio.AndroidPlatform
import org.umoja4life.basicio.FileServices
import org.umoja4life.basicio.READ_PERMISSION_CODE
import org.umoja4life.kamusimodel.KamusiViewModel


private const val DEBUG = true
private const val LOG_TAG = "MainActivity"

        const val DEFAULT_POSITION = 0  // default/starting position in list of kamusi results
        const val DEFAULT_PATH = "/sdcard/Download/"

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
        // findViewById<FloatingActionButton>(R.id.fab_read)
        //    .setOnClickListener { view -> doSomeInput(view) }

        if (savedInstanceState != null ) {  // means changing orientation
            currentPosition = savedInstanceState.getInt( KEY_POSITION, DEFAULT_POSITION )
        }
        else {
            // If have read permission, kicks off backend setup, asynchronously
            if (FileServices.hasReadPermission(this)) initializeFatashiBackend()
            // else we might be continuing here without read permission!

            supportFragmentManager    // initiate KamusiItemFragment
                .beginTransaction()
                .add(R.id.fragment_container, KamusiItemFragment())
                .commit()
        }
    }

    /*
    A lambda expression or anonymous function
    (as well as a local function and an object expression)
    can access its closure, i.e. the variables declared in the outer scope.
    The variables captured in the closure can be modified in the lambda
    */
    // displayLambda  -- will be used within AndroidPlatform.listOut() to process the results
    // list and display it view RecyclerViewAdapter.
    private val displayLambda : (List<String>) -> Unit = { listResults ->
        var myfragment  = supportFragmentManager.findFragmentById(R.id.fragment_container)

        if ( myfragment is VPShellFragment ) {  // oops, still on the detail view fragment
            supportFragmentManager.popBackStackImmediate()  // pop back to KamusiItemFragment
            // now get the fragment supporting that view
            myfragment  = supportFragmentManager.findFragmentById(R.id.fragment_container)
        }
        (myfragment as KamusiItemFragment).updateFragmentResults( listResults )
    }

    // initializeFatashiBackend -- convenience function to hold backend initialization
    // kicks off Backend initialization but doesn't wait for completion
    // DEPENDING UPON where Read Permission is detected/granted, this can be called
    // from onCreate() above, or onRequestPermissionResult (click action)
    private fun initializeFatashiBackend() {
        // val myPath = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)?.path ?: DEFAULT_PATH
        val myPath = DEFAULT_PATH

        if (DEBUG) Log.d(LOG_TAG, ">>> initBackend <<< path: $myPath")

        myViewModel.initializeBackend(
            AndroidPlatform(myPath,myLayout,this, displayLambda)
        )
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
                initializeFatashiBackend()  // asynchronously initialize backend
                // returns here before completion of backend setup
            }
            else {
                // Permission request was denied.
                Snackbar.make(myLayout, R.string.read_permission_denied, Snackbar.LENGTH_SHORT).show()
                // only enable built-in test data?
                // Snackbar.make(myLayout, R.string.forced_exit, Snackbar.LENGTH_SHORT).show()
                // finishAndRemoveTask()
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
        if (DEBUG) Log.d(LOG_TAG, ">>> SearchRequest <<<  $maulizo")

            // skip unless the backend has been authorized and started
        if (startedBackend)  {
            if (DEBUG) Log.d(LOG_TAG, ">>> VM.parseCommand <<<  ")
            myViewModel.parseCommand( maulizo )
            // asynch return here possibly BEFORE backend has processed!
        }
        else {  // tell the user we can't do anything without read permissions
            Snackbar.make(myLayout, R.string.cannot_search, Snackbar.LENGTH_SHORT)
                .setAction(R.string.retry) {
                    // Responds to click on the action ==>
                    // try again to get read permission, initializeBackEnd, then parseCommand
                    if (FileServices.hasReadPermission(this)) {
                        initializeFatashiBackend()
                        myViewModel.parseCommand( maulizo )
                    }
                }  // onClick RETRY
                .show()
        }  // fi startedBackend ; else
    }

    // DEBUG USAGE ONLY: doSomeInput was for testing purposes
    private fun doSomeInput(view: View) {
        TODO("DEBUGGING vestige only in doSomeInput()")
        if (DEBUG) Log.d(LOG_TAG, ">>> doSomeInput <<< ")
        val perm = FileServices.hasReadPermission(this)
        // Snackbar.make(view, "Permission state: $perm", Snackbar.LENGTH_LONG).show()

        // (getActivity() as MainActivity).
        if (perm) {
            myViewModel.getKamusiFormatJson(
                "config_properties.json",
                onSuccess = {kf ->
                    Toast.makeText(this, kf.filename, Toast.LENGTH_LONG).show()
                    kf
                },
                onFail = {err -> Toast.makeText(this, err, Toast.LENGTH_LONG).show()}
            )
        }  // fi perm
    }

    // onSaveInstanceState callback -- when activity is being put on hold; save currentPosition
    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)

        outState.putInt( KEY_POSITION, currentPosition )
    }

    // ************************************************************************
    // ************************************************************************
    // ************************************************************************
    companion object {
        private const val KEY_POSITION = "org.umoja4life.fatashi.key.currentPosition"

            // currentPosition remembers selected result item out of list of items
            // used to remember state throughout transitions
        var currentPosition = DEFAULT_POSITION
        var startedBackend = false

    }
    
} // class MainActivity