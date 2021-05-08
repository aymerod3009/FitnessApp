package com.qrolic.fitnessapp.activity

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
//import com.google.android.gms.ads.AdListener
//import com.google.android.gms.ads.AdLoader
//import com.google.android.gms.ads.AdRequest
//import com.google.android.gms.ads.LoadAdError
//import com.google.android.gms.ads.nativead.MediaView
//import com.google.android.gms.ads.nativead.NativeAd
//import com.google.android.gms.ads.nativead.NativeAdView
import com.gusakov.library.start
import com.qrolic.fitnessapp.R
import com.qrolic.fitnessapp.database.MySharedPreferences
import com.qrolic.fitnessapp.databinding.ActivityRestBinding
import com.qrolic.fitnessapp.model.WorkoutListTable
import java.util.*
import kotlin.collections.ArrayList

class RestActivity : AppCompatActivity(), TextToSpeech.OnInitListener  {

    private var totalCaloriesBurned: Double=0.0
    private var totalExerciseTime: Int = 0
    lateinit var workoutListTableList: ArrayList<WorkoutListTable>
    var position: Int = 0
    var restTime: Int = 30
    private var tts: TextToSpeech? = null
    lateinit var mySharedPreferences: MySharedPreferences
    private lateinit var workoutName: String
//    private lateinit var nativeAd: NativeAd
    lateinit var frameLayout: FrameLayout
    lateinit var activityRestBinding:ActivityRestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityRestBinding = DataBindingUtil.setContentView(this,R.layout.activity_rest)
        initAll()
    }

    private fun initAll() {

        supportActionBar?.hide()
            mySharedPreferences = MySharedPreferences(this)
        tts = TextToSpeech(this, this)

        if (mySharedPreferences.getMute()!!){
            activityRestBinding.ivMic.setImageResource(R.drawable.ic_baseline_mic_off_24)
        }else{
            activityRestBinding.ivMic.setImageResource(R.drawable.ic_baseline_mic_24)
        }

        if(mySharedPreferences.getScreenOn()!!){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }else{
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

        restTime = mySharedPreferences.getRestSetTime().toString().toInt()

        workoutListTableList =
            intent.getSerializableExtra("exerciseList") as ArrayList<WorkoutListTable>
        position = intent.getStringExtra("position").toString().toInt()
        totalExerciseTime = intent.getStringExtra("totalExerciseTime").toString().toInt()
        totalCaloriesBurned = intent.getStringExtra("totalCaloriesBurned").toString().toDouble()
        workoutName = intent.getStringExtra("workoutName").toString()

        activityRestBinding.tvExerciseNumber.text = (position + 1).toString()
        activityRestBinding.tvTotalExercise.text = workoutListTableList.size.toString()
        activityRestBinding.tvNextEcersieName.text = workoutListTableList[position].execriseName
        activityRestBinding.tvNextEcerciseTime.text = workoutListTableList[position].execrsieTime
        activityRestBinding.ltvNextExecrsieImage.setAnimation(workoutListTableList[position].workoutTypeImage)

        activityRestBinding.spvOverallProgress.totalProgress = workoutListTableList.size
        activityRestBinding.spvOverallProgress.currentProgress = position

        var total = activityRestBinding.spvOverallProgress.totalProgress
        val arrayList: ArrayList<Int> = ArrayList<Int>(total)

        for (i in 0..total){
            arrayList.add(i)
            activityRestBinding.spvOverallProgress.markers = arrayList

        }

        restTimer(restTime)

        activityRestBinding.ivBack.setOnClickListener {  backClick()}
        activityRestBinding.tvIncreaseRestTime.setOnClickListener {  onIncreaseRestTimeClick()}
        activityRestBinding.tvSkip.setOnClickListener {  onSkipClick()}
        activityRestBinding.ivMic.setOnClickListener {  onMicClick()}
    }

    fun restTimer(restTime: Int) {
        activityRestBinding.pulseCountDown.startValue = restTime
        activityRestBinding.pulseCountDown.endValue = 0


        activityRestBinding.pulseCountDown.start {
            if (tts != null) {
                tts!!.stop()
                tts!!.shutdown()
            }
            val intent = Intent(this@RestActivity, StartExerciseActivity::class.java)
            intent.putExtra("exerciseList", workoutListTableList)
            intent.putExtra("position", "" + position)
            intent.putExtra("totalExerciseTime", "" + totalExerciseTime)
            intent.putExtra("totalCaloriesBurned", "" + totalCaloriesBurned)
            intent.putExtra("workoutName",workoutName)

            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }

    }

    fun backClick(){
        onBackPressed()
    }

     fun onIncreaseRestTimeClick() {
        restTime = activityRestBinding.pulseCountDown.text.toString().toInt() + 20
        restTimer(restTime)
    }

    fun onSkipClick() {
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }
        val intent = Intent(this@RestActivity, StartExerciseActivity::class.java)
        intent.putExtra("exerciseList", workoutListTableList)
        intent.putExtra("position", "" + position)
        intent.putExtra("totalExerciseTime", "" + totalExerciseTime)
        intent.putExtra("totalCaloriesBurned", "" + totalCaloriesBurned)
        intent.putExtra("workoutName",workoutName)

        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }

    @SuppressWarnings("kotlin:S3776")
    // onInt() method have Cognitive Complexity
    override fun onInit(status: Int) {
        // set UK English as language for tts

        if (status == TextToSpeech.LANG_MISSING_DATA || status == TextToSpeech.LANG_NOT_SUPPORTED) {
            Log.e("TTS", "The Language specified is not supported!")
        } else {
            if (!mySharedPreferences.getMute()!!) {

                if (mySharedPreferences.getLocaleLanguage().equals("UK")) {
                    tts!!.setLanguage(Locale.UK)
                } else if (mySharedPreferences.getLocaleLanguage().equals("CANADA_FRENCH")) {
                    tts!!.setLanguage(Locale.CANADA_FRENCH)
                }else if (mySharedPreferences.getLocaleLanguage().equals("CANADA")){
                    tts!!.setLanguage(Locale.CANADA)
                }else if(mySharedPreferences.getLocaleLanguage().equals("ENGLISH")){
                    tts!!.setLanguage(Locale.ENGLISH)
                }else if (mySharedPreferences.getLocaleLanguage().equals("FRENCH")){
                    tts!!.setLanguage(Locale.FRENCH)
                }else if (mySharedPreferences.getLocaleLanguage().equals("GERMAN")){
                    tts!!.setLanguage(Locale.GERMAN)
                }else if (mySharedPreferences.getLocaleLanguage().equals("ITALIAN")){
                    tts!!.setLanguage(Locale.ITALIAN)
                }else if(mySharedPreferences.getLocaleLanguage().equals("JAPANESE")){
                    tts!!.setLanguage(Locale.JAPANESE)
                }else{
                    tts!!.setLanguage(Locale.US)
                }
                tts!!.speak(
                    "Take a rest for "+restTime+ "seconds. Next" + activityRestBinding.tvNextEcersieName.text.toString() + " with " + activityRestBinding.tvNextEcerciseTime.text.toString()
                        .substring(
                            workoutListTableList[position].execrsieTime.toString().indexOf(":") + 1,
                            workoutListTableList[position].execrsieTime.toString().length
                        ) + " Seconds", TextToSpeech.QUEUE_FLUSH, null, ""
                )
            }
        }

    }

    override fun onBackPressed() {
        val dialogQuit = Dialog(this@RestActivity, android.R.style.Theme_Dialog)
        dialogQuit.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogQuit.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogQuit.setContentView(R.layout.dialog_quit)

        var tvContinue = dialogQuit.findViewById<View>(R.id.tvContinue) as TextView
        var tvQuit = dialogQuit.findViewById<View>(R.id.tvQuit) as TextView
        var ivClose = dialogQuit.findViewById<View>(R.id.ivClose) as ImageView
        frameLayout = dialogQuit.findViewById<FrameLayout>(R.id.fl_adplaceholder)

        refreshAd()

        ivClose.setOnClickListener(View.OnClickListener { view ->
            dialogQuit.dismiss()
        })

        tvContinue.setOnClickListener(View.OnClickListener { view ->
            dialogQuit.dismiss()
        })

        tvQuit.setOnClickListener(View.OnClickListener { view ->
            super.onBackPressed()

        })

        dialogQuit.show()
        dialogQuit.setCancelable(true)    }

    public override fun onDestroy() {
        // Shutdown TTS
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }
        super.onDestroy()
    }
    private fun populateUnifiedNativeAdView(
//        nativeAd: NativeAd,
//        adView: NativeAdView
    ) {
        // Set the media view. Media content will be automatically populated in the media view once
//        // adView.setNativeAd() is called.
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
//            frameLayout.removeAllViews()
//            frameLayout.addView(adView)
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

    fun onMicClick(){
        if (mySharedPreferences.getMute()!!){
            mySharedPreferences.setMute(false)
        }else{
            mySharedPreferences.setMute(true)
        }

        if (mySharedPreferences.getMute()!!){
            activityRestBinding.ivMic.setImageResource(R.drawable.ic_baseline_mic_off_24)
        }else{
            activityRestBinding.ivMic.setImageResource(R.drawable.ic_baseline_mic_24)
        }
        onInit(0)
    }

}