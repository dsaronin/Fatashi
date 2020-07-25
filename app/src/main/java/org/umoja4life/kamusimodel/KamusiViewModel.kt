package org.umoja4life.kamusimodel

import android.os.Environment
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.umoja4life.basicio.AndroidPlatform
import org.umoja4life.basicio.DEFAULT_PATH
import org.umoja4life.fatashi.MainActivity
import org.umoja4life.fatashibackend.FatashiWork
import org.umoja4life.fatashibackend.KamusiFormat
import org.umoja4life.fatashibackend.MyEnvironment
import java.io.File
import java.io.IOException

private const val DEBUG = true
private const val LOG_TAG = "KamusiViewModel"

class KamusiViewModel: ViewModel() {
    private val MYARGS =  arrayOf("-v", "-p", "-n", "16")

    // initializeBackend  -- wrapped coroutine to setup MyEnvironment
    // ASSUMPTION: caller has already checked READ_PERMISSIONS
    // Three different places in MainActivity can call us, so recheck whether we've tried to
    // start or not already.
    fun initializeBackend(myPlatform : AndroidPlatform) {
        if (!MainActivity.startedBackend.getAndSet(true)) {  // try to initialize unless already tried
                if (DEBUG) Log.d(LOG_TAG, ">>> initializeBackend <<< STARTED ...")

            viewModelScope.launch {
                MyEnvironment.setup(MYARGS, myPlatform)
                // ASSUMPTION: No Kamusi/JSON file read errors!
                // If errors do occur, we'll still come here and think the Backend is setup.
                // FUTURE: if those errors occur, maybe put backend into TEST mode
                if (DEBUG) Log.d(LOG_TAG, ">>> initializeBackend <<< ...ENDED")
            }  // launch
        }
        else replacePlatform( myPlatform )
    }

    // each Android Activity lifecycle requires a refreshed AndroidPlatform
    // NEEDS to be done when other requests are not pending

    fun replacePlatform(myPlatform : AndroidPlatform) {
        MyEnvironment.replacePlatform( myPlatform )
    }

    fun parseCommand(cmdline: String) {
        // parse & handle command wrapped in coroutine
        viewModelScope.launch {
            val isReloop = FatashiWork.parseCommands( cmdline.trim().split(' ') )
            if (DEBUG) Log.d(LOG_TAG, ">>> parseCommand <<< isReloop: $isReloop")
        }  // launch
    }

    // ***************************************************************************************
    // ***************************************************************************************
    // functions below are test/debugging only.
    // ***************************************************************************************
    // ***************************************************************************************

    fun getKamusiFormatJson(
        f: String,
        onSuccess: (KamusiFormat) -> KamusiFormat,
        onFail: (String) -> Unit
    ) {
        if (DEBUG) Log.d(LOG_TAG, ">>> getKFJson <<< f: $f")

        val kamusiFormatType = object : TypeToken<KamusiFormat>() {}.type

        // wrap fileread in a scope
        viewModelScope.launch {
            try {
                onSuccess(
                    Gson().fromJson((getFile(f) as String), kamusiFormatType)
                )
            }  // try
            catch(ex: JsonSyntaxException) {
                Log.e(LOG_TAG, ex.toString())
                onFail(ex.toString())
            } // catch

        } // scope
    }

        // getFile  -- inputs a textfile in Dispatchers.IO space
    suspend fun getFile(f: String) : String {
            var result = ""

        withContext(Dispatchers.IO) {
            try {
                result = File(DEFAULT_PATH + f).readText()
            } catch (ex: IOException) {
                Log.e(LOG_TAG, "file: $f ex: $ex")
            } // catch
        }
        if (DEBUG) Log.d(LOG_TAG, ">>> getFile <<< f: $f \njson: $result")

        return result
    }

}  // class KamusiViewModel