package org.umoja4life.basicio

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

const val READ_PERMISSION_CODE  =  9     // permission request code
const val WRITE_PERMISSION_CODE = 10     // permission request code


class FileServices {

    private var readPermission: Boolean = false
    private var writePermission: Boolean = false

    fun hasReadPermission(ctx: Context, atividade: AppCompatActivity): Boolean {
        if (ContextCompat.checkSelfPermission(ctx, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {

            // shouldShowRequestPermissionRationale() ?
            ActivityCompat.requestPermissions(
                atividade, 
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 
                READ_PERMISSION_CODE
            )
        } else {
            readPermission = true
        }
        return readPermission
    }

    fun hasWritePermission(ctx: Context, atividade: AppCompatActivity): Boolean {
        if (ContextCompat.checkSelfPermission(ctx, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {

            // shouldShowRequestPermissionRationale() ?
            ActivityCompat.requestPermissions(
                atividade, 
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                WRITE_PERMISSION_CODE
            )
        } else {
            writePermission = true
        }
        return writePermission
    }

    
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