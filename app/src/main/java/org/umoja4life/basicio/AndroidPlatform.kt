package org.umoja4life.basicio

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.net.toUri
import androidx.documentfile.provider.DocumentFile
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.umoja4life.fatashi.BuildConfig
import org.umoja4life.fatashi.MainActivity
import org.umoja4life.fatashi.R
import org.umoja4life.fatashi.stripANSI
import org.umoja4life.fatashibackend.PlatformIO
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset
import java.util.concurrent.atomic.AtomicBoolean

private fun InputStream.readText(charset: Charset= Charsets.UTF_8): String {
    return readBytes().toString(charset)
}

private const val DEBUG = false
private const val LOG_TAG = "AndroidPlatform"


class AndroidPlatform(
    override val myPath: String,
    val myView : View,
    val myContext : Context,
    val directoryUri : Uri?,
    val displayLambda : (List<String>, Boolean) -> Unit
) : PlatformIO {

    var showDirList = AtomicBoolean(false)  // true if already listed directory in log

    val dirFiles: Array<DocumentFile> by lazy {
        if ( directoryUri == null ) arrayOf<DocumentFile>()
        else {
            DocumentFile.fromTreeUri(myContext, directoryUri)?.listFiles()
                ?: arrayOf<DocumentFile>()
        }
    }

    // *********************************************************************************
    // note difference between path for Uri.withAppendedPath(myPath.toUri()).path
    // and that of the path by finding the file in the list of files in given directory
    // ex: content://com.android.externalstorage.documents/tree/primary%3ADownload%2Ffatashi
    // ex: /tree/primary:Download/fatashi/document/primary:Download/fatashi/config_properties.json
    // *********************************************************************************
    // getFile -- do File().readText() in a non-blocking way
    override suspend fun getFile(f: String): String {
        var result = ""

        withContext(Dispatchers.IO) {
            loadDirectory()   // forces lazy load dirFiles && debug file trace
            val uri = getUriFromFile(f)  // find the file URI for f in dirFiles

            if ( uri != null ) {  // if valid uri
                val resolver: ContentResolver = myContext.contentResolver
                if (DEBUG) Log.d(LOG_TAG, ">>> found File Uri <<< ${uri.path}")

                // open input stream for the URI and then readText for entire file
                result = resolver.openInputStream(uri)?.use {
                        stream -> stream.readText()
                } ?: fileError(uri.path)
            }
        }
        if (DEBUG) Log.d(LOG_TAG, ">>> getFile <<< f: $f, size: ${result.length}")

        return result
    }
    // *********************************************************************************
    // fileError just logs the error and returns empty string
    // convenience function
    private fun fileError(s: String?) : String {
        if (DEBUG) Log.e(LOG_TAG, ">>> file failure <<<:  $s ")
        return ""
    }
    // *********************************************************************************
    // debugging: logs all the files in the chosen directory
    // showDirList AtomicBoolean flips on first time
    // dirFiles will be lazy initialized at first isNullOrEmpty check
    // logging lists directory only if DEBUG switch is on
    private fun loadDirectory() {
        if ( !showDirList.getAndSet(true) &&
             !dirFiles.isNullOrEmpty()  &&
              DEBUG
        ) {  // only list directory if firsttime && dirFiles has been setup && DEBUG flag on
            for (doc in dirFiles) {
                Log.d(LOG_TAG, ">>> dirList <<< name: ${doc.name}, path: ${doc.uri.path}")
            }
        }
    }
    // *********************************************************************************
    // getUriFromFile -- returns the URI for a given file if found, else null
    private fun getUriFromFile(f: String) : Uri? {
        if( f.isBlank() ) return null
        return dirFiles.find { it.name.equals(f) }?.uri ?: null
    }

    // *********************************************************************************
    // *********************************************************************************
    // getFile_v0_1 -- do File().readText() in a non-blocking way
    // DEPRECATED due to Android 11 SAF
    /***********
    suspend fun getFile_v0_1(f: String): String {
    var result = ""

    withContext(Dispatchers.IO) {
    try {
    val uri = Uri.withAppendedPath(myPath.toUri(), f)
    Log.d(LOG_TAG, ">>> appendedPath <<< f: ${uri.path}")

    result = File(myPath + f).readText()
    } catch (ex: IOException) {
    Log.e(LOG_TAG, "file:  ${myPath + f} ex: $ex")
    } // catch
    }
    Log.d(LOG_TAG, ">>> getFile <<< f: ${myPath + f}, size: ${result.length}")

    return result
    }
     ******************/

    // *********************************************************************************
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
        "\n(c) 2022 David S. Anderson\nAll Rights Reserved"

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


