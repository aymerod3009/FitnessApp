package com.qrolic.fitnessapp.myUtils

import android.app.ActivityManager
import android.app.ActivityManager.RunningAppProcessInfo
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.text.Html
import android.text.TextUtils
import android.util.Patterns
import androidx.core.app.NotificationCompat
import com.qrolic.fitnessapp.R
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.text.ParseException
import java.text.SimpleDateFormat

class FirebaseNotificationUtils(mContext: Context) {

    val NOTIFICATION_ID = 100
    val NOTIFICATION_ID_BIG_IMAGE = 101
    private var mContext: Context? = mContext

    fun showNotificationMessage(
        title: String?,
        message: String?,
        timeStamp: String?,
        intent: Intent
    ) {
        showNotificationMessage(title, message, timeStamp, intent, null)
    }

    fun showNotificationMessage(
        title: String?,
        message: String?,
        timeStamp: String?,
        intent: Intent,
        imageUrl: String?
    ) {
        // Check for empty push message
        if (TextUtils.isEmpty(message)) return


        // notification icon
        val icon: Int = R.mipmap.ic_launcher
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val resultPendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)
        val mBuilder =
            NotificationCompat.Builder(
                mContext
            )
        val alarmSound = Uri.parse(
            ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + mContext!!.packageName + "/raw/notification"
        )
        if (!TextUtils.isEmpty(imageUrl)) {
            if (imageUrl != null && imageUrl.length > 4 && Patterns.WEB_URL.matcher(imageUrl)
                    .matches()
            ) {
                val bitmap = getBitmapFromURL(imageUrl)
                if (bitmap != null) {
                    showBigNotification(
                        bitmap,
                        mBuilder,
                        icon,
                        title,
                        message,
                        timeStamp,
                        resultPendingIntent,
                        alarmSound
                    )
                } else {
                    showSmallNotification(
                        mBuilder,
                        icon,
                        title,
                        message,
                        timeStamp,
                        resultPendingIntent,
                        alarmSound
                    )
                }
            }
        } else {
            showSmallNotification(
                mBuilder,
                icon,
                title,
                message,
                timeStamp,
                resultPendingIntent,
                alarmSound
            )
            playNotificationSound()
        }
    }


    private fun showSmallNotification(
        mBuilder: NotificationCompat.Builder,
        icon: Int,
        title: String?,
        message: String?,
        timeStamp: String?,
        resultPendingIntent: PendingIntent,
        alarmSound: Uri
    ) {
        val inboxStyle =
            NotificationCompat.InboxStyle()
        inboxStyle.addLine(message)
        val notification: Notification
        notification = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
            .setAutoCancel(true)
            .setContentTitle(title)
            .setContentIntent(resultPendingIntent)
            .setSound(alarmSound)
            .setStyle(inboxStyle)
            .setWhen(getTimeMilliSec(timeStamp))
            .setSmallIcon(R.mipmap.ic_launcher)
            .setLargeIcon(BitmapFactory.decodeResource(mContext!!.resources, icon))
            .setContentText(message)
            .build()
        val notificationManager =
            mContext!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    @SuppressWarnings("kotlin:S107")
    //showBigNotification() have too many parameters
    private fun showBigNotification(
        bitmap: Bitmap,
        mBuilder: NotificationCompat.Builder,
        icon: Int,
        title: String?,
        message: String?,
        timeStamp: String?,
        resultPendingIntent: PendingIntent,
        alarmSound: Uri
    ) {
        val bigPictureStyle =
            NotificationCompat.BigPictureStyle()
        bigPictureStyle.setBigContentTitle(title)
        bigPictureStyle.setSummaryText(Html.fromHtml(message).toString())
        bigPictureStyle.bigPicture(bitmap)
        val notification: Notification
        notification = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
            .setAutoCancel(true)
            .setContentTitle(title)
            .setContentIntent(resultPendingIntent)
            .setSound(alarmSound)
            .setStyle(bigPictureStyle)
            .setWhen(getTimeMilliSec(timeStamp))
            .setSmallIcon(R.mipmap.ic_launcher)
            .setLargeIcon(BitmapFactory.decodeResource(mContext!!.resources, icon))
            .setContentText(message)
            .build()
        val notificationManager =
            mContext!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID_BIG_IMAGE, notification)
    }

    fun getBitmapFromURL(strURL: String?): Bitmap? {
        return try {
            val url = URL(strURL)
            val connection =
                url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            BitmapFactory.decodeStream(input)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    // Playing notification sound
    fun playNotificationSound() {
        try {
            val alarmSound = Uri.parse(
                ContentResolver.SCHEME_ANDROID_RESOURCE
                        + "://" + mContext!!.packageName + "/raw/notification"
            )
            val r = RingtoneManager.getRingtone(mContext, alarmSound)
            r.play()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @SuppressWarnings("kotlin:S3776")
    //isAppIsInBackground() method have cognitive complexity
    fun isAppIsInBackground(context: Context): Boolean {
        var isInBackground = true
        val am =
            context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            val runningProcesses =
                am.runningAppProcesses
            for (processInfo in runningProcesses) {
                if (processInfo.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (activeProcess in processInfo.pkgList) {
                        if (activeProcess == context.packageName) {
                            isInBackground = false
                        }
                    }
                }
            }
        } else {
            val taskInfo = am.getRunningTasks(1)
            val componentInfo = taskInfo[0].topActivity
            if (componentInfo!!.packageName == context.packageName) {
                isInBackground = false
            }
        }
        return isInBackground
    }


    fun getTimeMilliSec(timeStamp: String?): Long {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        try {
            val date = format.parse(timeStamp)
            return date.time
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return 0
    }
}