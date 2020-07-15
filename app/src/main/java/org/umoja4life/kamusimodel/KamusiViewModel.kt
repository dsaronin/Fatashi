package org.umoja4life.kamusimodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException

private const val DEBUG = true
private const val LOG_TAG = "KamusiViewModel"

const val filepath = "/sdcard/Download/"

class KamusiViewModel: ViewModel() {
    var result: String = "file not read yet"

    fun needJson(f:String, showme: (String) -> Unit  ) {
        if (DEBUG) Log.d(LOG_TAG, ">>> needJson <<< f: $f")
        viewModelScope.launch {
            simpleGetFile(f)
            if (DEBUG) Log.d(LOG_TAG, ">>> postneedJson <<<  $result")
            showme(result)
        }
    }

    suspend fun simpleGetFile(f: String) = withContext(Dispatchers.IO) {
        try {
            result = File(filepath+f).readText()
        } catch (ex: IOException) {
            Log.e(LOG_TAG, ex.toString())
            Log.e(LOG_TAG, "file: $f: caused an I/O Exception Error")
        } // catch
    }

}  // class KamusiViewModel