package com.qrolic.fitnessapp.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.qrolic.fitnessapp.R
import com.qrolic.fitnessapp.adapter.ExerciseCompleteListAdapter
import com.qrolic.fitnessapp.database.FitnessDatabase
import com.qrolic.fitnessapp.databinding.FragmentHistoryBinding
import com.qrolic.fitnessapp.model.ExerciseCompleteTable
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class HistoryFragment : Fragment() {

    private val TAG: String ="HistoryFragment"
    private lateinit var exerciseCompleteListAdapter: ExerciseCompleteListAdapter
    private val exerciseCompleteTableList: ArrayList<ExerciseCompleteTable> = ArrayList<ExerciseCompleteTable>()
    var date:String = ""
    lateinit var db: FitnessDatabase
    lateinit var fragmentHistoryBinding:FragmentHistoryBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

       fragmentHistoryBinding=DataBindingUtil.inflate(inflater,R.layout.fragment_history, container, false)
        initAll()
        return fragmentHistoryBinding.root
    }

    private fun initAll() {
        db = FitnessDatabase.invoke(requireContext())

        var simpleDateFormat = SimpleDateFormat("dd-MM-yyyy")

       /*
       * for date change
       * */
        fragmentHistoryBinding.calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar[year, month] = dayOfMonth
            val sDate = simpleDateFormat.format(calendar.time)
            Log.d(TAG, "sDate formatted: $sDate")
            fragmentHistoryBinding.tvDate.text = sDate
            getExerciseList((sDate))

        }

        /*
        current date
        * */
        date = simpleDateFormat.format(Date())
        fragmentHistoryBinding.tvDate.text = date

        exerciseCompleteListAdapter = ExerciseCompleteListAdapter(exerciseCompleteTableList,context)
        val layoutManager = LinearLayoutManager(context)
        fragmentHistoryBinding.rvExerciseList.layoutManager = layoutManager
        fragmentHistoryBinding.rvExerciseList.itemAnimator = DefaultItemAnimator()
        fragmentHistoryBinding.rvExerciseList.adapter = exerciseCompleteListAdapter

        getExerciseList(date)

    }

    private fun getExerciseList(date:String) {
        lifecycleScope.launch {
            this.coroutineContext.let {
                var exrciseCompleteTable  = db.exerciseComplete().fetchTodayData(date)
                Log.d(TAG, "getExerciseList: date"+date)
                exerciseCompleteTableList.clear()
                exerciseCompleteTableList.addAll(exrciseCompleteTable)
                var exerciseTime = 0.0
                var burnedCalories = 0.0
                for (i in exerciseCompleteTableList.iterator()){

                     exerciseTime +=  i.totalTime.replace(":",".").toDouble()

                    burnedCalories +=  i.burnedCalories.toDouble()
            }
                fragmentHistoryBinding.tvExerciseTotalTime.text = DecimalFormat("#.##").format(exerciseTime).toString().replace(".",":")
                fragmentHistoryBinding.tvExerciseBurnedCalories.text = DecimalFormat("#.##").format(burnedCalories).toString()
                fragmentHistoryBinding.tvWorkoutCount.text = exerciseCompleteTableList.size.toString().plus(" workouts")
                exerciseCompleteListAdapter.notifyDataSetChanged()
            }
        }
    }
}