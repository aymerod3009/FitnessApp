package com.qrolic.fitnessapp.adapter

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

import com.qrolic.fitnessapp.R
import com.qrolic.fitnessapp.database.MySharedPreferences
import com.qrolic.fitnessapp.model.RemiderTable

import com.qrolic.fitnessapp.myUtils.MyBroadCastReciver
import java.util.*

internal class ReminderListAdapter(val context: Context,private var reminderList: List<RemiderTable>,
                                   val clickListener: OnTypeClickListner
) : RecyclerView.Adapter<ReminderListAdapter.MyViewHolder>() {


    open interface OnTypeClickListner {
        fun onRemoveItemClick(position: Int)
        fun onItemClick(position: Int)
    }
    internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var scReminder: SwitchCompat = view.findViewById(R.id.scReminder)
        var tvReminderTime: TextView = view.findViewById(R.id.tvReminderTime)
        var tvMonday: TextView = view.findViewById(R.id.tvMonday)
        var tvTuesday: TextView = view.findViewById(R.id.tvTuesday)
        var tvWednesday: TextView = view.findViewById(R.id.tvWednesday)
        var tvThursday: TextView = view.findViewById(R.id.tvThursday)
        var tvFriday: TextView = view.findViewById(R.id.tvFriday)
        var tvSaturday: TextView = view.findViewById(R.id.tvSaturday)
        var tvSunday: TextView = view.findViewById(R.id.tvSunday)
        var tvRemove: TextView = view.findViewById(R.id.tvRemove)
        var llReminder: LinearLayout = view.findViewById(R.id.llReminder)

    }

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.inflater_reminder_list, parent, false)
        return MyViewHolder(itemView)
    }

    @SuppressWarnings("kotlin:S3776")
    //onBindViewHolder() method have cognitive complexity
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val remindTable = reminderList[position]
        var mySharedPreferences = MySharedPreferences(context)
        holder.tvReminderTime.text = remindTable.time

        if (mySharedPreferences.getReminderSwitchEnable()!!){
            holder.scReminder.isChecked = true
        }else{
            holder.scReminder.isChecked = false
        }

        holder.scReminder.setOnCheckedChangeListener({ _, isChecked ->
            if (isChecked) {
                mySharedPreferences.setReminderSwitchEnabled(true)
                scheduleAlarm(mySharedPreferences.getHour().toString().toInt(),mySharedPreferences.getMinute().toString().toInt())
            } else {
                mySharedPreferences.setReminderSwitchEnabled(false)
                cancelAlarm()
            }
        })

        if (remindTable.monday){
            holder.tvMonday.background = ContextCompat.getDrawable(context,R.drawable.trans_pink_circle)
        }else{
            holder.tvMonday.background = ContextCompat.getDrawable(context,R.drawable.gray_bg)
        }

        if (remindTable.tuesday){
            holder.tvTuesday.background = ContextCompat.getDrawable(context,R.drawable.trans_pink_circle)
        }else{
            holder.tvTuesday.background =ContextCompat.getDrawable(context,R.drawable.gray_bg)
        }

        if (remindTable.wednesday){
            holder.tvWednesday.background = ContextCompat.getDrawable(context,R.drawable.trans_pink_circle)
        }else{
            holder.tvWednesday.background = ContextCompat.getDrawable(context,R.drawable.gray_bg)
        }

        if (remindTable.thursday){
            holder.tvThursday.background = ContextCompat.getDrawable(context,R.drawable.trans_pink_circle)
        }else{
            holder.tvThursday.background = ContextCompat.getDrawable(context,R.drawable.gray_bg)
        }

        if (remindTable.friday){
            holder.tvFriday.background = ContextCompat.getDrawable(context,R.drawable.trans_pink_circle)
        }else{
            holder.tvFriday.background = ContextCompat.getDrawable(context,R.drawable.gray_bg)
        }

        if (remindTable.saturday){
            holder.tvSaturday.background = ContextCompat.getDrawable(context,R.drawable.trans_pink_circle)
        }else{
            holder.tvSaturday.background = ContextCompat.getDrawable(context,R.drawable.gray_bg)
        }

        if (remindTable.sunday){
            holder.tvSunday.background = ContextCompat.getDrawable(context,R.drawable.trans_pink_circle)
        }else{
            holder.tvSunday.background = ContextCompat.getDrawable(context,R.drawable.gray_bg)
        }

        holder.tvRemove.setOnClickListener(View.OnClickListener { view ->
            clickListener.onRemoveItemClick(position)
        })

        holder.llReminder.setOnClickListener(View.OnClickListener { view ->
            clickListener.onItemClick(position)
        })

    }

    private fun scheduleAlarm(
        hour: Int,
        minute: Int
    ) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)

        // Check we aren't setting it in the past which would trigger it to fire instantly
        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 7)
        }

        notificationClick(calendar.timeInMillis)

    }


    private fun notificationClick(
        millies: Long

    ) {
        var intent = Intent(context, MyBroadCastReciver::class.java)
        intent.putExtra("trigger time",millies)
        var pendingIntent =
            PendingIntent.getBroadcast(
                context,
                0,
                intent,
                0
            )
        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, millies, pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            am.setExact(AlarmManager.RTC_WAKEUP, millies, pendingIntent);
        } else {
            am.set(AlarmManager.RTC_WAKEUP, millies, pendingIntent);
        }


    }

    private fun cancelAlarm() {
        val alarmManager2 =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val pendingIntent2 = PendingIntent.getBroadcast(
            context, 0, Intent(
                context,
                MyBroadCastReciver::class.java
            ), 0
        )
        alarmManager2.cancel(pendingIntent2)


    }
    override fun getItemCount(): Int {
        return reminderList.size
    }
}