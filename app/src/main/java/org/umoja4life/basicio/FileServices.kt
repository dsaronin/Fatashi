package org.umoja4life.basicio

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
                    .setPositiveButton(compatActivity.getString(R.string.yes_proceed)) { dialog, which ->
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
            return System.getenv("EXTERNAL_STORAGE") + DOWNLOAD_DIR
        }

        // Checks if a volume containing external storage is available to at least read.
        fun isExternalStorageReadable(): Boolean {
            return Environment.getExternalStorageState() in
                    setOf(Environment.MEDIA_MOUNTED, Environment.MEDIA_MOUNTED_READ_ONLY)
        }

        fun listDir(myContext: Context) {
            // val dynaPaths = Context.getExternalFilesDirs(Environment.DIRECTORY_DOWNLOADS)

        }

        fun getMyFilePath(myContext: Context) : String = dynamicExternalDownloadPath() ?: DEFAULT_PATH


    }  // companion object

}  // class FileServices

/*
If your app does not have the requested permissions the user will be presented with UI for
accepting them. After the user has accepted or rejected the requested permissions you will
receive a callback reporting whether the permissions were granted or not. Your activity has
to implement ActivityCompat.OnRequestPermissionsResultCallback and the results of permission
requests will be delivered to its
ActivityCompat.OnRequestPermissionsResultCallback.onRequestPermissionsResult(int, String[], int[])
method.

Note that requesting a permission does not guarantee it will be granted and your app should
be able to run without having this permission.

This method may start an activity allowing the user to choose which permissions to grant and
which to reject. Hence, you should be prepared that your activity may be paused and resumed.
Further, granting some permissions may require a restart of you application. In such a case,
the system will recreate the activity stack before delivering the result to your
ActivityCompat.OnRequestPermissionsResultCallback.onRequestPermissionsResult(int, String[], int[]).
 */
/*
Android docs say this check is no longer needed:
    if (Build.VERSION.SDK_INT < 23) {
        readPermission = true
    } else {
    }

 */