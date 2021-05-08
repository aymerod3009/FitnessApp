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
import com.qrolic.fitnessapp.databinding.ActivityPrivacyPolicyBinding

class PrivacyPolicyActivity : AppCompatActivity() {


    lateinit var activityPrivacyPolicyBinding: ActivityPrivacyPolicyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityPrivacyPolicyBinding = DataBindingUtil.setContentView(this,R.layout.activity_privacy_policy)
        initAll()
    }

    private fun initAll() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle("Privacy Policy")
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#26022C")))

        activityPrivacyPolicyBinding.wvPrivacyPolicy.loadUrl(getString(R.string.privacy_policy))

        /*
      * banner ad
      * */
        MobileAds.initialize(this) {}
        val adRequest = AdRequest.Builder().build()
        activityPrivacyPolicyBinding.adView.loadAd(adRequest)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            onBackPressed()
        }
        return true
    }
}