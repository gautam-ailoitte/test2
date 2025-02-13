package com.example.test2.utlis



import android.content.ContentValues.TAG
import android.content.Context
import android.media.AudioManager
import android.util.Log

object AudioUtils {
    fun ringEvenInSilentMode(context: Context) {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audioManager.ringerMode = AudioManager.RINGER_MODE_NORMAL
        Log.d(TAG, "Ringer mode set to normal for Dad's call.")
        audioManager.setStreamVolume(
            AudioManager.STREAM_RING,
            audioManager.getStreamMaxVolume(AudioManager.STREAM_RING),
            0
        )
    }
}
