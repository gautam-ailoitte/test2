package com.example.test2

import android.os.Build.VERSION_CODES
import androidx.annotation.RequiresApi
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine

class MainActivity : FlutterActivity() {
    private lateinit var methodChannelManager: MethodChannelManager


    @RequiresApi(VERSION_CODES.Q)
    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        methodChannelManager = MethodChannelManager(this,activity, flutterEngine)
    }
}
