package org.umoja4life.fatashi

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_main.*
import org.umoja4life.basicio.AndroidPlatform
import org.umoja4life.kamusimodel.KamusiViewModel
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.util.concurrent.atomic.AtomicBoolean

private const val DEBUG = false
private const val LOG_TAG = "MainActivity"

        const val DEFAULT_POSITION = 0  // default/starting position in list of kamusi results
        const val DEFAULT_PATH = "/sdcard/Download/fatashi"

// MainActivity -- APP starting point``````````````````````

class MainActivity : AppCompatActivity(), ActivityCompat.OnRequestPermissionsResultCallback  {

    private lateinit var myLayout: View
    val myViewModel = KamusiViewModel()
    var myPath = DEFAULT_PATH
    var myDirectoryUri: Uri? = null
    val isRepeat = AtomicBoolean(false)  // true if onResume not first time

    // onCreate callback -- when Activity is first created
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (DEBUG) Log.d(LOG_TAG, ">>> onCreate <<< ******************* $myPath")

        setContentView(R.layout.activity_main)  // Inflate the contentView
        myLayout = fragment_container   // id for the main layout section

        setSupportActionBar(toolbar)  // Inflate the ActionBar: findViewById(R.id.toolbar)
        handleKeyboardSubmit(search_request_layout) // findViewById( R.id.search_request_layout )

        if (savedInstanceState != null ) {  // means changing orientation
            currentPosition = savedInstanceState.getInt(KEY_POSITION, DEFAULT_POSITION)
        }

       // If have DIRECTORY read permission, kick off backend setup, asynchronously
        if ( isDirectoryPermission() ) initializeFatashiBackend()
        else openDirectory()  // processing will pick up from onActivityResult()

