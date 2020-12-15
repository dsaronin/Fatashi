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

//  ***** ENTIRE FILE IS DEPRECATED; DO NOT USE ************************************************
//  ***** ENTIRE FILE IS DEPRECATED; DO NOT USE ************************************************
//  ***** ENTIRE FILE IS DEPRECATED; DO NOT USE ************************************************

const val READ_PERMISSION_CODE  =  9     // permission request code
const val WRITE_PERMISSION_CODE = 10     // permission request code

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

    }  // companion object

}  // class FileServices
