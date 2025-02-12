package com.example.test2

import android.app.Activity
import android.app.role.RoleManager
import android.content.Context
import android.content.Intent
import android.os.Build

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import com.example.test2.services.MyCallScreeningService
import com.example.test2.utlis.BatteryInfo
import com.example.test2.utlis.PermissionUtils
import com.example.test2.utlis.ScreenTimeInfo
import io.flutter.embedding.android.FlutterActivity
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
                "requestCallScreeningRole" -> requestCallScreeningRole(result)

                else -> result.notImplemented()
            }
        }
        roleRequestLauncher = (activity as FlutterActivity).registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // App is now the call screening app
                MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL).invokeMethod("onRoleGranted", true)
            } else {
                // App is NOT the call screening app
                MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL).invokeMethod("onRoleGranted", false)
            }
        }
    }
    }
    // Initialize ActivityResultLauncher for role request


private fun requestCallScreeningRole(result: MethodChannel.Result) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val roleManager = context.getSystemService(RoleManager::class.java)
        if (roleManager?.isRoleAvailable(RoleManager.ROLE_CALL_SCREENING) == true) {
            val intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_CALL_SCREENING)
            roleRequestLauncher.launch(intent)
            result.success(null)
            return
        }
    }
    result.error("UNAVAILABLE", "RoleManager is not available", null)
}
}