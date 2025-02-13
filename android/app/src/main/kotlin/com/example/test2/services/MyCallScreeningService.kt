package com.example.test2.services

import android.os.Build
import androidx.annotation.RequiresApi
import android.telecom.Call
import android.telecom.CallScreeningService
import android.util.Log
import com.example.test2.utlis.AudioUtils
import com.example.test2.utlis.ContactUtils
//
// this service code is written in manifest so it automatically gets registered
@RequiresApi(Build.VERSION_CODES.N)
class MyCallScreeningService : CallScreeningService() {

    override fun onScreenCall(callDetails: Call.Details) {
        val incomingNumber = callDetails.handle.schemeSpecificPart
        Log.d("CallScreening", "Incoming call from: $incomingNumber")

        if (ContactUtils.isTrustedContact(contentResolver, incomingNumber)) {
            if (ContactUtils.isDadCall(incomingNumber)) {
                AudioUtils.ringEvenInSilentMode(this)
            }
            allowCall(callDetails)
        } else {
            blockCall(callDetails)
        }
    }


    private fun allowCall(callDetails: Call.Details) {
        val response = CallResponse.Builder()
            .setDisallowCall(false)
            .setRejectCall(false)
            .setSkipCallLog(false)
            .setSkipNotification(false)
            .build()
        respondToCall(callDetails, response)
    }

    private fun blockCall(callDetails: Call.Details) {
        val response = CallResponse.Builder()
            .setDisallowCall(true)
            .setRejectCall(true)
            .setSkipCallLog(true)
            .setSkipNotification(true)
            .build()
        respondToCall(callDetails, response)
    }
}
