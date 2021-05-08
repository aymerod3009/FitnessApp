package com.qrolic.fitnessapp.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.qrolic.fitnessapp.R
import com.qrolic.fitnessapp.activity.ProfileActivity
import com.qrolic.fitnessapp.database.FitnessDatabase
import com.qrolic.fitnessapp.database.MySharedPreferences
import com.qrolic.fitnessapp.databinding.FragmentReportBinding
import com.qrolic.fitnessapp.model.ExerciseCompleteTable
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ReportFragment : Fragment() {

    private val TAG: String? = "ReportFragment"
    lateinit var db: FitnessDatabase
    var date: String = ""
    private val exerciseCompleteTableList: java.util.ArrayList<ExerciseCompleteTable> =
        java.util.ArrayList<ExerciseCompleteTable>()
    lateinit var barExerciseEntry: ArrayList<BarEntry>
    lateinit var barCaloriesEntry: ArrayList<BarEntry>
    lateinit var dateList: ArrayList<String>
    lateinit var mySharedPreferences: MySharedPreferences
     var weightInKg:Double=0.0
    lateinit var heightInCM:String
    lateinit var fragmentReportBinding: FragmentReportBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentReportBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_report, container, false)
        iniAll()
        return fragmentReportBinding.root
    }

    private fun iniAll() {

        /*
        * banner ad
        * */
        MobileAds.initialize(context) {}
        val adRequest = AdRequest.Builder().build()
        fragmentReportBinding.adView.loadAd(adRequest)

        db = FitnessDatabase.invoke(requireContext())
        mySharedPreferences = MySharedPreferences(requireContext())

        var simpleDateFormat = SimpleDateFormat("dd-MM-yyyy")
        var simpleDateFormat1 = SimpleDateFormat("d-MM-yyyy")

        date = simpleDateFormat.format(Date())

        barExerciseEntry = ArrayList<BarEntry>()
        barCaloriesEntry = ArrayList<BarEntry>()
        dateList = ArrayList<String>()

        /*
        * set sharedpref values
        * */
        setSharedPrefValues()
        /*
        * BMI calculation
        * BMI = kg/(m*m) formula
        * */

        var heightInMeter = heightInCM.toDouble() / 100
        var bmi = DecimalFormat("#.#").format((weightInKg / (heightInMeter*heightInMeter).toDouble())).toDouble()
        fragmentReportBinding.tvBMI.text = bmi.toString()
        if (bmi.toInt()> 18 && bmi.toInt()<25){
            fragmentReportBinding.tvBMIWeightStatus.text = "Healthy Weight"
            fragmentReportBinding.tvBMIWeightStatus.setTextColor(resources.getColor(R.color.green))
        }else if (bmi.toInt()<=18){
            fragmentReportBinding.tvBMIWeightStatus.text = "underWeight"
            fragmentReportBinding.tvBMIWeightStatus.setTextColor(resources.getColor(R.color.midgray))
        }else{
            fragmentReportBinding.tvBMIWeightStatus.text = "overWeight"
            fragmentReportBinding.tvBMIWeightStatus.setTextColor(resources.getColor(R.color.yellow))
        }


        fragmentReportBinding.tvEdit.setOnClickListener { onEditClick() }
        /*
        * croller
        * */

        fragmentReportBinding.croller.indicatorWidth = 10f
        fragmentReportBinding.croller.backCircleColor = Color.parseColor("#EDEDED")
        fragmentReportBinding.croller.mainCircleColor = Color.WHITE
        fragmentReportBinding.croller.max = 40
        fragmentReportBinding.croller.startOffset = 15
        fragmentReportBinding.croller.progress = bmi.toInt()
        fragmentReportBinding.croller.setIsContinuous(false)

        fragmentReportBinding.croller.labelColor = resources.getColor(R.color.midgray)
        fragmentReportBinding.croller.label =bmi.toString()
        fragmentReportBinding.croller.isContinuous
        fragmentReportBinding.croller.progressPrimaryColor = Color.parseColor("#E91E63")
        fragmentReportBinding.croller.indicatorColor = Color.parseColor("#E91E63")
        fragmentReportBinding.croller.progressSecondaryColor = resources.getColor(R.color.midgray)
        //touch click disable
        fragmentReportBinding.croller.setOnTouchListener(OnTouchListener { v, event -> true })


        /*
        * get current month's max date
        * */
        var calendar = Calendar.getInstance()
        var maxDaysOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        /*
        * get exercise data list for all dates of particular month
        * */
        for (i in (0..maxDaysOfMonth)) {
            var date = i.toString().plus(date.substring(date.indexOf("-")))
            var parseDate = simpleDateFormat1.parse(date)

            dateList.add(i.toString())
            getExerciseList(simpleDateFormat.format(parseDate), i)
        }
        Log.d(TAG, "iniAll: " + maxDaysOfMonth)

    }

    private fun setSharedPrefValues(){
        if (mySharedPreferences.getType().equals("lbft")) {
            weightInKg = DecimalFormat("#.#").format((mySharedPreferences.getWeight().toString().toDouble() / 2.205)).toDouble()
            var value =
                mySharedPreferences.getHeight().toString().substring(0, mySharedPreferences.getHeight().toString().indexOf(" FT "))
                    .trim().plus(".")
                    .plus(
                        mySharedPreferences.getHeight().toString().substring(
                            mySharedPreferences.getHeight().toString().indexOf(" FT ") + 4,
                            mySharedPreferences.getHeight().toString().indexOf("IN")
                        )
                    ).trim()

            heightInCM = DecimalFormat("#.#").format((value.toDouble() * 30.48).toDouble()).toString()
        }
        else {
            weightInKg = DecimalFormat("#.#").format(mySharedPreferences.getWeight().toString().toDouble()).toDouble()
            heightInCM = (mySharedPreferences.getHeight().toString().substring(0,mySharedPreferences.getHeight().toString().indexOf(" CM")))
        }

        fragmentReportBinding.tvUserWeight.text = weightInKg.toString()
        fragmentReportBinding.tvUserHeight.text = heightInCM.toString()
    }
    private fun getExerciseList(date: String, i: Int) {
        lifecycleScope.launch {
            this.coroutineContext.let {
                var exrciseCompleteTable = db.exerciseComplete().fetchTodayData(date)
                exerciseCompleteTableList.clear()
                exerciseCompleteTableList.addAll(exrciseCompleteTable)
                var totalWorkout: Float = 0f
                var totalCalories: Float = 0f
                for (i in exerciseCompleteTableList.iterator()) {

                    totalWorkout += i.totalExercise.toFloat()
                    totalCalories += i.burnedCalories.toFloat()

                }

                /*
                * barchart for exercise count
                * */
                barExerciseEntry.add(
                    BarEntry(
                        totalWorkout, i
                    )
                )
                var barDataSet = BarDataSet(barExerciseEntry, "Exercise")
                barDataSet.color = resources.getColor(R.color.dark_pink)

                var barData = BarData(dateList, barDataSet)

                var xAxis: XAxis? = fragmentReportBinding.barChartExercise.getXAxis()
                xAxis?.setDrawGridLines(false)
                xAxis?.position = XAxis.XAxisPosition.BOTTOM
                fragmentReportBinding.barChartExercise.animateY(5000)
                fragmentReportBinding.barChartExercise.setDescription("")
                fragmentReportBinding.barChartExercise.axisRight.isEnabled = false
                fragmentReportBinding.barChartExercise.axisLeft.gridColor = resources.getColor(R.color.midgray)
                fragmentReportBinding.barChartExercise.data = barData
                fragmentReportBinding.barChartExercise.invalidate()


                /*
                * barchart for calories count
                * */
                barCaloriesEntry.add(
                    BarEntry(
                        totalCalories, i
                    )
                )
                var barDataSetCalories = BarDataSet(barCaloriesEntry, "Calories")
                barDataSetCalories.color = resources.getColor(R.color.dark_pink)

                var barDataCalories = BarData(dateList, barDataSetCalories)

                var xCaloriesAxis: XAxis? = fragmentReportBinding.barChartCalories.getXAxis()
                xCaloriesAxis?.setDrawGridLines(false)
                xCaloriesAxis?.position = XAxis.XAxisPosition.BOTTOM
                fragmentReportBinding.barChartCalories.animateY(5000)
                fragmentReportBinding.barChartCalories.setDescription("")
                fragmentReportBinding.barChartCalories.axisRight.isEnabled = false
                fragmentReportBinding.barChartCalories.axisLeft.gridColor = resources.getColor(R.color.midgray)
                fragmentReportBinding.barChartCalories.data = barDataCalories
                fragmentReportBinding.barChartCalories.invalidate()

            }
        }
    }

    fun onEditClick() {
        val intent = Intent(requireContext(), ProfileActivity::class.java)
        intent.putExtra("type","update")
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        setSharedPrefValues()
    }
}