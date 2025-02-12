package com.example.test2

import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.telecom.TelecomManager
import androidx.annotation.RequiresApi
import com.example.test2.services.MyCallScreeningService
import com.example.test2.utlis.BatteryInfo
import com.example.test2.utlis.PermissionUtils
import com.example.test2.utlis.ScreenTimeInfo
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel

@RequiresApi(Build.VERSION_CODES.Q)
class MethodChannelManager(private val context: Context, flutterEngine: FlutterEngine) {
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
                "isCallScreeningEnabled" -> result.success(isCallScreeningEnabled())
                "openCallScreeningSettings" -> {
                    openCallScreeningSettings()
                    result.success(null)
                }
                    else -> result.notImplemented()
            }
        }

    }
    private fun isCallScreeningEnabled(): Boolean {
        val telecomManager = context.getSystemService(TelecomManager::class.java)
        return telecomManager.defaultDialerPackage == context.packageName
    }

    private fun openCallScreeningSettings() {
        val intent = Intent(Settings.ACTION_MANAGE_DEFAULT_APPS_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}