package com.qrolic.fitnessapp.activity

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.qrolic.fitnessapp.R
import com.qrolic.fitnessapp.databinding.ActivityWorkoutBenifitsBinding

class WorkoutBenifitsActivity : AppCompatActivity() {


    lateinit var activityWorkoutBenifitsBinding: ActivityWorkoutBenifitsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityWorkoutBenifitsBinding = DataBindingUtil.setContentView(this,R.layout.activity_workout_benifits)
        initAll()
    }

    private fun initAll() {
        supportActionBar?.setTitle("Workout Benefits")
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#26022C")))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        /*
      * banner ad
      * */
        MobileAds.initialize(this) {}
        val adRequest = AdRequest.Builder().build()
        activityWorkoutBenifitsBinding.adView.loadAd(adRequest)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home){
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}