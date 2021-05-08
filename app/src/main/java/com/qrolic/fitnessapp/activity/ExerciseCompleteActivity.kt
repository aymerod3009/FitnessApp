package com.qrolic.fitnessapp.activity

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.content.Intent
import android.os.Bundle
import android.text.format.DateUtils
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
//import com.google.android.gms.ads.AdRequest
//import com.google.android.gms.ads.LoadAdError
//import com.google.android.gms.ads.MobileAds
//import com.google.android.gms.ads.interstitial.InterstitialAd
//import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.qrolic.fitnessapp.BuildConfig
import com.qrolic.fitnessapp.R
import com.qrolic.fitnessapp.database.FitnessDatabase
import com.qrolic.fitnessapp.database.MySharedPreferences
import com.qrolic.fitnessapp.databinding.ActivityExerciseCompleteBinding
import com.qrolic.fitnessapp.model.ExerciseCompleteTable
import com.qrolic.fitnessapp.model.WorkoutListTable
import hotchemi.android.rate.AppRate
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class ExerciseCompleteActivity : AppCompatActivity() {

    private var tag: String = "ExerciseCompleteActivity"
    private var totalCaloriesMET: Double=0.0
    private var totalExerciseTime: Int = 0
     lateinit var workoutListTableList: ArrayList<WorkoutListTable>
    lateinit var mySharedPreferences: MySharedPreferences
    lateinit var db: FitnessDatabase
    private lateinit var workoutName: String
//    private lateinit var mInterstitialAd: InterstitialAd
    lateinit var activityExerciseCompleteBinding: ActivityExerciseCompleteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityExerciseCompleteBinding = DataBindingUtil.setContentView(this,R.layout.activity_exercise_complete)
        initAll()
    }

    private fun initAll() {
        supportActionBar?.hide()

        /*
        * intertitial ad
        * */
//        MobileAds.initialize(this) {}
//        val adRequest = AdRequest.Builder().build()

//        InterstitialAd.load(
//            this,
//            resources.getString(R.string.intertitial_id),
//            adRequest,
//            object : InterstitialAdLoadCallback() {
//                override fun onAdLoaded(interstitialAd: InterstitialAd) {
//                    // The mInterstitialAd reference will be null until
//                    // an ad is loaded.
//                    mInterstitialAd = interstitialAd
//                    mInterstitialAd.show(this@ExerciseCompleteActivity)
//                }
//
//                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
//                    // Handle the error
//                }
//        })

        rateus()


        db = FitnessDatabase.invoke(this)

        val  reportfragment = com.qrolic.fitnessapp.fragment.ReportFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.flReport, reportfragment)
        transaction.commit()

        mySharedPreferences = MySharedPreferences(this)
        if(mySharedPreferences.getScreenOn()!!){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }else{
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

        activityExerciseCompleteBinding.ltvExcersieComplete.addAnimatorListener(object :
            AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                activityExerciseCompleteBinding.ltvExcersieComplete.visibility = (View.VISIBLE)
            }

            override fun onAnimationEnd(animation: Animator) {
                activityExerciseCompleteBinding.ltvExcersieComplete.visibility = (View.GONE)
            }

            override fun onAnimationCancel(animation: Animator) {
                Log.e("Animation:", "cancel")
            }

            override fun onAnimationRepeat(animation: Animator) {
                Log.e("Animation:", "repeat")
            }
        })

        workoutListTableList =
            intent.getSerializableExtra("exerciseList") as ArrayList<WorkoutListTable>
        totalExerciseTime = intent.getStringExtra("totalExerciseTime").toString().toInt()
        totalCaloriesMET = intent.getStringExtra("totalCaloriesBurned").toString().toDouble()
        workoutName = intent.getStringExtra("workoutName").toString()

        activityExerciseCompleteBinding.tvTotalExercise.text = workoutListTableList.size.toString()
        activityExerciseCompleteBinding.tvTotalTime.text = DateUtils.formatElapsedTime(totalExerciseTime.toLong()).toString()

       /*
       Total calories burned = Duration (in minutes)*(MET*3.5*weight in kg)/200 formula
       */
        val totalMinutes = TimeUnit.SECONDS.toMinutes(totalExerciseTime.toLong())
        val totalMET = totalCaloriesMET

        val weightInKg:Double
        if (mySharedPreferences.getType().equals("lbft")) {
            weightInKg = (mySharedPreferences.getWeight().toString().toDouble() / 2.205)
        }
        else {
            weightInKg = mySharedPreferences.getWeight().toString().toDouble()
        }

        val burnedCalories = totalMinutes * (totalMET * 3.5 * weightInKg) / 200

        activityExerciseCompleteBinding.tvCaloriesBurned.text = DecimalFormat("#.#").format(burnedCalories).toString()

        Log.d(tag, "initAll: totalMinutes " + totalMinutes)
        Log.d(tag, "initAll: totalMet " + totalMET)
        Log.d(tag, "initAll: weight " + weightInKg)
        Log.d(tag, "initAll: calories " + burnedCalories)
        Log.d(tag, "initAll: time " + Calendar.getInstance().time)

        activityExerciseCompleteBinding.llShare.setOnClickListener { onShareClick() }
        activityExerciseCompleteBinding.tvDoItAgain.setOnClickListener { onDoItAgainClick() }


        var dateTime = DateFormat.getDateTimeInstance().format(Date())
        var date = Calendar.getInstance().time
        var simpleDateFormat =SimpleDateFormat("dd-MM-yyyy")
        var formateddate = simpleDateFormat.format(date)
        Log.d(tag, "initAll: formateddate " + date)

        insertExerciseData(
            activityExerciseCompleteBinding.tvTotalTime.text.toString(),
            activityExerciseCompleteBinding.tvCaloriesBurned.text.toString(),
            activityExerciseCompleteBinding.tvTotalExercise.text.toString(),
            workoutName,
            dateTime,
            formateddate
        )
    }

    private fun rateus() {
        // Here 0 means
        // the installation date.
        AppRate.with(this)

            // default 10
            .setInstallDays(0)

            // default 10
            .setLaunchTimes(1)

            // default 1
            .setRemindInterval(1)
            .monitor()

        // Show a dialogue
        // if meets conditions
        AppRate
            .showRateDialogIfMeetsConditions(
                this
            )

    }

    fun onShareClick(){
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_SUBJECT, "Android Studio Pro")
        intent.putExtra(
            Intent.EXTRA_TEXT,
            "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID
        )
        intent.type = "text/plain"
        startActivity(intent)

    }

    fun onDoItAgainClick() {
        val intent =
            Intent(this@ExerciseCompleteActivity, ReadyToGoActivity::class.java)
        intent.putExtra("exerciseList", workoutListTableList)
        intent.putExtra("workoutName", workoutName)

        intent.flags =
            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }

    private fun insertExerciseData(
        totalTime: String,
        burnedCalories: String,
        totalExercise: String,
        workoutTypeName: String,
        dateTime: String,
        date: String
    ) {
        lifecycleScope.launch {
            val data = ExerciseCompleteTable(
                0,
                totalTime,
                burnedCalories,
                totalExercise,
                workoutTypeName,
                dateTime,
                date
            )
            this.coroutineContext.let {
                db.exerciseComplete().insertAll(data)
            }
        }
    }
}