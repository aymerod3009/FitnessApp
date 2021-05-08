package com.qrolic.fitnessapp.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnTouchListener
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.qrolic.fitnessapp.R
import com.qrolic.fitnessapp.adapter.AppIntroViewPagerAdapter
import com.qrolic.fitnessapp.database.MySharedPreferences
import com.qrolic.fitnessapp.databinding.ActivityAppIntroBinding


class AppIntroActivity : AppCompatActivity() {


    var appIntroViewPagerAdapter: AppIntroViewPagerAdapter? = null
    private val layouts = intArrayOf(
        R.layout.intro_slide1,
        R.layout.intro_slide2,
        R.layout.intro_slide3
    )
    lateinit var mySharedPreferences: MySharedPreferences
    lateinit var activityAppIntroBinding:ActivityAppIntroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityAppIntroBinding =DataBindingUtil.setContentView(this,R.layout.activity_app_intro)
        init()
    }

    private fun init() {

        supportActionBar?.hide()

        mySharedPreferences = MySharedPreferences(this)

        if (!mySharedPreferences.isFirstRun()){
            launchHomeScreen()
            finish()
        }

        // progress bar
        val resources = resources
        val drawable = ResourcesCompat.getDrawable(resources,R.drawable.circularprogressbar,null)
        activityAppIntroBinding.circularProgressbar.progress = 0
        activityAppIntroBinding.circularProgressbar.secondaryProgress = 25
        activityAppIntroBinding.circularProgressbar.max = 100
        activityAppIntroBinding.circularProgressbar.progressDrawable = drawable

        // viewpgaer adapter
        appIntroViewPagerAdapter = AppIntroViewPagerAdapter(this, layouts)
        activityAppIntroBinding.viewPager.adapter = appIntroViewPagerAdapter
        activityAppIntroBinding.viewPager.addOnPageChangeListener(viewPagerPageChangeListener)
        //stop finger swipe in viewpager
        activityAppIntroBinding.viewPager.setOnTouchListener(OnTouchListener { view, motionEvent -> true })
    }

    var viewPagerPageChangeListener: OnPageChangeListener = object : OnPageChangeListener {
        override fun onPageSelected(position: Int) {

            if (position == layouts.size - 1) {
                activityAppIntroBinding.circularProgressbar.progress = 75
                activityAppIntroBinding.ivNext.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.ic_baseline_check_24,null))
                activityAppIntroBinding.tvSkip.setVisibility(View.INVISIBLE)
            } else {
                // still pages are left
                activityAppIntroBinding.circularProgressbar.progress +=  25
                activityAppIntroBinding.ivNext.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.ic_baseline_navigate_next_24,null))
                activityAppIntroBinding.tvSkip.setVisibility(View.VISIBLE)
            }
        }

        override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {
            //  This method will be invoked when the current page is scrolled, either as part of a programmatically initiated smooth scroll or a user initiated touch scroll.

        }

        override fun onPageScrollStateChanged(arg0: Int) {
            // Called when the scroll state changes.
        }
    }

    private fun getItem(i: Int): Int {
        return activityAppIntroBinding.viewPager.currentItem + i
    }


    fun onViewClicked(view: View) {
        when (view.id) {
            R.id.ivNext -> {
                // checking for last page
                // if last page home screen will be launched
                val current = getItem(+1)
                if (current < layouts.size) {
                    // move to next screen
                    activityAppIntroBinding.viewPager.currentItem = current
                } else {
                    launchHomeScreen()
                }
            }
            R.id.tvSkip -> launchHomeScreen()
            else -> launchHomeScreen()
        }
    }

    private fun launchHomeScreen() {
        mySharedPreferences.setFirstRun()
        val intent = Intent(this@AppIntroActivity,ProfileActivity::class.java)
        intent.putExtra("type","save")
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }


}