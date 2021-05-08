package com.qrolic.fitnessapp.myUtils

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.Color
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.qrolic.fitnessapp.R
import com.qrolic.fitnessapp.activity.BottomNavigationActivity
import com.qrolic.fitnessapp.database.MySharedPreferences
import java.util.*


class NotificationService : Service() {
    override fun onBind(p0: Intent?): IBinder? {
        throw  UnsupportedOperationException("Not yet implemented");
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val cal = Calendar.getInstance()
        var dayofWeek = cal.get(Calendar.DAY_OF_WEEK)
        var mySharedPreferences = MySharedPreferences(this)
        if (dayofWeek == 2 && mySharedPreferences.getMonday()) {
            notificationShow()
        }else if(dayofWeek == 3 && mySharedPreferences.getTuesday()){
            notificationShow()
        }else if (dayofWeek == 4 && mySharedPreferences.getWednesday()){
            notificationShow()
        }else if (dayofWeek == 5 && mySharedPreferences.getThursday()){
            notificationShow()
        }else if (dayofWeek == 6 && mySharedPreferences.getFriday()){
            notificationShow()
        }else if (dayofWeek == 7 && mySharedPreferences.getSaturday()){
            notificationShow()
        }else if (dayofWeek == 1 && mySharedPreferences.getSunday()){
            notificationShow()
        }
        return START_STICKY
    }


    private fun notificationShow() {


        var startIntent = Intent(this, BottomNavigationActivity::class.java)
        startIntent.flags =
            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        var startPendingIntent = PendingIntent.getActivity(this, 0, startIntent, 0)

        var builder = NotificationCompat.Builder(this, "noti")
            .setSmallIcon(R.drawable.f)
            .setContentTitle("Fitness App")
            .setContentText("start your workout now")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .addAction(R.drawable.f, "START NOW", startPendingIntent)
            .setColor(Color.parseColor("#E91E63"))

        var notificationManager = NotificationManagerCompat.from(this)

        notificationManager.notify(200, builder.build())

    }

}