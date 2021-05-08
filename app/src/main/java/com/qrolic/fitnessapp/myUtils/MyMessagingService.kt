package com.qrolic.fitnessapp.myUtils


import android.content.Intent

import android.text.TextUtils
import android.util.Log

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.qrolic.fitnessapp.activity.BottomNavigationActivity
import org.json.JSONException
import org.json.JSONObject


class MyMessagingService :FirebaseMessagingService() {

    private val TAG: String = MyMessagingService::class.java.getSimpleName()
    private lateinit var firebaseNotificationUtils: FirebaseNotificationUtils

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        if (remoteMessage == null) return

        
        // Check if message contains a data payload.
        if (remoteMessage.data.size > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.data)

            try {
                val json = JSONObject(remoteMessage.data as Map<*, *>)
                handleDataMessage(json)
            } catch (e: Exception) {
                Log.e(
                    TAG,
                    "Exception: " + e.message
                )
            }
        }
        super.onMessageReceived(remoteMessage)
    }



    private fun handleDataMessage(json: JSONObject) {
        Log.e(TAG, "push json: $json")
        try {
            val title = json.getString("title")
            val message = json.getString("message")
            val isBackground = json.getString("is_background")
            val imageUrl = json.getString("image")
            val timestamp = json.getString("timestamp")
            Log.e(TAG, "title: $title")
            Log.e(TAG, "message: $message")
            Log.e(TAG, "isBackground: $isBackground")
            Log.e(TAG, "imageUrl: $imageUrl")
            Log.e(TAG, "timestamp: $timestamp")

            firebaseNotificationUtils = FirebaseNotificationUtils(this)
            firebaseNotificationUtils.playNotificationSound()

            if (!firebaseNotificationUtils.isAppIsInBackground(applicationContext)) {
                // code for app is in foreground

            } else {
                val resultIntent =
                    Intent(applicationContext, BottomNavigationActivity::class.java)
                resultIntent.putExtra("message", message)

                // check for image attachment
                if (TextUtils.isEmpty(imageUrl)) {
                    showNotificationMessage1(
                        title,
                        message,
                        timestamp,
                        resultIntent
                    )
                } else {
                    // image is present, show notification with image
                    showNotificationMessageWithBigImage(
                        title,
                        message,
                        timestamp,
                        resultIntent,
                        imageUrl
                    )
                }
            }
        } catch (e: JSONException) {
            Log.e(
               TAG,
                "Json Exception: " + e.message
            )
        } catch (e: java.lang.Exception) {
            Log.e(
                TAG,
                "Exception: " + e.message
            )
        }
    }

    private fun showNotificationMessage1(
        title: String,
        message: String,
        timeStamp: String,
        intent: Intent
    ) {
        firebaseNotificationUtils.showNotificationMessage(title, message, timeStamp, intent)
    }

    /**
     * Showing notification with text and image
     */
    private fun showNotificationMessageWithBigImage(
        title: String,
        message: String,
        timeStamp: String,
        intent: Intent,
        imageUrl: String
    ) {
        firebaseNotificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl)
    }
}