package org.umoja4life.basicio

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.umoja4life.fatashi.BuildConfig
import org.umoja4life.fatashi.R


const val READ_PERMISSION_CODE  =  9     // permission request code
const val WRITE_PERMISSION_CODE = 10     // permission request code

const val DEFAULT_PATH = "/sdcard/Download/"
const val DOWNLOAD_DIR = "/Download/"

class FileServices {

    companion object {  // singleton to handle permissions

        var readPermission: Boolean = false
        var writePermission: Boolean = false

        // hasReadPermission -- returns TRUE if we have/got Read permission
        fun hasReadPermission(compatActivity: AppCompatActivity): Boolean {

            // ----- V0_1 --------------------------------------------------------------
            if ( BuildConfig.FATASHI_MY_VERSION == BuildConfig.FATASHI_V0_1 ) {

                if ( compatActivity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED ) {
                    readPermission = true
                }
                else {
                    showWhyGetPermission(
                        compatActivity,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        READ_PERMISSION_CODE)
                }
            }
            // ----- V0_2 --------------------------------------------------------------
            else {

            }
            // -------------------------------------------------------------------------

            return readPermission
        }

        // hasWritePermission -- returns TRUE if we have/got write permission
        fun hasWritePermission(compatActivity: AppCompatActivity): Boolean {
            if ( compatActivity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
                writePermission = true
            }
            else {
                showWhyGetPermission(
                    compatActivity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    WRITE_PERMISSION_CODE)
            }
            return writePermission
        }

        // showWhyGetPermission  -- if needed, shows rationale for request
        // stops to ask permission if user wants to proceed
        private fun showWhyGetPermission(compatActivity: AppCompatActivity, pType: String, pCode: Int) {
            if (shouldShowRequestPermissionRationale( compatActivity,pType )) {

                MaterialAlertDialogBuilder(compatActivity)
                    .setTitle( compatActivity.getString(R.string.permission_rationale) )
                    .setMessage( compatActivity.getString(R.string.permission_reason) )
                    .setPositiveButton(compatActivity.getString(R.string.yes_proceed)) { dialog, _ ->
                        dialog.dismiss()
                        getPermission(compatActivity, pType, pCode)  // request permissions
                    }
                    .show()
            }
            else {
                getPermission(compatActivity, pType, pCode)  // request permissions
            }
        }

        // getPermission -- initiative asynch grant-me-permission request;
        // CALLBACK expected in MainActivity!
        private fun getPermission(compatActivity: AppCompatActivity, pType: String, pCode: Int) {
            ActivityCompat.requestPermissions(compatActivity, arrayOf(pType), pCode)
        }


        // try to determine external public storage path
        fun dynamicExternalDownloadPath() : String? {
            if ( System.getenv("EXTERNAL_STORAGE").isNullOrBlank() ) return null
            return System.getenv("EXTERNAL_STORAGE")!! + DOWNLOAD_DIR
        }

        // Checks if a volume containing external storage is available to at least read.
        fun isExternalStorageReadable(): Boolean {
            return Environment.getExternalStorageState() in
                    setOf(Environment.MEDIA_MOUNTED, Environment.MEDIA_MOUNTED_READ_ONLY)
        }

       /* fun listDir(myContext: Context) {
            // val dynaPaths = Context.getExternalFilesDirs(Environment.DIRECTORY_DOWNLOADS)

        }*/

        fun getMyFilePath(myContext: Context) : String = dynamicExternalDownloadPath() ?: DEFAULT_PATH


    }  // companion object

}  // class FileServices
