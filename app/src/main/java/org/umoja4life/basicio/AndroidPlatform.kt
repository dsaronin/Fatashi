package org.umoja4life.basicio

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.umoja4life.fatashi.R
import org.umoja4life.fatashi.stripANSI
import org.umoja4life.fatashibackend.PlatformIO
import java.io.File
import java.io.IOException

private const val DEBUG = true
private const val LOG_TAG = "AndroidPlatform"


class AndroidPlatform(
    override val myPath: String,
    val myView : View,
    val myContext : Context,
    val displayLambda : (List<String>) -> Unit
) : PlatformIO {

    // *********************************************************************************
    // getFile -- do File().readText() in a non-blocking way
    override suspend fun getFile(f: String): String {
        var result = ""

        withContext(Dispatchers.IO) {
            try {
                result = File(myPath + f).readText()
            } catch (ex: IOException) {
                Log.e(LOG_TAG, "file: $f ex: $ex")
            } // catch
        }
        if (DEBUG) Log.d(LOG_TAG, ">>> getFile <<< f: $f, size: ${result.length}")

        return result
    }

    // *********************************************************************************
    // output to an Alert-style modal box some brief information/status
    override fun infoAlert(s: String) {
        if (DEBUG) Log.d(LOG_TAG, ">>> infoAlert <<< $s")

        MaterialAlertDialogBuilder(myContext)
            .setTitle( myContext.getString(R.string.app_alert) )
            .setMessage( s )
            .setNeutralButton(myContext.getString(R.string.okay), null)
            .show()
    }

    override fun infoAlert(l: List<String>) {
        infoAlert( l.joinToString("\n"))
    }


    // *********************************************************************************
    // tell user about an error
    override fun lineoutError(s: String) {
        if (DEBUG) Log.d(LOG_TAG, ">>> lineoutError <<< $s")

        Log.e(LOG_TAG, ">>>>> $s")
        Snackbar.make(myView, s, Snackbar.LENGTH_LONG).show()
    }

    // *********************************************************************************
    // simple brief info for user: handle as long toast
    override fun lineoutInfo(s: String) {
        if (DEBUG) Log.d(LOG_TAG, ">>> lineoutInfo <<<  $s")

        Toast.makeText(myContext, s, Toast.LENGTH_LONG).show()
    }

    // *********************************************************************************
    // primary dictionary results interface
    override fun listout(l: List<String>) {
        if (DEBUG) Log.d(LOG_TAG, ">>> listout <<< #items: ${l.size}")

        displayLambda(l)  // will display view KamusiItemFragment & RecyclerViewHandler
    }

    // *********************************************************************************
    // ******************** NOT IMPLEMENTED IN ANDROID *********************************
    // *********************************************************************************

    override fun putPrompt(prompt: String) {
        TODO("Not yet implemented: putPrompt")
    }

    override fun getCommandLine(): List<String> {
        TODO("Not yet implemented: getCommandLine")
    }

} // class AndroidPlatform

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

