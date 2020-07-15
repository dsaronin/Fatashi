package org.umoja4life.kamusimodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.umoja4life.fatashibackend.KamusiFormat
import java.io.File
import java.io.IOException

private const val DEBUG = true
private const val LOG_TAG = "KamusiViewModel"

const val filepath = "/sdcard/Download/"

class KamusiViewModel: ViewModel() {

    fun tryKFJson(f: String) : KamusiFormat {

    }

    fun needJson(f:String, useme: (String) -> Unit  ) {
        if (DEBUG) Log.d(LOG_TAG, ">>> needJson <<< f: $f")

        // wrap fileread in a scope
        viewModelScope.launch {
            useme( simpleGetFile(f) as String )
        }
    }

        // simpleGetFile  -- inputs a textfile in Dispatchers.IO space
    suspend fun simpleGetFile(f: String) : String {
            var result = ""

        withContext(Dispatchers.IO) {
            try {
                result = File(filepath + f).readText()
            } catch (ex: IOException) {
                Log.e(LOG_TAG, ex.toString())
                Log.e(LOG_TAG, "file: $f: caused an I/O Exception Error")
            } // catch
        }
        return result
    }

}  // class KamusiViewModel