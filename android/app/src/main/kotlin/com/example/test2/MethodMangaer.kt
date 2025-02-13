package com.example.test2

import android.app.Activity
import android.content.Context
import android.content.Context.TELECOM_SERVICE
import android.content.Intent
import android.os.Build

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
                    setDefaultDialer()
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
            val intent = Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER).apply {
                putExtra(TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, context.packageName)
            }
            context.startActivity(intent)
            Log.d("MainActivity", "Requested to be set as default dialer.")
        }
    }
}


