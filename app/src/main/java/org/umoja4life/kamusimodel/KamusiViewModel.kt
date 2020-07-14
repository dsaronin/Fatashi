package org.umoja4life.kamusimodel

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import java.io.File
import java.io.IOException

private const val DEBUG = true
private const val LOG_TAG = "KamusiViewModel"

const val filepath = "/storage/emulated/0/Download/"

class KamusiViewModel: ViewModel() {

    fun needJson(f:String, showme: (String) -> Unit  ) {
        var result: String = "file $f not read yet"

        if (DEBUG) Log.d(LOG_TAG, ">>> needJson <<< f: $f")
        viewModelScope.launch { result = simpleGetFile(f) as String }
        if (DEBUG) Log.d(LOG_TAG, ">>> postneedJson <<<  $result")
        showme(result)
    }

    suspend fun simpleGetFile(f: String) = withContext(Dispatchers.IO) {
        try {
            File(filepath+f).readText()
        } catch (ex: IOException) {
            Log.e(LOG_TAG, ex.toString())
            Log.e(LOG_TAG, "file: $f: caused an I/O Exception Error")
        } // catch
    }

}  // class KamusiViewModel