        initiateKamusiItemFragment() // -- if it doesn't exist
    }

        // initiateKamusiItemFragment -- if it doesn't exist
    private fun initiateKamusiItemFragment() {
        if (supportFragmentManager.findFragmentById(R.id.fragment_container) == null) {
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
    private val displayLambda : (List<String>, Boolean) -> Unit = { listResults, clearBuffer ->
        var myfragment  = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (DEBUG) Log.d(LOG_TAG, ">>> displayLambda <<< $this, $myfragment")

        if ( myfragment is VPShellFragment ) {  // oops, still on the detail view fragment
            supportFragmentManager.beginTransaction()
                .remove(myfragment)
                .commit()
            supportFragmentManager.popBackStackImmediate()  // pop back to KamusiItemFragment

            // now get the fragment supporting that view
            myfragment  = supportFragmentManager.findFragmentById(R.id.fragment_container)
        }
        (myfragment as KamusiItemFragment).updateFragmentResults(listResults, clearBuffer)
    }

        // startupLambda  -- callback for mock command as first display for user
    private val startupLambda : ( ) -> Unit = {
        myViewModel.parseCommand("l")  // does list command
    }

    // initializeFatashiBackend -- convenience function to hold backend initialization
    // kicks off Backend initialization but doesn't wait for completion
    // DEPENDING UPON where Read Permission is detected/granted, this can be called
    // from onCreate() above, or onRequestPermissionResult (click action)
    private fun initializeFatashiBackend() {
        // val myPath = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)?.path ?: DEFAULT_PATH
        if (DEBUG) Log.d(LOG_TAG, ">>> initBackend <<< path: $myPath")

        myViewModel.initializeBackend(
            AndroidPlatform(myPath, myLayout, this, myDirectoryUri, displayLambda),
            startupLambda
        )
    }

    // initializeFallbackBackend  -- fallback if can't get read permission or access files
    // will use small built-in kamusi for demo purposes
    private fun initializeFallbackBackend() {
        // val myPath = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)?.path ?: DEFAULT_PATH
        if (DEBUG) Log.d(LOG_TAG, ">>> initBFallback <<< path: $myPath")

        myViewModel.startNoFileBackend(
            AndroidPlatform(myPath, myLayout, this, myDirectoryUri, displayLambda),
            startupLambda
        )

    }

    //  callback just before the activity starts interacting with the user.
    //  At this point, the activity is at the top of the activity stack
    override fun onResume() {
        super.onResume()

        if (DEBUG) Log.d(LOG_TAG, ">>> onResume <<< ********************")

            // first time thru, AndroidPlatform & MyEnvironment have already
            // been started in onCreate, so skip the refresh here
        if (isRepeat.getAndSet(true)) {
            myLayout = fragment_container   // id for the main layout section

            if (DEBUG) Log.d(LOG_TAG, ">>> onResume <<< repeat")

            myViewModel.replacePlatform(
                AndroidPlatform(myPath, myLayout, this, myDirectoryUri, displayLambda)
            )
        }
        initiateKamusiItemFragment()
    }

    // handleKeyboardSubmit -- setup the listener for keyboard SEARCH-submits
    // setup a listener for keyboard-based SEARCH submit button
    private fun handleKeyboardSubmit(view: TextInputLayout)  {
        if (DEBUG) Log.d(LOG_TAG, ">>> kbd listen <<< (${view.editText != null})")

        view.editText?.setOnEditorActionListener { _, actionId, _ ->

            if (DEBUG) Log.d(LOG_TAG, ">>> kbd TRIGGER <<< $actionId")

            // treat same as onClick button
            when (actionId) {
                EditorInfo.IME_ACTION_SEARCH,
                EditorInfo.IME_ACTION_DONE,
                EditorInfo.IME_ACTION_NEXT,
                EditorInfo.IME_NULL -> {
                    searchRequest(view)
                    true
                }
                else                             -> false
            }

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
        if (startedBackend.get())  {
            if (DEBUG) Log.d(LOG_TAG, ">>> VM.parseCommand <<<  ")
            myViewModel.parseCommand(maulizo)
            // asynch return here possibly BEFORE backend has processed!
        }
        else {  // tell the user we can't do anything without read permissions
            Snackbar.make(myLayout, R.string.cannot_search, Snackbar.LENGTH_LONG)
                .setAction(R.string.retry) {
                    // Responds to click on the action ==>
                    // try again to get read permission, initializeBackEnd, then parseCommand
                    openDirectory()  // onActivityResult
                    myViewModel.parseCommand(maulizo)
                }  // onClick RETRY
                .show()
        }  // fi startedBackend ; else
    }

    // onSaveInstanceState callback -- when activity is being put on hold; save currentPosition
    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)

        outState.putInt(KEY_POSITION, currentPosition)
    }

    //  vvvvvvvvv V0.2 Document Tree support ===============================================

    // onActivityResult -- callback for ACTION_OPEN_DOCUMENT_TREE
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val directoryUri = data?.data

        if (DEBUG) Log.d(LOG_TAG, ">>> onActivityResult <<<  ${directoryUri?.getPath() ?: "PATH NULL"}  ")

        if (
            requestCode == OPEN_DIRECTORY_REQUEST_CODE &&
            resultCode == Activity.RESULT_OK &&
            directoryUri != null
        ) {  // **** SUCCESS comes here; do something *****************
               // persist that we have directory read permission
            contentResolver.takePersistableUriPermission(
                directoryUri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            setMyPath(directoryUri)
            initializeFatashiBackend()  // asynchronously initialize backend
            // returns here before completion of backend setup

        }
        else {  // else FAILURE comes here; Permission request was denied.
            Snackbar.make(myLayout, R.string.read_permission_denied, Snackbar.LENGTH_LONG).show()
            // initialize builtin kamusi for demo purposes
            initializeFallbackBackend()
        }

    }

    // openDirectory  -- initiates ACTION_OPEN_DOCUMENT_TREE
    // asynchronously returns
    private fun openDirectory() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        startActivityForResult(intent, OPEN_DIRECTORY_REQUEST_CODE)
    }

    // isOpenDirectory()  -- returns Uri of a directory user has already chosen
    // else returns null: no directory selected yet
    // !! returns first Uri for UriPermission with ReadPermission; assumes correct
    private fun getUriForOpenDirectory() : Uri?  {

        if (DEBUG) Log.d(LOG_TAG, ">>> getUriForOpenDirectory <<<  ")

        for (urip in contentResolver.getPersistedUriPermissions( ) ) {
            if ( urip.isReadPermission() ) return urip.getUri()
        }

        return null   // failure
    }

    // isDirectoryPermission -- checks if permission exists & sets myPath, if so
    private fun isDirectoryPermission() : Boolean {
        val directoryUri = getUriForOpenDirectory() ?: return false

        setMyPath(directoryUri)  // convert Uri to String & set myPath
        return true
    }

    // setMyPath -- convert Uri to String & set myPath, myDirectoryUri
    private fun setMyPath(directoryUri: Uri) {
        myDirectoryUri = directoryUri
        myPath = directoryUri.toString()
        if (DEBUG) Log.d(LOG_TAG, ">>> setMyPath <<<  $myPath")
    }

    //  ^^^^^^^^^ V0.2 Document Tree support ===============================================



    // ************************************************************************
    // ************************************************************************
    // ************************************************************************
    companion object {
        private const val KEY_POSITION = "org.umoja4life.fatashi.key.currentPosition"

            // currentPosition remembers selected result item out of list of items
            // used to remember state throughout transitions
        var currentPosition = DEFAULT_POSITION
        var startedBackend = AtomicBoolean(false)

    }
    
} // class MainActivity

private const val OPEN_DIRECTORY_REQUEST_CODE = 0xf11e




