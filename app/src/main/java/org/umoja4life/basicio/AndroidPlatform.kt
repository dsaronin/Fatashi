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
import org.umoja4life.fatashibackend.PlatformIO
import java.io.File
import java.io.IOException

private const val DEBUG = true
private const val LOG_TAG = "AndroidPlatform"


class AndroidPlatform(
    override val myPath: String,
    val myView : View,
    val myContext : Context
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
        MaterialAlertDialogBuilder(myContext)
            .setTitle( myContext.getString(R.string.app_alert) )
            .setMessage( s )
            .show()
    }

    override fun infoAlert(l: List<String>) {
        infoAlert( l.joinToString("\n"))
    }


    // *********************************************************************************
    // tell user about an error
    override fun lineoutError(s: String) {
        Log.e(LOG_TAG, ">>>>> $s")
        Snackbar.make(myView, s, Snackbar.LENGTH_LONG).show()
    }

    // *********************************************************************************
    // simple brief info for user: handle as long toast
    override fun lineoutInfo(s: String) {
        Toast.makeText(myContext, s, Toast.LENGTH_LONG).show()
    }

    // *********************************************************************************
    // primary dictionary results interface
    override fun listout(l: List<String>) {
        TODO("Not yet implemented")
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

}