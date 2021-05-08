package com.qrolic.fitnessapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.qrolic.fitnessapp.R
import com.qrolic.fitnessapp.adapter.WorkoutNameListAdapter
import com.qrolic.fitnessapp.database.FitnessDatabase
import com.qrolic.fitnessapp.databinding.FragmentHomeBinding
import com.qrolic.fitnessapp.model.ExerciseCompleteTable
import com.qrolic.fitnessapp.model.WorkoutNameTable
import com.qrolic.fitnessapp.model.WorkoutTypeTable
import hotchemi.android.rate.AppRate
import kotlinx.coroutines.launch
import java.text.DecimalFormat

class HomeFragment : Fragment() {

    private val workoutNameTableList = ArrayList<WorkoutNameTable>()
    private val workoutTypeTableList1 = ArrayList<WorkoutTypeTable>()
    private val workoutTypeTableList2 = ArrayList<WorkoutTypeTable>()
    private val workoutTypeTableList3 = ArrayList<WorkoutTypeTable>()
    private val workoutTypeTableList4 = ArrayList<WorkoutTypeTable>()
    private val workoutTypeTableList5 = ArrayList<WorkoutTypeTable>()
    private val workoutTypeTableList6 = ArrayList<WorkoutTypeTable>()
    private lateinit var workoutNameListAdapter: WorkoutNameListAdapter
    lateinit var db: FitnessDatabase
    var date:String = ""
    private val exerciseCompleteTableList: java.util.ArrayList<ExerciseCompleteTable> =
        java.util.ArrayList<ExerciseCompleteTable>()
    var workouts7:String ="7 Workouts"
    lateinit var fragmentHomeBinding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragmentHomeBinding=DataBindingUtil.inflate(inflater,R.layout.fragment_home, container, false)
        initAll()
        return fragmentHomeBinding.root
    }

    private fun initAll() {
        db = FitnessDatabase.invoke(requireContext())

        getExerciseList()

        workoutNameListAdapter = WorkoutNameListAdapter(workoutNameTableList,context)
        val layoutManager = LinearLayoutManager(context)
        fragmentHomeBinding.rvWorkoutNameList.layoutManager = layoutManager
        fragmentHomeBinding.rvWorkoutNameList.itemAnimator = DefaultItemAnimator()
        fragmentHomeBinding.rvWorkoutNameList.adapter = workoutNameListAdapter

        addWorkoutNameData()
    }

    private fun addWorkoutNameData() {

        if (workoutNameTableList.isEmpty()) {


            var workoutTypeTable1 = WorkoutTypeTable(
                getString(R.string.arm_intermediate),
                (R.drawable.arm_advanced),
                workouts7,
                "arm intermediate"
            )
            workoutTypeTableList1.add(workoutTypeTable1)
            var workoutNameTable =
                WorkoutNameTable(getString(R.string.arm_workout), workoutTypeTableList1)
            workoutNameTableList.add(workoutNameTable)

            var workoutTypeTable2 = WorkoutTypeTable(
                getString(R.string.LEG_BEGINNER),
                (R.drawable.leg_beginner),
                "13 Workouts",
                "leg beginner"
            )
            workoutTypeTableList2.add(workoutTypeTable2)
            workoutTypeTable2 = WorkoutTypeTable(
                getString(R.string.LEG_INTERMEDIATE),
                (R.drawable.leg_intermediate),
                "12 Workouts",
                "leg intermediate"
            )
            workoutTypeTableList2.add(workoutTypeTable2)
            workoutTypeTable2 = WorkoutTypeTable(
                getString(R.string.leg_advanced),
                (R.drawable.leg_advanced),
                "13 Workouts",
                "leg advanced"
            )
            workoutTypeTableList2.add(workoutTypeTable2)
            workoutNameTable =
                WorkoutNameTable(getString(R.string.leg_workout), workoutTypeTableList2)
            workoutNameTableList.add(workoutNameTable)

            var workoutTypeTable3 = WorkoutTypeTable(
                getString(R.string.s_and_b_intermediate),
                R.drawable.yoga_intermediate,
                "11 Workouts",
                "shoulder and back intermediate"
            )
            workoutTypeTableList3.add(workoutTypeTable3)
            workoutTypeTable3 = WorkoutTypeTable(
                getString(R.string.s_and_b_advanced),
                R.drawable.s_and_b_advanced,
                workouts7,
                "shoulder and back advanced"
            )
            workoutTypeTableList3.add(workoutTypeTable3)
            workoutNameTable =
                WorkoutNameTable(getString(R.string.s_and_b_workout), workoutTypeTableList3)
            workoutNameTableList.add(workoutNameTable)

            var workoutTypeTable4 = WorkoutTypeTable(
                getString(R.string.yoga_beginner),
                R.drawable.yoga_beginner,
                "4 Workouts",
                "yoga beginner"
            )
            workoutTypeTableList4.add(workoutTypeTable4)
            workoutNameTable =
                WorkoutNameTable(getString(R.string.yoga_workout), workoutTypeTableList4)
            workoutNameTableList.add(workoutNameTable)

            var workoutTypeTable5 = WorkoutTypeTable(
                getString(R.string.chest_beginner),
                (R.drawable.arm_intermediate),
                "9 Workouts",
                "chest beginner"
            )
            workoutTypeTableList5.add(workoutTypeTable5)
            workoutTypeTable5 = WorkoutTypeTable(
                getString(R.string.chest_intermediate),
                (R.drawable.body_beginner),
                "15 Workouts",
                "chest intermediate"
            )
            workoutTypeTableList5.add(workoutTypeTable5)
            workoutTypeTable5 = WorkoutTypeTable(
                getString(R.string.chest_advanced),
                (R.drawable.body),
                "10 Workouts",
                "chest advanced"
            )
            workoutTypeTableList5.add(workoutTypeTable5)
            workoutNameTable =
                WorkoutNameTable(getString(R.string.chest_workout), workoutTypeTableList5)
            workoutNameTableList.add(workoutNameTable)

            var workoutTypeTable6 = WorkoutTypeTable(
                getString(R.string.abd_beginner),
                (R.drawable.abs_beginner),
                workouts7,
                "abs beginner"
            )
            workoutTypeTableList6.add(workoutTypeTable6)
            workoutTypeTable6 = WorkoutTypeTable(
                getString(R.string.abd_intermediate),
                (R.drawable.abs_intermediate),
                "10 Workouts",
                "abs intermediate"
            )
            workoutTypeTableList6.add(workoutTypeTable6)
            workoutTypeTable6 = WorkoutTypeTable(
                getString(R.string.abd_advanced),
                (R.drawable.abs_advanced),
                "9 Workouts",
                "abs advanced"
            )
            workoutTypeTableList6.add(workoutTypeTable6)
            workoutNameTable =
                WorkoutNameTable(getString(R.string.abs_workout), workoutTypeTableList6)
            workoutNameTableList.add(workoutNameTable)
        }
        rateus()

    }

    private fun rateus() {
        // Here 0 means
        // the installation date.
        AppRate.with(context)

            // default 10
            .setInstallDays(0)

            // default 10
            .setLaunchTimes(3)

            // default 1
            .setRemindInterval(1)
            .monitor();

        // Show a dialogue
        // if meets conditions
        AppRate
            .showRateDialogIfMeetsConditions(
                requireActivity());

    }

    private fun getExerciseList() {
        lifecycleScope.launch {
            this.coroutineContext.let {
                var exrciseCompleteTable  = db.exerciseComplete().fetchAllData()
                exerciseCompleteTableList.clear()
                exerciseCompleteTableList.addAll(exrciseCompleteTable)
                var exerciseTime = 0.0
                var burnedCalories = 0.0
                for (i in exerciseCompleteTableList.iterator()){

                    exerciseTime +=  i.totalTime.replace(":",".").toDouble()

                    burnedCalories +=  i.burnedCalories.toDouble()
                }
                fragmentHomeBinding.tvTotalTime.text = DecimalFormat("#.##").format(exerciseTime).toString().replace(".",":")
                fragmentHomeBinding.tvCaloriesBurned.text = DecimalFormat("#.##").format(burnedCalories).toString()
                fragmentHomeBinding.tvTotalWorkout.text = exerciseCompleteTableList.size.toString()

            }
        }
    }

    override fun onResume() {
        getExerciseList()
        super.onResume()
    }
}