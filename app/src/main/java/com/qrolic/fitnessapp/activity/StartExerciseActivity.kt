package com.qrolic.fitnessapp.activity

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.gusakov.library.start
import com.qrolic.fitnessapp.R
import com.qrolic.fitnessapp.database.MySharedPreferences
import com.qrolic.fitnessapp.databinding.ActivityStartExerciseBinding
import com.qrolic.fitnessapp.model.WorkoutListTable
import java.util.*
import kotlin.collections.ArrayList

class StartExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private var totalExerciseCaloriesIntent: Double = 0.0
    private var totalExerciseTimeIntent: Int = 0
    private lateinit var workoutName: String
    var counter = 0
    lateinit var workoutListTableList: ArrayList<WorkoutListTable>
    var position: Int = 0
    private var tts: TextToSpeech? = null
    private lateinit var countDownTimer: CountDownTimer
    var mMediaPlayer: MediaPlayer? = null
    lateinit var totalTime:String
    var totalExerciseTime:Int = 0
    var totalCalories:Double = 0.0
    var exerciseMET:Double = 0.0
    lateinit var mySharedPreferences:MySharedPreferences
    var backPress:Boolean = false
    lateinit var nativeAd: NativeAd
    lateinit var frameLayout: FrameLayout
    lateinit var activityStartExerciseBinding: ActivityStartExerciseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityStartExerciseBinding = DataBindingUtil.setContentView(this,R.layout.activity_start_exercise)
        initAll()
    }

    @SuppressWarnings("kotlin:S3776")
    //initAll() method have Cognitive Complexity
    private fun initAll() {
        supportActionBar?.hide()
        mySharedPreferences = MySharedPreferences(this)

        activityStartExerciseBinding.pulseCountDown.visibility = View.VISIBLE

        tts = TextToSpeech(this, this)

        if (mySharedPreferences.getMute()!!){
            activityStartExerciseBinding.ivMic.setImageResource(R.drawable.ic_baseline_mic_off_24)
        }else{
            activityStartExerciseBinding.ivMic.setImageResource(R.drawable.ic_baseline_mic_24)
        }
        if(mySharedPreferences.getScreenOn()!!){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }else{
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }


        activityStartExerciseBinding.ivNext.setOnClickListener { onNextClick() }
        activityStartExerciseBinding.ivPause.setOnClickListener {  onPauseClick() }
        activityStartExerciseBinding.ivPlay.setOnClickListener { onPlayClick() }
        activityStartExerciseBinding.ivBack.setOnClickListener { onBackButtonClick() }
        activityStartExerciseBinding.ivMic.setOnClickListener { onMicClick() }

        mySharedPreferences.setPauseEnable(false)
        activityStartExerciseBinding.ltvExcersieImage.playAnimation()

            if (mySharedPreferences.getPauseEnable()!!) {
                activityStartExerciseBinding.ivPlay.visibility = View.VISIBLE
                activityStartExerciseBinding.ivPause.visibility = View.GONE
                activityStartExerciseBinding.ltvExcersieImage.pauseAnimation()
            } else {
                activityStartExerciseBinding.ivPlay.visibility = View.GONE
                activityStartExerciseBinding.ivPause.visibility = View.VISIBLE
                activityStartExerciseBinding.ltvExcersieImage.playAnimation()
            }
        if(backPress== false) {
            activityStartExerciseBinding.ivPause.visibility = View.GONE
            activityStartExerciseBinding.ivPlay.visibility = View.VISIBLE
        }
        /*
        set values from intent list
        */
        workoutListTableList =
            intent.getSerializableExtra("exerciseList") as ArrayList<WorkoutListTable>
        position = intent.getStringExtra("position").toString().toInt()
        totalExerciseTimeIntent = intent.getStringExtra("totalExerciseTime").toString().toInt()
        totalExerciseCaloriesIntent = intent.getStringExtra("totalCaloriesBurned").toString().toDouble()
        workoutName = intent.getStringExtra("workoutName").toString()


        activityStartExerciseBinding.tvExcersieName.text = workoutListTableList[position].execriseName
        activityStartExerciseBinding.tvExerciseTime.text = workoutListTableList[position].execrsieTime.toString().substring(
            workoutListTableList[0].execrsieTime.toString().indexOf(":") + 1,
            workoutListTableList[0].execrsieTime.toString().length
        )
        activityStartExerciseBinding.ltvExcersieImage.setAnimation(workoutListTableList[position].workoutTypeImage)

        exerciseMET = workoutListTableList[position].execrsieMET.toString().toDouble()

        totalCalories = exerciseMET

        totalCalories = totalExerciseCaloriesIntent + totalCalories


        /*
        overall progress bar code
        * */
        activityStartExerciseBinding.spvOverallProgress.totalProgress = workoutListTableList.size
        activityStartExerciseBinding.spvOverallProgress.currentProgress = position

        var total = activityStartExerciseBinding.spvOverallProgress.totalProgress
        val arrayList: ArrayList<Int> = ArrayList<Int>(total)

        for (i in 0..total) {
            arrayList.add(i)
            activityStartExerciseBinding.spvOverallProgress.markers = arrayList

        }


        /*
         play whistle sound using media player
        * */
        playSound()


        activityStartExerciseBinding.pulseCountDown.startValue = 3
        activityStartExerciseBinding.pulseCountDown.endValue = 0
        /*
        count downtimer start when pluscountdown timer reach to 0
        * */
        activityStartExerciseBinding.pulseCountDown.start {
            activityStartExerciseBinding.ivNext.visibility = View.VISIBLE
            activityStartExerciseBinding.ivPause.visibility = View.VISIBLE
            activityStartExerciseBinding.ivBack.visibility = View.VISIBLE

            backPress = true
            textToSpeech()

            activityStartExerciseBinding.pulseCountDown.visibility = View.GONE

            totalTime = activityStartExerciseBinding.tvExerciseTime.text.toString()

            countDownTimer = object : CountDownTimer((totalTime.toLong() + 1) * 1000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    activityStartExerciseBinding.tvCountTime.text = counter.toString()
                    activityStartExerciseBinding.stepProgressView.currentProgress = counter
                    activityStartExerciseBinding.stepProgressView.totalProgress = totalTime.toInt()
                    counter++

                    totalExerciseTime = counter -1
                    totalExerciseTime = totalExerciseTimeIntent + totalExerciseTime

                    if (totalTime.toInt() - 1 == counter || totalTime.toInt() == counter || totalTime.toInt() + 1 == counter) {
                        textToSpeechCounter((counter - 1).toString())
                    }
                    if ((totalTime.toInt() + 1) / 2 == counter) {
                        textToSpeechHalfTime()
                    }
                }

                override fun onFinish() {
                    position++


                    if (position < workoutListTableList.size) {
                        if (tts != null) {
                            tts!!.stop()
                            tts!!.shutdown()
                        }
                        val intent =
                            Intent(this@StartExerciseActivity, RestActivity::class.java)
                        intent.putExtra("exerciseList", workoutListTableList)
                        intent.putExtra("position", "" + position)
                        intent.putExtra("totalExerciseTime", "" + totalExerciseTime)
                        intent.putExtra("totalCaloriesBurned", "" + totalCalories)
                        intent.putExtra("workoutName",workoutName)

                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        startActivity(intent)
                        finish()
                    }else{
                        val intent =
                            Intent(this@StartExerciseActivity, ExerciseCompleteActivity::class.java)
                        intent.putExtra("exerciseList", workoutListTableList)
                        intent.putExtra("totalExerciseTime", "" + totalExerciseTime)
                        intent.putExtra("totalCaloriesBurned", "" + totalCalories)
                        intent.putExtra("workoutName",workoutName)

                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        startActivity(intent)
                        finish()
                    }

                }
            }.start()
        }
    }


    private fun textToSpeechHalfTime() {
        if (!mySharedPreferences.getMute()!!) {
            tts!!.speak("Half Time Left", TextToSpeech.QUEUE_FLUSH, null, "")
        }
    }

    fun onNextClick() {
       mySharedPreferences.setPauseEnable(false)
        activityStartExerciseBinding.ltvExcersieImage.playAnimation()
        if (countDownTimer != null) {
            countDownTimer.cancel()
        }
        position++
        if (position < workoutListTableList.size) {
            if (tts != null) {
                tts!!.stop()
                tts!!.shutdown()
            }
            val intent = Intent(this@StartExerciseActivity, RestActivity::class.java)
            intent.putExtra("exerciseList", workoutListTableList)
            intent.putExtra("position", "" + position)
            intent.putExtra("totalExerciseTime", "" + totalExerciseTime)
            intent.putExtra("totalCaloriesBurned", "" + totalCalories)
            intent.putExtra("workoutName",workoutName)

            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }else{
            val intent =
                Intent(this@StartExerciseActivity, ExerciseCompleteActivity::class.java)
            intent.putExtra("exerciseList", workoutListTableList)
            intent.putExtra("totalExerciseTime", "" + totalExerciseTime)
            intent.putExtra("totalCaloriesBurned", "" + totalCalories)
            intent.putExtra("workoutName",workoutName)

            intent.flags =
                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }
    }

    fun playSound() {
        if (!mySharedPreferences.getMute()!!) {

            if (mMediaPlayer == null) {
                mMediaPlayer = MediaPlayer.create(this, R.raw.whistle)
                mMediaPlayer!!.isLooping = false
                mMediaPlayer!!.start()

            } else mMediaPlayer!!.start()
        }


    }

    override fun onStop() {
        super.onStop()
        if (mMediaPlayer != null) {
            mMediaPlayer!!.release()
            mMediaPlayer = null
        }
    }

    fun onPauseClick(){
        mySharedPreferences.setPauseEnable(true)
            if (countDownTimer != null){
                countDownTimer.cancel()
            }
        activityStartExerciseBinding.ivPlay.visibility = View.VISIBLE
        activityStartExerciseBinding.ivPause.visibility = View.GONE
        activityStartExerciseBinding.ltvExcersieImage.pauseAnimation()

    }

    @SuppressWarnings("kotlin:S3776")
    //onPlayClick() method have Cognitive Complexity
    fun onPlayClick(){
        mySharedPreferences.setPauseEnable(false)
        if (countDownTimer != null) {
            countDownTimer = object : CountDownTimer((totalTime.toInt()+1 - counter).toLong() * 1000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    activityStartExerciseBinding.tvCountTime.text = counter.toString()
                    activityStartExerciseBinding.stepProgressView.currentProgress = counter
                    activityStartExerciseBinding.stepProgressView.totalProgress = totalTime.toInt()
                    counter++

                    totalExerciseTime = counter -1

                    totalExerciseTime = totalExerciseTimeIntent + totalExerciseTime

                    if (totalTime.toInt() - 1 == counter || totalTime.toInt() == counter || totalTime.toInt() + 1 == counter) {
                        textToSpeechCounter((counter - 1).toString())
                    }
                    if ((totalTime.toInt() + 1) / 2 == counter) {
                        textToSpeechHalfTime()
                    }
                }

                override fun onFinish() {
                    position++
                    if (position < workoutListTableList.size) {
                        if (tts != null) {
                            tts!!.stop()
                            tts!!.shutdown()
                        }
                        val intent =
                            Intent(this@StartExerciseActivity, RestActivity::class.java)
                        intent.putExtra("exerciseList", workoutListTableList)
                        intent.putExtra("position", "" + position)
                        intent.putExtra("totalExerciseTime", "" + totalExerciseTime)
                        intent.putExtra("totalCaloriesBurned", "" + totalCalories)
                        intent.putExtra("workoutName",workoutName)

                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        startActivity(intent)
                        finish()
                    }else{
                        val intent =
                            Intent(this@StartExerciseActivity, ExerciseCompleteActivity::class.java)
                        intent.putExtra("exerciseList", workoutListTableList)
                        intent.putExtra("totalExerciseTime", "" + totalExerciseTime)
                        intent.putExtra("totalCaloriesBurned", "" + totalCalories)
                        intent.putExtra("workoutName",workoutName)

                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        startActivity(intent)
                        finish()
                    }

                }
            }.start()
        }
        activityStartExerciseBinding.ivPause.visibility = View.VISIBLE
        activityStartExerciseBinding.ivPlay.visibility = View.GONE
        activityStartExerciseBinding.ltvExcersieImage.playAnimation()

    }

    fun onBackButtonClick() {
        onBackPressed()
    }

    override fun onInit(status: Int) {

        if (status == TextToSpeech.LANG_MISSING_DATA || status == TextToSpeech.LANG_NOT_SUPPORTED) {
            Log.e("TTS", "The Language specified is not supported!")
        } else {
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
        }
    }

    fun textToSpeech() {
        if (!mySharedPreferences.getMute()!!) {
            tts!!.speak(
                "Starts " + activityStartExerciseBinding.tvExcersieName.text.toString() + " for " + activityStartExerciseBinding.tvExerciseTime.text.toString() + "Seconds",
                TextToSpeech.QUEUE_FLUSH,
                null,
                ""
            )
        }

    }

    fun textToSpeechCounter(counter: String) {
        if (!mySharedPreferences.getMute()!!) {
            tts!!.speak("" + counter, TextToSpeech.QUEUE_FLUSH, null, "")
        }

    }

    public override fun onDestroy() {
        mySharedPreferences.setPauseEnable(false)
        if (mySharedPreferences.getPauseEnable()!!){
            activityStartExerciseBinding.ivPlay.visibility = View.VISIBLE
            activityStartExerciseBinding.ivPause.visibility = View.GONE
            activityStartExerciseBinding.ltvExcersieImage.pauseAnimation()
        }else{
            activityStartExerciseBinding.ivPlay.visibility = View.GONE
            activityStartExerciseBinding.ivPause.visibility = View.VISIBLE
            activityStartExerciseBinding.ltvExcersieImage.playAnimation()
        }
        // Shutdown TTS
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }
        super.onDestroy()
    }

    @SuppressWarnings("kotlin:S3776")
    //onBackPressed() method have cognitive complexity
    override fun onBackPressed() {
        if (backPress) {
            if (countDownTimer != null) {
                countDownTimer.cancel()
            }
            if (mySharedPreferences.getPauseEnable()!!) {
                activityStartExerciseBinding.ivPlay.visibility = View.VISIBLE
                activityStartExerciseBinding.ivPause.visibility = View.GONE
                activityStartExerciseBinding.ltvExcersieImage.pauseAnimation()
            } else {
                activityStartExerciseBinding.ivPlay.visibility = View.GONE
                activityStartExerciseBinding.ivPause.visibility = View.VISIBLE
                activityStartExerciseBinding.ltvExcersieImage.playAnimation()
            }

            activityStartExerciseBinding.ivPlay.visibility = View.VISIBLE
            activityStartExerciseBinding.ivPause.visibility = View.GONE
            mySharedPreferences.setPauseEnable(true)

            val dialogQuit = Dialog(this@StartExerciseActivity, android.R.style.Theme_Dialog)
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
                if (mySharedPreferences.getPauseEnable()!!) {
                    activityStartExerciseBinding.ivPlay.visibility = View.VISIBLE
                    activityStartExerciseBinding.ivPause.visibility = View.GONE
                    activityStartExerciseBinding.ltvExcersieImage.pauseAnimation()
                } else {
                    activityStartExerciseBinding.ivPlay.visibility = View.GONE
                    activityStartExerciseBinding.ivPause.visibility = View.VISIBLE
                    activityStartExerciseBinding.ltvExcersieImage.playAnimation()
                }
            })

            tvContinue.setOnClickListener(View.OnClickListener { view ->
                dialogQuit.dismiss()
                mySharedPreferences.setPauseEnable(false)
                if (countDownTimer != null) {
                    countDownTimer = object :
                        CountDownTimer((totalTime.toInt() + 1 - counter).toLong() * 1000, 1000) {
                        override fun onTick(millisUntilFinished: Long) {
                            activityStartExerciseBinding.tvCountTime.text = counter.toString()
                            activityStartExerciseBinding.stepProgressView.currentProgress = counter
                            activityStartExerciseBinding.stepProgressView.totalProgress = totalTime.toInt()
                            counter++

                            totalExerciseTime = counter - 1

                            totalExerciseTime = totalExerciseTimeIntent + totalExerciseTime

                            if (totalTime.toInt() - 1 == counter || totalTime.toInt() == counter || totalTime.toInt() + 1 == counter) {
                                textToSpeechCounter((counter - 1).toString())
                            }
                            if ((totalTime.toInt() + 1) / 2 == counter) {
                                textToSpeechHalfTime()
                            }
                        }

                        override fun onFinish() {
                            position++
                            if (position < workoutListTableList.size) {
                                if (tts != null) {
                                    tts!!.stop()
                                    tts!!.shutdown()
                                }
                                val intent =
                                    Intent(this@StartExerciseActivity, RestActivity::class.java)
                                intent.putExtra("exerciseList", workoutListTableList)
                                intent.putExtra("position", "" + position)
                                intent.putExtra("totalExerciseTime", "" + totalExerciseTime)
                                intent.putExtra("totalCaloriesBurned", "" + totalCalories)
                                intent.putExtra("workoutName", workoutName)

                                intent.flags =
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                                startActivity(intent)
                                finish()
                            } else {
                                val intent =
                                    Intent(
                                        this@StartExerciseActivity,
                                        ExerciseCompleteActivity::class.java
                                    )
                                intent.putExtra("exerciseList", workoutListTableList)
                                intent.putExtra("totalExerciseTime", "" + totalExerciseTime)
                                intent.putExtra("totalCaloriesBurned", "" + totalCalories)
                                intent.putExtra("workoutName", workoutName)

                                intent.flags =
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                                startActivity(intent)
                                finish()
                            }

                        }
                    }.start()
                }
                activityStartExerciseBinding.ivPause.visibility = View.VISIBLE
                activityStartExerciseBinding.ivPlay.visibility = View.GONE
                activityStartExerciseBinding.ltvExcersieImage.playAnimation()

            })

            tvQuit.setOnClickListener(View.OnClickListener { view ->
                if (backPress) {
                    dialogQuit.dismiss()
                    if (countDownTimer != null) {
                        countDownTimer.cancel()
                    }
                    super.onBackPressed()
                }
            })

            dialogQuit.show()
            dialogQuit.setCancelable(true)
        }

    }

    private fun populateUnifiedNativeAdView(
        nativeAd: NativeAd,
        adView: NativeAdView
    ) {
        // Set the media view. Media content will be automatically populated in the media view once
        // adView.setNativeAd() is called.
        val mediaView: MediaView = adView.findViewById(R.id.ad_media)
        adView.mediaView = mediaView
        mediaView.setMediaContent(nativeAd.mediaContent)
        mediaView.setImageScaleType(ImageView.ScaleType.CENTER_CROP)


        // Set other ad assets.
        adView.headlineView = adView.findViewById(R.id.ad_headline)
        adView.bodyView = adView.findViewById(R.id.ad_body)
        adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)
        adView.iconView = adView.findViewById(R.id.ad_app_icon)
        adView.priceView = adView.findViewById(R.id.ad_price)
        adView.starRatingView = adView.findViewById(R.id.ad_stars)
        adView.storeView = adView.findViewById(R.id.ad_store)
        adView.advertiserView = adView.findViewById(R.id.ad_advertiser)

        // The headline is guaranteed to be in every UnifiedNativeAd.
        (adView.headlineView as TextView).text = nativeAd.headline

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.body == null) {
            adView.bodyView.visibility = View.INVISIBLE
        } else {
            adView.bodyView.visibility = View.VISIBLE
            (adView.bodyView as TextView).text = nativeAd.body
        }
        if (nativeAd.callToAction == null) {
            adView.callToActionView.visibility = View.INVISIBLE
        } else {
            adView.callToActionView.visibility = View.VISIBLE
            (adView.callToActionView as Button).text = nativeAd.callToAction
        }
        if (nativeAd.icon == null) {
            adView.iconView.visibility = View.GONE
        } else {
            (adView.iconView as ImageView).setImageDrawable(
                nativeAd.icon.drawable
            )
            adView.iconView.visibility = View.VISIBLE
        }
        if (nativeAd.price == null) {
            adView.priceView.visibility = View.INVISIBLE
        } else {
            adView.priceView.visibility = View.VISIBLE
            (adView.priceView as TextView).text = nativeAd.price
        }
        if (nativeAd.store == null) {
            adView.storeView.visibility = View.INVISIBLE
        } else {
            adView.storeView.visibility = View.VISIBLE
            (adView.storeView as TextView).text = nativeAd.store
        }
        if (nativeAd.starRating == null) {
            adView.starRatingView.visibility = View.INVISIBLE
        } else {
            (adView.starRatingView as RatingBar).rating = nativeAd.starRating.toFloat()
            adView.starRatingView.visibility = View.VISIBLE
        }
        if (nativeAd.advertiser == null) {
            adView.advertiserView.visibility = View.INVISIBLE
        } else {
            (adView.advertiserView as TextView).text = nativeAd.advertiser
            adView.advertiserView.visibility = View.VISIBLE
        }

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad. The SDK will populate the adView's MediaView
        // with the media content from this native ad.

        adView.setNativeAd(nativeAd)


    }

    /**
     * Creates a request for a new native ad based on the boolean parameters and calls the
     * corresponding "populate" method when one is successfully returned.
     *
     */
    private fun refreshAd() {
        val builder = AdLoader.Builder(this, resources.getString(R.string.native_id))
        builder.forNativeAd (NativeAd.OnNativeAdLoadedListener {
                nativeAd1 ->
            // OnUnifiedNativeAdLoadedListener implementation.
            // You must call destroy on old ads when you are done with them,
            // otherwise you will have a memory leak.
            if (nativeAd1 != null) {
                nativeAd1!!.destroy()
            }
            nativeAd = nativeAd1
            val adView = layoutInflater
                .inflate(R.layout.ad_unified, null) as NativeAdView
            populateUnifiedNativeAdView(nativeAd, adView)
            frameLayout.removeAllViews()
            frameLayout.addView(adView)
        })

        val adLoader = builder
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    // Handle the failure by logging, altering the UI, and so on.
                }
            })
            .build()

        adLoader.loadAd(AdRequest.Builder().build())

    }

    override fun onResume() {
        if (backPress) {
            if (mySharedPreferences.getPauseEnable()!!) {
                activityStartExerciseBinding.ivPlay.visibility = View.VISIBLE
                activityStartExerciseBinding.ivPause.visibility = View.GONE
                activityStartExerciseBinding.ltvExcersieImage.pauseAnimation()
            } else {
                activityStartExerciseBinding.ivPlay.visibility = View.GONE
                activityStartExerciseBinding.ivPause.visibility = View.VISIBLE
                activityStartExerciseBinding.ltvExcersieImage.playAnimation()
            }
        }else{
            activityStartExerciseBinding.ivPause.visibility = View.GONE
            activityStartExerciseBinding.ivPlay.visibility = View.GONE
        }
        super.onResume()
    }


    override fun onPause() {
        if(backPress && countDownTimer != null) {
               countDownTimer.cancel()

        }
        if (mySharedPreferences.getPauseEnable()!!){
            activityStartExerciseBinding.ivPlay.visibility = View.VISIBLE
            activityStartExerciseBinding.ivPause.visibility = View.GONE
            activityStartExerciseBinding.ltvExcersieImage.pauseAnimation()
        }else{
            activityStartExerciseBinding.ivPlay.visibility = View.GONE
            activityStartExerciseBinding.ivPause.visibility = View.VISIBLE
            activityStartExerciseBinding.ltvExcersieImage.playAnimation()

        }


        activityStartExerciseBinding.ivPlay.visibility = View.VISIBLE
        activityStartExerciseBinding.ivPause.visibility = View.GONE
        activityStartExerciseBinding.ltvExcersieImage.pauseAnimation()
        mySharedPreferences.setPauseEnable(true)
        super.onPause()
    }

    fun onMicClick(){
        if (mySharedPreferences.getMute()!!){
            mySharedPreferences.setMute(false)
        }else{
            mySharedPreferences.setMute(true)
        }

        if (mySharedPreferences.getMute()!!){
            activityStartExerciseBinding.ivMic.setImageResource(R.drawable.ic_baseline_mic_off_24)
        }else{
            activityStartExerciseBinding.ivMic.setImageResource(R.drawable.ic_baseline_mic_24)
        }
        onInit(0)
    }
}