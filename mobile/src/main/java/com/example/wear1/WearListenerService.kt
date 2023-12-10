package com.example.wear1

import android.content.Intent
import android.util.Log
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.WearableListenerService
class WearListenerService : WearableListenerService() {

    override fun onMessageReceived(messageEvent: MessageEvent) {
        Log.d(TAG, "EL TELEFONO RECIBIO EL MENSAJEEEEE: $messageEvent")
        Log.d(TAG, String(messageEvent.data))
        if (messageEvent.path == MESSAGE_PATH) {
            val startIntent = Intent(this, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                putExtra("MessageData", messageEvent.data)
            }
            startActivity(startIntent)
        }
    }

    companion object{
        private const val TAG = "WearListenerService"
        private const val MESSAGE_PATH = "/deploy"
    }
}