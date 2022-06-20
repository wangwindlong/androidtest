package net.wangyl.test.perm

import android.Manifest
import android.Manifest.permission.BLUETOOTH_CONNECT
import android.os.Build
import androidx.annotation.RequiresApi

object Perms {

    val BLUETOOTH_PERMS = BLUETOOTH_CONNECT
    val STORAGE_AND_CAMERA = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.CAMERA
    )
}