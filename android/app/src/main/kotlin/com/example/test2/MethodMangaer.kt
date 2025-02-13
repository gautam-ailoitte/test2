package com.example.test2

import android.app.Activity
import android.content.Context
import android.content.Context.TELECOM_SERVICE
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.app.role.RoleManager

import android.telecom.TelecomManager
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.test2.utlis.BatteryInfo
import com.example.test2.utlis.PermissionUtils
import com.example.test2.utlis.ScreenTimeInfo
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel

@RequiresApi(Build.VERSION_CODES.Q)
class MethodChannelManager(private val context: Context,  private val activity: Activity,flutterEngine: FlutterEngine) {
    private val CHANNEL = "test/native"


    init {
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL).setMethodCallHandler { call, result ->
            when (call.method) {
                "getBatteryLevel" -> result.success(BatteryInfo.getBatteryLevel(context))
                "getScreenTimeData" -> result.success(ScreenTimeInfo.getScreenTimeData(context))
                "isUsageAccessGranted" -> result.success(PermissionUtils.isUsageAccessGranted(context))
                "openUsageAccessSettings" -> {
                    PermissionUtils.openUsageAccessSettings(context)
                    result.success(null)
                }
                "isDefaultDialer" -> result.success(isDefaultDialer())
                "setDefaultDialer" -> {
                    requestCallScreeningRole(result)
                    setDefaultDialer()
//                    openCallScreeningSettings()
                    result.success("Request to set default dialer sent.")
                }
                else -> result.notImplemented()
            }
        }

    }
    // check is Default dialer
    private fun isDefaultDialer(): Boolean {
        val telecomManager = context.getSystemService(TELECOM_SERVICE) as TelecomManager
        val packageName = context.packageName
        return telecomManager.defaultDialerPackage == packageName
    }
    // set as default dialer
    private fun setDefaultDialer() {
        val telecomManager = context.getSystemService(TELECOM_SERVICE) as TelecomManager

        if (telecomManager.defaultDialerPackage != context.packageName) {
            Log.d("MethodChannelManager", "Attempting to set app as default dialer...")

            val intent = Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER).apply {
                putExtra(TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, context.packageName)
            }
            try {
                activity.startActivityForResult(intent, 1001) // ✅ Must use `activity`, not `context`
                Log.d("MethodChannelManager", "Default dialer request sent successfully.")
            } catch (e: Exception) {
                Log.e("MethodChannelManager", "Failed to start default dialer intent: ${e.message}")
            }
        } else {
            Log.d("MethodChannelManager", "App is already the default dialer.")
        }
    }

    private fun openCallScreeningSettings() {
        val intent = Intent(Settings.ACTION_MANAGE_DEFAULT_APPS_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    // request role
    @RequiresApi(Build.VERSION_CODES.Q)
    private fun requestCallScreeningRole(result: MethodChannel.Result) {
        val roleManager = context.getSystemService(RoleManager::class.java)

        if (roleManager != null && roleManager.isRoleAvailable(RoleManager.ROLE_CALL_SCREENING)) {
            if (!roleManager.isRoleHeld(RoleManager.ROLE_CALL_SCREENING)) {
                Log.d("MethodChannelManager", "Requesting Call Screening Role...")
                val intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_CALL_SCREENING)
                activity.startActivityForResult(intent, 1002)
                result.success(null)
            } else {
                Log.d("MethodChannelManager", "App already has Call Screening Role.")
                result.success(true)
            }
        } else {
            Log.e("MethodChannelManager", "RoleManager is unavailable on this device.")
            result.error("UNAVAILABLE", "RoleManager not supported on this Android version.", null)
        }
    }

}





