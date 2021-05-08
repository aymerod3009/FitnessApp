package com.qrolic.fitnessapp.activity

import android.app.AlarmManager
import android.app.Dialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
//import com.google.android.gms.ads.AdListener
//import com.google.android.gms.ads.AdLoader
//import com.google.android.gms.ads.AdRequest
//import com.google.android.gms.ads.LoadAdError
//import com.google.android.gms.ads.nativead.MediaView
//import com.google.android.gms.ads.nativead.NativeAd
//import com.google.android.gms.ads.nativead.NativeAdView
import com.qrolic.fitnessapp.R
import com.qrolic.fitnessapp.adapter.ReminderListAdapter
import com.qrolic.fitnessapp.database.FitnessDatabase
import com.qrolic.fitnessapp.database.MySharedPreferences
import com.qrolic.fitnessapp.databinding.ActivityReminderBinding
import com.qrolic.fitnessapp.model.RemiderTable
import com.qrolic.fitnessapp.myUtils.MyBroadCastReciver
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class ReminderActivity : AppCompatActivity() {

    private lateinit var s_intentFilter: IntentFilter
    private var reminderList: ArrayList<RemiderTable> = ArrayList<RemiderTable>()
    private lateinit var reminderListAdapter: ReminderListAdapter
    var TAG = "ReminderActivity"
    lateinit var db: FitnessDatabase
    lateinit var mySharedPreferences: MySharedPreferences
    lateinit var myBroadCastReciver: MyBroadCastReciver
//    lateinit var nativeAd: NativeAd
    lateinit var activityReminderBinding:ActivityReminderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityReminderBinding=DataBindingUtil.setContentView(this,R.layout.activity_reminder)
        initAll()
    }

    private fun initAll() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle("Reminder")
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#26022C")))

        refreshAd()

        db = FitnessDatabase.invoke(this)
        mySharedPreferences = MySharedPreferences(this)
        myBroadCastReciver = MyBroadCastReciver()
        s_intentFilter = IntentFilter()
        s_intentFilter.addAction(Intent.ACTION_DATE_CHANGED)
        s_intentFilter.addAction(Intent.ACTION_TIMEZONE_CHANGED)


        reminderListAdapter = ReminderListAdapter(this,
            reminderList,
            object : ReminderListAdapter.OnTypeClickListner {
                override fun onItemClick(position1: Int) {
                    updateDateTime(reminderList.get(position1))
                }

                override fun onRemoveItemClick(position: Int) {
                    removeReminder((position))
                }
            })
        val layoutManager = LinearLayoutManager(applicationContext)

        activityReminderBinding.rvReminder.layoutManager = layoutManager
        activityReminderBinding.rvReminder.itemAnimator = DefaultItemAnimator()
        activityReminderBinding.rvReminder.adapter = reminderListAdapter


        activityReminderBinding.ivAddReminder.setOnClickListener { onAddReminderClick() }
        getReminderData()
    }

    /*
    * update reminder -START
    * */
    private fun updateDateTime(remiderTable: RemiderTable) {

        val startHour = remiderTable.time.substring(0, remiderTable.time.indexOf(":")).toInt()
        val startMinute = remiderTable.time.substring(
            remiderTable.time.indexOf(":") + 1,
            remiderTable.time.indexOf(" ")
        ).toInt()
        var ampm: String
        TimePickerDialog(
            this@ReminderActivity,
            TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                if (hour < 12) {
                    ampm = "AM"
                } else {
                    ampm = "PM"
                }
                var time = hour.toString().plus(":").plus(minute).plus(" ").plus(ampm)

                Log.d(TAG, "pickDateTime: " + time)
                dialogWeekList(time, hour, minute, "update", remiderTable)
            },
            startHour,
            startMinute,
            android.text.format.DateFormat.is24HourFormat(this@ReminderActivity)
        ).show()
    }

    @SuppressWarnings("kotlin:S107")
    //updateReminderData() method have too many parameters
    private fun updateReminderData(
        time: String,
        monday: Boolean,
        tuesday: Boolean,
        wednesday: Boolean,
        thursday: Boolean,
        friday: Boolean,
        saturday: Boolean,
        sunday: Boolean,
        hour: Int,
        minute: Int,
        remiderTable: RemiderTable?
    ) {
        lifecycleScope.launch {

            this.coroutineContext.let {
                remiderTable!!.time = time
                remiderTable!!.monday = monday
                remiderTable!!.tuesday = tuesday
                remiderTable!!.wednesday = wednesday
                remiderTable!!.thursday = thursday
                remiderTable!!.friday = friday
                remiderTable!!.saturday = saturday
                remiderTable!!.sunday = sunday
                remiderTable!!.switchReminder = mySharedPreferences.getReminderSwitchEnable()!!

                db.reminderDao().updateAll(remiderTable)
                getReminderData()

                mySharedPreferences.setWeekData(monday,tuesday,wednesday,thursday,friday,saturday,sunday)
                mySharedPreferences.setReminderSwitchEnabled(remiderTable.switchReminder)
                mySharedPreferences.setHour(hour.toString())
                mySharedPreferences.setMinute(minute.toString())
                scheduleAlarm(hour,minute)

            }
        }
    }

    /*
    * update reminder - END
    * */

    private fun removeReminder(id: Int) {
        lifecycleScope.launch {
            this.coroutineContext.let {

                var remiderTable = db.reminderDao().deleteReminderData(reminderList.get(id))
                val aBoolean: Boolean
                aBoolean = if (remiderTable > 0) {
                    true
                } else {
                    false
                }
                if (aBoolean) {
                    cancelAlarm()
                    getReminderData()
                } else {
                    Toast.makeText(this@ReminderActivity, "Failed Delete Item", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getReminderData() {

        lifecycleScope.launch {
            this.coroutineContext.let {
                var remiderTable = db.reminderDao().fetchAllData()
                reminderList.clear()
                reminderList.addAll(remiderTable)
                reminderListAdapter.notifyDataSetChanged()
                if (reminderList.size == 1) {
                    activityReminderBinding.ivAddReminder.visibility = View.GONE
                } else {
                    activityReminderBinding.ivAddReminder.visibility = View.VISIBLE
                }

            }
        }
    }

    /*
    * Insert Reminder Code ---- START
    * */
    fun onAddReminderClick() {
        pickDateTime()
    }

    private fun pickDateTime() {
        val currentDateTime = Calendar.getInstance()
        val startHour = currentDateTime.get(Calendar.HOUR_OF_DAY)
        val startMinute = currentDateTime.get(Calendar.MINUTE)
        var ampm: String
        TimePickerDialog(
            this@ReminderActivity,
            R.style.TimePickerTheme,
            TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                if (hour < 12) {
                    ampm = "AM"
                } else {
                    ampm = "PM"
                }
                var time = hour.toString().plus(":").plus(minute).plus(" ").plus(ampm)

                Log.d(TAG, "pickDateTime: " + time)
                dialogWeekList(time, hour, minute, "insert", null)
            },
            startHour,
            startMinute,
            android.text.format.DateFormat.is24HourFormat(this@ReminderActivity)
        ).show()

    }

    @SuppressWarnings("kotlin:S3776")
    //dialogWeekList() method Cognitive Complexity
    private fun dialogWeekList(
        time: String,
        hour: Int,
        minute: Int,
        type: String,
        remiderTable: RemiderTable?
    ) {
        val dialogWeekList = Dialog(this@ReminderActivity, android.R.style.Theme_Dialog)
        dialogWeekList.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogWeekList.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogWeekList.setContentView(R.layout.dialog_week_list)

        var cbWeekMonday = dialogWeekList.findViewById<AppCompatCheckBox>(R.id.cbWeekMonday)
        var cbWeekTuesday = dialogWeekList.findViewById<AppCompatCheckBox>(R.id.cbWeekTuesday)
        var cbWeekWednesday = dialogWeekList.findViewById<AppCompatCheckBox>(R.id.cbWeekWednesday)
        var cbWeekThursday = dialogWeekList.findViewById<AppCompatCheckBox>(R.id.cbWeekThursday)
        var cbWeekFriday = dialogWeekList.findViewById<AppCompatCheckBox>(R.id.cbWeekFriday)
        var cbWeekSaturday = dialogWeekList.findViewById<AppCompatCheckBox>(R.id.cbWeekSaturday)
        var cbWeekSunday = dialogWeekList.findViewById<AppCompatCheckBox>(R.id.cbWeekSunday)
        var tvOK = dialogWeekList.findViewById<TextView>(R.id.tvOK)


        var monday: Boolean = true

        cbWeekMonday.setOnCheckedChangeListener { _, isChecked ->
            if (cbWeekMonday.isChecked) {
                monday = true
            } else {
                monday = false
            }
        }

        var tuesday: Boolean = true
        cbWeekTuesday.setOnCheckedChangeListener { _, isChecked ->
            if (cbWeekTuesday.isChecked) {
                tuesday = true
            } else {
                tuesday = false
            }
        }

        var wednesday: Boolean = true
        cbWeekWednesday.setOnCheckedChangeListener { _, isChecked ->
            if (cbWeekWednesday.isChecked) {
                wednesday = true
            } else {
                wednesday = false
            }
        }

        var thursday: Boolean = true
        cbWeekThursday.setOnCheckedChangeListener { _, isChecked ->
            if (cbWeekThursday.isChecked) {
                thursday = true
            } else {
                thursday = false
            }
        }

        var friday: Boolean = true
        cbWeekFriday.setOnCheckedChangeListener { _, isChecked ->
            if (cbWeekFriday.isChecked) {
                friday = true
            } else {
                friday = false
            }
        }

        var saturday: Boolean = true
        cbWeekSaturday.setOnCheckedChangeListener { _, isChecked ->
            if (cbWeekSaturday.isChecked) {
                saturday = true
            } else {
                saturday = false
            }
        }

        var sunday: Boolean = true
        cbWeekSunday.setOnCheckedChangeListener { _, isChecked ->
            if (cbWeekSunday.isChecked) {
                sunday = true
            } else {
                sunday = false
            }
        }

        if (type.equals("update")) {
            monday = remiderTable!!.monday
            tuesday = remiderTable!!.tuesday
            wednesday = remiderTable!!.wednesday
            thursday = remiderTable!!.thursday
            friday = remiderTable!!.friday
            saturday = remiderTable!!.saturday
            sunday = remiderTable!!.sunday

            cbWeekMonday.isChecked = monday
            cbWeekTuesday.isChecked = tuesday
            cbWeekWednesday.isChecked = wednesday
            cbWeekThursday.isChecked = thursday
            cbWeekFriday.isChecked = friday
            cbWeekSaturday.isChecked = saturday
            cbWeekSunday.isChecked = sunday
        } else {
            cbWeekMonday.isChecked = monday
            cbWeekTuesday.isChecked = tuesday
            cbWeekWednesday.isChecked = wednesday
            cbWeekThursday.isChecked = thursday
            cbWeekFriday.isChecked = friday
            cbWeekSaturday.isChecked = saturday
            cbWeekSunday.isChecked = sunday
        }

        tvOK.setOnClickListener(View.OnClickListener { view ->
            if (!monday && !tuesday && !wednesday && !thursday && !friday && !saturday && !sunday) {
                Toast.makeText(
                    applicationContext,
                    "Please select at least one day of week",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                dialogWeekList.dismiss()

                if (type.equals("update")) {
                    updateReminderData(
                        time,
                        monday,
                        tuesday,
                        wednesday,
                        thursday,
                        friday,
                        saturday,
                        sunday,
                        hour,
                        minute,
                        remiderTable
                    )
                } else {
                    insertReminderData(
                        time,
                        monday,
                        tuesday,
                        wednesday,
                        thursday,
                        friday,
                        saturday,
                        sunday,
                        hour,
                        minute
                    )
                }
            }
        })

        dialogWeekList.show()
        dialogWeekList.setCancelable(true)

    }

    @SuppressWarnings("kotlin:S107")
    //insertReminderData() method have too many parameters
    private fun insertReminderData(
        time: String,
        monday: Boolean,
        tuesday: Boolean,
        wednesday: Boolean,
        thursday: Boolean,
        friday: Boolean,
        saturday: Boolean,
        sunday: Boolean,
        hour: Int,
        minute: Int
    ) {
        lifecycleScope.launch {
            val data = RemiderTable(
                0,
                time,
                true,
                monday,
                tuesday,
                wednesday,
                thursday,
                friday,
                saturday,
                sunday
            )
            this.coroutineContext.let {
                db.reminderDao().insertAll(data)
                getReminderData()
                mySharedPreferences.setWeekData(monday,tuesday,wednesday,thursday,friday,saturday,sunday)
                mySharedPreferences.setReminderSwitchEnabled(true)
                mySharedPreferences.setHour(hour.toString())
                mySharedPreferences.setMinute(minute.toString())
                scheduleAlarm(hour,minute)
            }
        }
    }

    fun scheduleAlarm(
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
        var intent = Intent(this@ReminderActivity, MyBroadCastReciver::class.java)
        var pendingIntent =
            PendingIntent.getBroadcast(
                this@ReminderActivity,
                0,
                intent,
                0
            )
        val am = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        am.setRepeating(AlarmManager.RTC_WAKEUP,millies,AlarmManager.INTERVAL_DAY,pendingIntent)
    }

    override fun onPostResume() {
        registerReceiver(myBroadCastReciver, s_intentFilter)
        super.onPostResume()
    }

    override fun onPause() {
        unregisterReceiver(myBroadCastReciver)
        super.onPause()
    }
     private fun cancelAlarm() {
         val alarmManager2 =
             getSystemService(Context.ALARM_SERVICE) as AlarmManager

         val pendingIntent2 = PendingIntent.getBroadcast(
             applicationContext, 0, Intent(
                 this,
                 MyBroadCastReciver::class.java
             ), 0
         )
         alarmManager2.cancel(pendingIntent2)


     }
    /*
   * Insert Reminder Code ---- END
   * */


    private fun populateUnifiedNativeAdView(
//        nativeAd: NativeAd,
//        adView: NativeAdView
    ) {
        // Set the media view. Media content will be automatically populated in the media view once
        // adView.setNativeAd() is called.
//        val mediaView: MediaView = adView.findViewById(R.id.ad_media)
//        adView.mediaView = mediaView
//        mediaView.setMediaContent(nativeAd.mediaContent)
//        mediaView.setImageScaleType(ImageView.ScaleType.CENTER_CROP)
//
//
//        // Set other ad assets.
//        adView.headlineView = adView.findViewById(R.id.ad_headline)
//        adView.bodyView = adView.findViewById(R.id.ad_body)
//        adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)
//        adView.iconView = adView.findViewById(R.id.ad_app_icon)
//        adView.priceView = adView.findViewById(R.id.ad_price)
//        adView.starRatingView = adView.findViewById(R.id.ad_stars)
//        adView.storeView = adView.findViewById(R.id.ad_store)
//        adView.advertiserView = adView.findViewById(R.id.ad_advertiser)
//
//        // The headline is guaranteed to be in every UnifiedNativeAd.
//        (adView.headlineView as TextView).text = nativeAd.headline
//
//        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
//        // check before trying to display them.
//        if (nativeAd.body == null) {
//            adView.bodyView.visibility = View.INVISIBLE
//        } else {
//            adView.bodyView.visibility = View.VISIBLE
//            (adView.bodyView as TextView).text = nativeAd.body
//        }
//        if (nativeAd.callToAction == null) {
//            adView.callToActionView.visibility = View.INVISIBLE
//        } else {
//            adView.callToActionView.visibility = View.VISIBLE
//            (adView.callToActionView as Button).text = nativeAd.callToAction
//        }
//        if (nativeAd.icon == null) {
//            adView.iconView.visibility = View.GONE
//        } else {
//            (adView.iconView as ImageView).setImageDrawable(
//                nativeAd.icon.drawable
//            )
//            adView.iconView.visibility = View.VISIBLE
//        }
//        if (nativeAd.price == null) {
//            adView.priceView.visibility = View.INVISIBLE
//        } else {
//            adView.priceView.visibility = View.VISIBLE
//            (adView.priceView as TextView).text = nativeAd.price
//        }
//        if (nativeAd.store == null) {
//            adView.storeView.visibility = View.INVISIBLE
//        } else {
//            adView.storeView.visibility = View.VISIBLE
//            (adView.storeView as TextView).text = nativeAd.store
//        }
//        if (nativeAd.starRating == null) {
//            adView.starRatingView.visibility = View.INVISIBLE
//        } else {
//            (adView.starRatingView as RatingBar).rating = nativeAd.starRating.toFloat()
//            adView.starRatingView.visibility = View.VISIBLE
//        }
//        if (nativeAd.advertiser == null) {
//            adView.advertiserView.visibility = View.INVISIBLE
//        } else {
//            (adView.advertiserView as TextView).text = nativeAd.advertiser
//            adView.advertiserView.visibility = View.VISIBLE
//        }
//
//        // This method tells the Google Mobile Ads SDK that you have finished populating your
//        // native ad view with this native ad. The SDK will populate the adView's MediaView
//        // with the media content from this native ad.
//
//        adView.setNativeAd(nativeAd)
//

    }

    /**
     * Creates a request for a new native ad based on the boolean parameters and calls the
     * corresponding "populate" method when one is successfully returned.
     *
     */
    private fun refreshAd() {
//        val builder = AdLoader.Builder(this, resources.getString(R.string.native_id))
//        builder.forNativeAd (NativeAd.OnNativeAdLoadedListener {
//                nativeAd1 ->
//            // OnUnifiedNativeAdLoadedListener implementation.
//            // You must call destroy on old ads when you are done with them,
//            // otherwise you will have a memory leak.
//            if (nativeAd1 != null) {
//                nativeAd1!!.destroy()
//            }
//            nativeAd = nativeAd1
//            val adView = layoutInflater
//                .inflate(R.layout.ad_unified, null) as NativeAdView
//            populateUnifiedNativeAdView(nativeAd, adView)
//            activityReminderBinding.flAdplaceholder.removeAllViews()
//            activityReminderBinding.flAdplaceholder.addView(adView)
//        })
//
//        val adLoader = builder
//            .withAdListener(object : AdListener() {
//                override fun onAdFailedToLoad(adError: LoadAdError) {
//                    // Handle the failure by logging, altering the UI, and so on.
//                }
//            })
//            .build()
//
//        adLoader.loadAd(AdRequest.Builder().build())

    }
}