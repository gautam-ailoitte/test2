package com.example.test2

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import com.example.test2.utlis.BatteryInfo
import com.example.test2.utlis.PermissionUtils
import com.example.test2.utlis.ScreenTimeInfo
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel

@RequiresApi(Build.VERSION_CODES.Q)
class MethodChannelManager(private val context: Context,  private val activity: Activity,flutterEngine: FlutterEngine) {
    private val CHANNEL = "test/native"
    private lateinit var roleRequestLauncher: ActivityResultLauncher<Intent>

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

    }
    }


