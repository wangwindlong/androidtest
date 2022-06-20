package net.wangyl.test.perm

import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import net.wangyl.test.MyApp
import net.wangyl.test.R
import java.lang.ref.WeakReference

class PermissionManager private constructor(private val caller: WeakReference<ActivityResultCaller>) {

    private val requiredPermissions = mutableListOf<Permission>()
    private var rationale: String? = null
    private var callback: (Boolean) -> Unit = {}
    private var detailedCallback: (Map<Permission,Boolean>) -> Unit = {}


    private val permissionCheck =
        caller.get()?.registerForActivityResult(RequestMultiplePermissions()) { grantResults ->
            sendResultAndCleanUp(grantResults)
        }

    companion object {
        fun from(caller: ActivityResultCaller) = PermissionManager(WeakReference(caller))
    }

    fun rationale(description: String): PermissionManager {
        rationale = description
        return this
    }

    fun request(vararg permission: Permission): PermissionManager {
        requiredPermissions.addAll(permission)
        return this
    }

    fun request(permissions: List<String>): PermissionManager {
        requiredPermissions.addAll(permissions.map { Permission.from(it) }.distinct())
        return this
    }
    fun request(permission: String): PermissionManager {
        requiredPermissions.add(Permission.from(permission))
        return this
    }

    fun checkPermission(callback: (Boolean) -> Unit) {
        this.callback = callback
        handlePermissionRequest()
    }

    fun checkDetailedPermission(callback: (Map<Permission,Boolean>) -> Unit) {
        this.detailedCallback = callback
        handlePermissionRequest()
    }

    private fun handlePermissionRequest() {
        caller.get()?.let { fragment ->
            when {
                areAllPermissionsGranted() -> sendPositiveResult()
                shouldShowPermissionRationale() -> displayRationale()
                else -> requestPermissions()
            }
        }
    }

    private fun displayRationale() {
        AlertDialog.Builder(when(caller.get()) {
            is Fragment -> (caller.get() as Fragment).context
            is Context -> caller.get() as Context
            else -> return
        })
            .setTitle(MyApp.getIdString(R.string.dialog_permission_title))
            .setMessage(rationale ?: MyApp.getIdString(R.string.dialog_permission_default_message))
            .setCancelable(false)
            .setPositiveButton(MyApp.getIdString(R.string.dialog_permission_button_positive)) { _, _ ->
                requestPermissions()
            }
            .show()
    }

    private fun sendPositiveResult() {
        sendResultAndCleanUp(getPermissionList().associate { it to true } )
    }

    private fun sendResultAndCleanUp(grantResults: Map<String, Boolean>) {
        callback(grantResults.all { it.value })
        detailedCallback(grantResults.mapKeys { Permission.from(it.key) })
        cleanUp()
    }

    private fun cleanUp() {
        requiredPermissions.clear()
        rationale = null
        callback = {}
        detailedCallback = {}
    }

    private fun requestPermissions() {
        permissionCheck?.launch(getPermissionList())
    }

    private fun areAllPermissionsGranted() =
        requiredPermissions.all { it.isGranted() }

    private fun shouldShowPermissionRationale() =
        requiredPermissions.any { it.requiresRationale() }

    private fun getPermissionList() =
        requiredPermissions.flatMap { it.permissions.toList() }.toTypedArray()

    private fun Permission.isGranted() =
        permissions.all { hasPermission(it) }

    private fun Permission.requiresRationale() =
        permissions.any {
            if (caller.get() is Fragment) {
                (caller.get() as Fragment).shouldShowRequestPermissionRationale(it)
            } else caller.get() is ComponentActivity
        }

    private fun hasPermission(permission: String) =
        ContextCompat.checkSelfPermission(
            MyApp.instance,
            permission
        ) == PackageManager.PERMISSION_GRANTED
}