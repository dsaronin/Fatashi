package org.umoja4life.basicio

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.umoja4life.fatashi.BuildConfig
import org.umoja4life.fatashi.R
import org.umoja4life.fatashi.stripANSI
import org.umoja4life.fatashibackend.PlatformIO
import java.io.File
import java.io.IOException


private const val DEBUG = false
private const val LOG_TAG = "AndroidPlatform"


class AndroidPlatform(
    override val myPath: String,
    val myView : View,
    val myContext : Context,
    val displayLambda : (List<String>, Boolean) -> Unit
) : PlatformIO {

    // *********************************************************************************
    // getFile -- do File().readText() in a non-blocking way
    override suspend fun getFile(f: String): String {
        var result = ""

        withContext(Dispatchers.IO) {
            try {
                result = File(myPath + f).readText()
            } catch (ex: IOException) {
                if (DEBUG) Log.e(LOG_TAG, "file: $f ex: $ex")
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

        if (DEBUG) Log.e(LOG_TAG, ">>>>> $s")
        Snackbar.make(myView, s.stripANSI(), Snackbar.LENGTH_LONG).show()
    }

    // *********************************************************************************
    // simple brief info for user: handle as long toast
    override fun lineoutInfo(s: String) {
        if (DEBUG) Log.d(LOG_TAG, ">>> lineoutInfo <<<  $s")

        Toast.makeText(myContext, s.stripANSI(), Toast.LENGTH_LONG).show()
    }

    // *********************************************************************************
    // primary dictionary results interface
    override fun listout(l: List<String>, clearBuffer: Boolean) {
        if (DEBUG) Log.d(LOG_TAG, ">>> listout <<< #items: ${l.size}, clear: $clearBuffer")

        displayLambda(l, clearBuffer)  // will display view KamusiItemFragment & RecyclerViewHandler
    }

    override fun getFrontVersion(): String =
        "\nAndroid: ${BuildConfig.VERSION_NAME} [${BuildConfig.VERSION_CODE}] @ ${BuildConfig.BUILD_TIME}" +
        "\n(c) 2020 David S. Anderson\nAll Rights Reserved"

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


