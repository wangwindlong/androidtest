package net.wangyl.test.perm

import android.Manifest.permission.*
import android.os.Build
import androidx.annotation.RequiresApi
import net.wangyl.test.perm.Perms.BLUETOOTH_PERMS

sealed class Permission(vararg val permissions: String) {
    // Individual permissions
    @RequiresApi(Build.VERSION_CODES.S)
    object BLE : Permission(BLUETOOTH_CONNECT)

    // Bundled permissions
    object MandatoryForFeatureOne : Permission(WRITE_EXTERNAL_STORAGE, ACCESS_FINE_LOCATION)
    object Camera : Permission(WRITE_EXTERNAL_STORAGE, CAMERA)
    object Audio : Permission(WRITE_EXTERNAL_STORAGE, RECORD_AUDIO)

    // Grouped permissions
    object Location : Permission(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION)
    object Storage : Permission(WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE)
    object Phone : Permission(READ_PHONE_STATE, READ_EXTERNAL_STORAGE)


    companion object {
        fun from(permission: String) = when (permission) {
            ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION -> Location
            WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE -> Storage
            READ_PHONE_STATE, CALL_PHONE, READ_CALL_LOG -> Phone
            CAMERA -> Camera
            RECORD_AUDIO -> Audio
            BLUETOOTH_PERMS -> BLE
            else -> throw IllegalArgumentException("Unknown permission: $permission")
        }
    }
}