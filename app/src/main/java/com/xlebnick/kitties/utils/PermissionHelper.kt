package com.xlebnick.kitties.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import javax.inject.Inject

class PermissionHelper @Inject constructor(private val context: Context) {

    companion object {
        const val REQUEST_CODE_PERMISSIONS = 10
        val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }

    fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            context, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun onCameraPermissionGranted(requestCode: Int): Boolean {
        return requestCode == REQUEST_CODE_PERMISSIONS && allPermissionsGranted()
    }
}
