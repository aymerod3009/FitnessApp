package com.qrolic.fitnessapp.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.widget.SwitchCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.qrolic.fitnessapp.BuildConfig
import com.qrolic.fitnessapp.R
import com.qrolic.fitnessapp.activity.PrivacyPolicyActivity
import com.qrolic.fitnessapp.activity.ProfileActivity
import com.qrolic.fitnessapp.activity.ReminderActivity
import com.qrolic.fitnessapp.activity.WorkoutBenifitsActivity
import com.qrolic.fitnessapp.database.MySharedPreferences
import com.qrolic.fitnessapp.databinding.FragmentProfileBinding
import java.util.*


class ProfileFragment() : Fragment(), TextToSpeech.OnInitListener {

    private lateinit var languages: Array<String>
    lateinit var tvRestSeconds: TextView
    lateinit var tvTitle: TextView
    lateinit var tvCancel: TextView
    lateinit var tvOk: TextView
    lateinit var tvSet: TextView
    lateinit var ivplus: ImageView
    lateinit var ivMinus: ImageView
    lateinit var switchCompact: SwitchCompat
    lateinit var mySharedPreferences: MySharedPreferences
    lateinit var type: String
    private var tts: TextToSpeech? = null
    lateinit var fragmentProfileBinding:FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragmentProfileBinding=DataBindingUtil.inflate(inflater,R.layout.fragment_profile, container, false)
        initAll()
        return fragmentProfileBinding.root
    }

    private fun initAll() {
        /*
        * banner ad
        * */
        MobileAds.initialize(context) {}
        val adRequest = AdRequest.Builder().build()
        fragmentProfileBinding.adView.loadAd(adRequest)


        mySharedPreferences = MySharedPreferences(requireContext())
        tts = TextToSpeech(context, this)

        fragmentProfileBinding.tvRestSetSeconds.text = mySharedPreferences.getRestSetTime()
        fragmentProfileBinding.tvCountDownTime.text = mySharedPreferences.getCountDownTime()

        if (mySharedPreferences.getScreenOn()!!) {
            fragmentProfileBinding.switchDeviceScreen.isChecked = true
        } else {
            fragmentProfileBinding.switchDeviceScreen.isChecked = false
        }

        fragmentProfileBinding.tvReminderDateTime.text = mySharedPreferences.getReminderTime()

        fragmentProfileBinding.switchDeviceScreen.setOnCheckedChangeListener({ _, isChecked ->
            if (isChecked) {
                mySharedPreferences.setScreenOn(true)
            } else {
                mySharedPreferences.setScreenOn(false)
            }
        })

        languages = resources.getStringArray(R.array.Languages)


        if (fragmentProfileBinding.spLanguage != null) {
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1, languages
            )
            fragmentProfileBinding.spLanguage.adapter = adapter

            for (i in 0..languages.size - 1) {
                if (languages[i].equals(mySharedPreferences.getLocaleLanguage())) {
                    fragmentProfileBinding.spLanguage.setSelection(i)
                    adapter.notifyDataSetChanged()
                }
            }

            fragmentProfileBinding.spLanguage?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // onNothingSelected() method for spinner item
                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    mySharedPreferences.setLocalLanguage(languages[position])

                }

            }

            /*
            * spinner text color set
            * */
            fragmentProfileBinding.spLanguage.getViewTreeObserver()
                .addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        (fragmentProfileBinding.spLanguage.getSelectedView() as TextView).setTextColor(Color.GRAY) //change to your color
                    }
                })
        }


        fragmentProfileBinding.llRestSet.setOnClickListener { onRestSetClick() }
        fragmentProfileBinding.llCountDown.setOnClickListener { onCountDownClick() }
        fragmentProfileBinding.llSoundOptions.setOnClickListener { onSoundOptionClick() }
        fragmentProfileBinding.llWorkoutBenefits.setOnClickListener { onWorkoutBenefits() }
        fragmentProfileBinding.llReminder.setOnClickListener { onReminderClick() }
        fragmentProfileBinding.llTestVoice.setOnClickListener { onTestVoiceClick() }
        fragmentProfileBinding.llRate.setOnClickListener { onRateClick() }
        fragmentProfileBinding.llShare.setOnClickListener { onShareClick() }
        fragmentProfileBinding.llFeedback.setOnClickListener { onFeedBackClick() }
        fragmentProfileBinding.llPrivacyPolicy.setOnClickListener { onPrivacyPolicyClick() }
        createNotiChannel()
    }

    fun onRestSetClick() {
        type = "restSet"
        dialogRestSet()
    }

    fun onCountDownClick() {
        type = "countDown"
        dialogRestSet()
    }

    fun onMyProfileClick() {
        val intent = Intent(requireContext(), ProfileActivity::class.java)
        intent.putExtra("type", "update")
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    fun onSoundOptionClick() {
        dialogSoundOptions()
    }

    public fun dialogSoundOptions() {
        val dialogSoundOptions = Dialog(requireContext(), android.R.style.Theme_Dialog)
        dialogSoundOptions.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogSoundOptions.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogSoundOptions.setContentView(R.layout.dialog_sound_options)

        tvOk = dialogSoundOptions.findViewById<View>(R.id.tvOk) as TextView
        switchCompact = dialogSoundOptions.findViewById<View>(R.id.switchCompact) as SwitchCompat

        if (mySharedPreferences.getMute()!!) {
            switchCompact.isChecked = true
        } else {
            switchCompact.isChecked = false

        }
        switchCompact.setOnCheckedChangeListener({ _, isChecked ->
            if (isChecked) {
                mySharedPreferences.setMute(true)
            } else {
                mySharedPreferences.setMute(false)

            }
        })

        tvOk.setOnClickListener(View.OnClickListener { view ->
            dialogSoundOptions.dismiss()
        }
        )

        dialogSoundOptions.show()
        dialogSoundOptions.setCancelable(true)

    }

    @SuppressWarnings(
            "kotlin:S3776")
    //dialogRestSet() method have cognitive complexity
    private fun dialogRestSet() {
        val dialogRestSet = Dialog(requireContext(), android.R.style.Theme_Dialog)
        dialogRestSet.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogRestSet.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogRestSet.setContentView(R.layout.dialog_rest_set)

        tvRestSeconds = dialogRestSet.findViewById<View>(R.id.tvRestSeconds) as TextView
        tvTitle = dialogRestSet.findViewById<View>(R.id.tvTitle) as TextView
        ivplus = dialogRestSet.findViewById<View>(R.id.ivplus) as ImageView
        ivMinus = dialogRestSet.findViewById<View>(R.id.ivMinus) as ImageView
        tvCancel = dialogRestSet.findViewById<View>(R.id.tvCancel) as TextView
        tvSet = dialogRestSet.findViewById<View>(R.id.tvSet) as TextView

        dialogRestSet.show()
        dialogRestSet.setCancelable(true)

        var secs: Int

        if (type.equals("countDown")) {
            tvTitle.text = "Set duration(10 ~ 30 secs)"
            tvRestSeconds.text = fragmentProfileBinding.tvCountDownTime.text.toString()
            secs = tvRestSeconds.text.toString().toInt()

        } else {
            tvTitle.text = "Set duration(5 ~ 180 secs)"
            tvRestSeconds.text = fragmentProfileBinding.tvRestSetSeconds.text.toString()
            secs = tvRestSeconds.text.toString().toInt()

        }

        tvCancel.setOnClickListener(View.OnClickListener { view ->
            dialogRestSet.dismiss()
        })

        ivplus.setOnClickListener(View.OnClickListener { view ->
            if (type.equals("countDown")) {
                if (secs < 30) {
                    secs += 1
                }
            } else {
                if (secs < 180) {
                    secs += 1
                }
            }
            tvRestSeconds.text = secs.toString()
        })

        ivMinus.setOnClickListener(View.OnClickListener { view ->
            if (type.equals("countDown")) {
                if (secs > 10) {
                    secs -= 1
                }
            } else {
                if (secs > 5) {
                    secs -= 1
                }
            }
            tvRestSeconds.text = secs.toString()

        })

        tvSet.setOnClickListener(View.OnClickListener { view ->
            dialogRestSet.dismiss()
            if (type.equals("countDown")) {
                fragmentProfileBinding.tvCountDownTime.text = secs.toString()
                mySharedPreferences.setCountDownTime(fragmentProfileBinding.tvCountDownTime.text.toString())
            } else {
                fragmentProfileBinding.tvRestSetSeconds.text = secs.toString()
                mySharedPreferences.setRestSetTime(fragmentProfileBinding.tvRestSetSeconds.text.toString())
            }
        })

    }

    fun onWorkoutBenefits(){
        val intent = Intent(context,WorkoutBenifitsActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }

    fun onReminderClick() {
        val intent = Intent(context,ReminderActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)

    }

    private fun createNotiChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            var name = "Fitness channel"
            var desc = "Fitness description"
            var importance = NotificationManager.IMPORTANCE_DEFAULT
            var channel = NotificationChannel("noti", name, importance)
            channel.description = desc

            var notificationManager = context?.getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }
    }


    fun onTestVoiceClick() {
        val builder = AlertDialog.Builder(context)

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
            "Did you hear the test voice ?",
            TextToSpeech.QUEUE_FLUSH,
            null,
            ""
        )

        builder.setMessage("Did you hear the test voice ?")

        builder.setPositiveButton("Yes") { dialogInterface, which ->

        }
        builder.setNegativeButton("No") { dialogInterface, which ->
            Toast.makeText(
                context,
                "The TTS engine you are using is not working now, please double check it, switch to another TTS engine, or download TTS voice language.",
                Toast.LENGTH_LONG
            ).show()
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(true)
        alertDialog.show()
    }


    override fun onInit(status: Int) {
        // set UK English as language for tts

        if (status == TextToSpeech.LANG_MISSING_DATA || status == TextToSpeech.LANG_NOT_SUPPORTED) {
            Log.e("TTS", "The Language specified is not supported!")
        }

    }

    public fun onRateClick() {
        preventTwoClick(fragmentProfileBinding.llRate)
        try {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + BuildConfig.APPLICATION_ID)
                )
            )
        } catch (anfe: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID)
                )
            )
        }
    }

    fun onShareClick() {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_SUBJECT, "Android Studio Pro")
        intent.putExtra(
            Intent.EXTRA_TEXT,
            "https://play.google.com/store/apps/details?id="+BuildConfig.APPLICATION_ID
        )
        intent.type = "text/plain"
        startActivity(intent)
    }

    fun onFeedBackClick(){
        val intent = Intent(Intent.ACTION_SEND)
        val recipients = arrayOf("info@qrolic.com")
        intent.putExtra(Intent.EXTRA_EMAIL, recipients)
        intent.putExtra(Intent.EXTRA_SUBJECT, "Fitness App")
        intent.putExtra(Intent.EXTRA_TEXT, "Feedback : ")
        intent.putExtra(Intent.EXTRA_CC, getString(R.string.email))
        intent.type = "text/html"
        intent.setPackage("com.google.android.gm")
        startActivity(Intent.createChooser(intent, "Send mail"))
    }

    fun onPrivacyPolicyClick(){
        val intent = Intent(context,PrivacyPolicyActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }

    //preventTwoClick
    fun preventTwoClick(view: View) {
        view.isEnabled = false
        view.postDelayed({ view.isEnabled = true }, 2000)
    }

    public override fun onDestroy() {
        // Shutdown TTS
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }
        super.onDestroy()
    }


}


