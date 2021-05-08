package com.qrolic.fitnessapp.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.ktx.Firebase
import com.qrolic.fitnessapp.R
import com.qrolic.fitnessapp.database.MySharedPreferences


class SplashActivity : AppCompatActivity() {
    lateinit var mySharedPreferences: MySharedPreferences
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    val TAG:String = SplashActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        init()
    }

    private fun init() {
        firebaseAnalytics = Firebase.analytics
        supportActionBar?.hide()
        mySharedPreferences = MySharedPreferences(this)

        firebaseToken()

        Handler(Looper.getMainLooper()).postDelayed({
            if (mySharedPreferences.getUserLogin()){
                val intent = Intent(this, BottomNavigationActivity::class.java)
                startActivity(intent)
                finish()
            }else {
                val intent = Intent(this, AppIntroActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, 3000) // 3000 is the delayed time in milliseconds.

    }

   private fun firebaseToken(){



       FirebaseInstallations.getInstance().getToken(true).addOnCompleteListener {
           var token = it.result!!.token
           Log.d(TAG, "token id$token")

           // DO your thing with your firebase token
       }

   }
}