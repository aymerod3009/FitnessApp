package com.qrolic.fitnessapp.myUtils

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.qrolic.fitnessapp.R
import com.qrolic.fitnessapp.activity.BottomNavigationActivity
import com.qrolic.fitnessapp.database.MySharedPreferences
import java.util.*


class MyBroadCastReciver: BroadcastReceiver() {
    @SuppressWarnings("kotlin:S3776")
    //onReceive() method have cognitive complexity
    override fun onReceive(p0: Context, p1: Intent) {

            if (p1.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
                val serviceIntent = Intent(p0, NotificationService::class.java)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    p0.startForegroundService(serviceIntent)
                } else {
                    p0.startService(serviceIntent)
                }
            } else {
                val cal = Calendar.getInstance()

                var dayofWeek = cal.get(Calendar.DAY_OF_WEEK)
                var mySharedPreferences = MySharedPreferences(p0)
                if (dayofWeek == 2 && mySharedPreferences.getMonday()) {
                    notificationShow(p0)
                } else if (dayofWeek == 3 && mySharedPreferences.getTuesday()) {
                    notificationShow(p0)
                } else if (dayofWeek == 4 && mySharedPreferences.getWednesday()) {
                    notificationShow(p0)
                } else if (dayofWeek == 5 && mySharedPreferences.getThursday()) {
                    notificationShow(p0)
                } else if (dayofWeek == 6 && mySharedPreferences.getFriday()) {
                    notificationShow(p0)
                } else if (dayofWeek == 7 && mySharedPreferences.getSaturday()) {
                    notificationShow(p0)
                } else if (dayofWeek == 1 && mySharedPreferences.getSunday()) {
                    notificationShow(p0)
                }

            }
            }
    }

    private fun notificationShow(
        p0: Context
    ){

        var startIntent = Intent(p0, BottomNavigationActivity::class.java)
        startIntent.flags =
            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        var startPendingIntent = PendingIntent.getActivity(p0, 0, startIntent, 0)

        var builder = NotificationCompat.Builder(p0, "noti")
            .setSmallIcon(R.drawable.f)
            .setContentTitle("Fitness App")
            .setContentText("start your workout now")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .addAction(R.drawable.f, "START NOW", startPendingIntent)
            .setColor(Color.parseColor("#E91E63"))

        var notificationManager = NotificationManagerCompat.from(p0)

        notificationManager.notify(200, builder.build())
    }



