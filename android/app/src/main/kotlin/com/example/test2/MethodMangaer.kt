package com.example.test2

import android.content.Context
import com.example.test2.utlis.BatteryInfo
import com.example.test2.utlis.PermissionUtils
import com.example.test2.utlis.ScreenTimeInfo
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel

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
                    else -> result.notImplemented()
            }
        }
    }}