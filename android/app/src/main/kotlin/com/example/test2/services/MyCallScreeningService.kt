package com.example.test2.services

import android.os.Build
import androidx.annotation.RequiresApi
import android.telecom.Call
import android.telecom.CallScreeningService
import android.util.Log
import com.example.test2.utlis.AudioUtils
import com.example.test2.utlis.ContactUtils

@RequiresApi(Build.VERSION_CODES.N)
class MyCallScreeningService : CallScreeningService() {

    override fun onScreenCall(callDetails: Call.Details) {
        val incomingNumber = callDetails.handle.schemeSpecificPart
        Log.d("CallScreening", "Incoming call detected: $incomingNumber")

        if (incomingNumber.isNullOrEmpty()) {
            Log.e("CallScreening", "Error: Incoming number is null!")
            return
        }

        if (ContactUtils.isTrustedContact(contentResolver, incomingNumber)) {
            Log.d("CallScreening", "Allowing call: $incomingNumber")
            if (ContactUtils.isDadCall(incomingNumber)) {
                Log.d("CallScreening", "Dad's call detected! Ringing even in silent mode.")
                AudioUtils.ringEvenInSilentMode(this)
            }
            allowCall(callDetails)
        } else {
            Log.d("CallScreening", "Blocking call: $incomingNumber")
            blockCall(callDetails)
        }
    }

    private fun allowCall(callDetails: Call.Details) {
        Log.d("CallScreening", "Sending response to allow call.")
        val response = CallResponse.Builder()
            .setDisallowCall(false)
            .setRejectCall(false)
            .setSkipCallLog(false)
            .setSkipNotification(false)
            .build()
        respondToCall(callDetails, response)
    }

    private fun blockCall(callDetails: Call.Details) {
        Log.d("CallScreening", "Sending response to block call.")
        val response = CallResponse.Builder()
            .setDisallowCall(true)
            .setRejectCall(true)
            .setSkipCallLog(true)
            .setSkipNotification(true)
            .build()
        respondToCall(callDetails, response)
    }
}
