package com.example.test2.utlis

import android.app.usage.UsageStatsManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import java.util.*

object ScreenTimeInfo {
    fun getScreenTimeData(context: Context): Map<String, Long> {
        val usageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val endTime = System.currentTimeMillis()
        val startTime = endTime - 24 * 60 * 60 * 1000 // Last 24 hours

        val usageStatsList = usageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY, startTime, endTime
        )
        println(usageStatsList)

        val screenTimeMap = mutableMapOf<String, Long>()
        usageStatsList?.forEach {
            screenTimeMap[it.packageName] = it.totalTimeInForeground
        }

        return screenTimeMap
    }
}
