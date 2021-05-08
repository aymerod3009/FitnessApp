package com.qrolic.fitnessapp.activity

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.google.android.gms.ads.*
import com.qrolic.fitnessapp.R
import com.qrolic.fitnessapp.adapter.WorkoutListAdapter
import com.qrolic.fitnessapp.databinding.ActivityWorkoutListBinding
import com.qrolic.fitnessapp.model.WorkoutListTable
import kotlin.properties.Delegates

class WorkoutListActivity : AppCompatActivity() {

    private val workoutListTableList: ArrayList<WorkoutListTable> = ArrayList<WorkoutListTable>()
    lateinit var workoutName: String
    lateinit var workoutTypeName: String
    lateinit var workoutTypeTime: String
    lateinit var workoutType: String
    var workoutTypeImage by Delegates.notNull<Int>()
    private lateinit var workoutListAdapter: WorkoutListAdapter
    var positionForWorkout: Int = 0
    lateinit var activityWorkoutListBinding: ActivityWorkoutListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityWorkoutListBinding=DataBindingUtil.setContentView(this,R.layout.activity_workout_list)
        initAll()
    }

    private fun initAll() {

        supportActionBar?.hide()

        /*
        * banner ad
        * */
//        MobileAds.initialize(this) {}
//        val adRequest = AdRequest.Builder().build()
//        activityWorkoutListBinding.adView.loadAd(adRequest)


        workoutName = intent.getStringExtra("workoutName").toString()
        workoutTypeName = intent.getStringExtra("workoutTypeName").toString()
        workoutTypeImage = intent.getIntExtra("workoutTypeImage", -1)
        workoutTypeTime = intent.getStringExtra("workoutTypeTime").toString()
        workoutType = intent.getStringExtra("workoutType").toString()

        activityWorkoutListBinding.tvWorkoutTypeName.text = workoutTypeName
        activityWorkoutListBinding.tvWorkoutTypeTime.text = workoutTypeTime

        activityWorkoutListBinding.tvActionBarWorkoutTypeName.text = workoutTypeName
        activityWorkoutListBinding.tvActionBarWorkoutTypeTime.text = workoutTypeTime

        Glide.with(applicationContext)
            .load(workoutTypeImage)
            .centerCrop()
            .into(activityWorkoutListBinding.ivWorkoutTypeImage);

        workoutListAdapter = WorkoutListAdapter(
            workoutListTableList,
            object : WorkoutListAdapter.OnTypeClickListner {
                override fun onItemClick(position1: Int) {
                    positionForWorkout = position1
                    dialogWorkout(
                        workoutListTableList,
                        position1
                    )
                }
            })
        val layoutManager = LinearLayoutManager(applicationContext)

        activityWorkoutListBinding.rvWorkoutList.layoutManager = layoutManager
        activityWorkoutListBinding.rvWorkoutList.itemAnimator = DefaultItemAnimator()
        activityWorkoutListBinding.rvWorkoutList.adapter = workoutListAdapter

        /*
     get execrise list according to types
    */
        if (workoutType.equals("arm intermediate")) {
            addArmIntermediate()
        } else if (workoutType.equals("leg beginner")) {
            addLegBeginner()
        } else if (workoutType.equals("leg intermediate")) {
            addLegIntermediate()
        } else if (workoutType.equals("leg advanced")) {
            addLegAdvanced()
        } else if (workoutType.equals("chest beginner")) {
            addChestBeginner()
        } else if (workoutType.equals("chest intermediate")) {
            addChestIntermediate()
        } else if (workoutType.equals("chest advanced")) {
            addChestAdvanced()
        } else if (workoutType.equals("abs beginner")) {
            addAbsBeginner()
        } else if (workoutType.equals("abs intermediate")) {
            addAbsIntermediate()
        }  else if (workoutType.equals("abs advanced")) {
            addAbsAdvanced()
        } else if (workoutType.equals("shoulder and back intermediate")) {
            addShoulderBackIntermediate()
        }  else if (workoutType.equals("shoulder and back advanced")) {
            addShoulderBackAdvanced()
        } else if (workoutType.equals("yoga beginner")) {
            addYogaBeginnerList()
        } else {
            addArmIntermediate()
        }

        activityWorkoutListBinding.llStart.setOnClickListener { onStartClick() }

    }

    /*
    * yoga beginner list
    * */
    private fun addYogaBeginnerList() {

        var workoutListTable = WorkoutListTable(
            getString(R.string.yoga_lotus_pose),
            getString(R.string.yoga_lotus_pose_json),
            getString(R.string.yoga_lotus_time),
            getString(R.string.yoga_lotus_MET),
            getString(R.string.yoga_lotus_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.yoga_tree_pose),
            getString(R.string.yoga_tree_pose_json),
            getString(R.string.yoga_tree_time),
            getString(R.string.yoga_tree_MET),
            getString(R.string.yoga_tree_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.yoga_child_pose),
            getString(R.string.yoga_child_pose_json),
            getString(R.string.yoga_child_time),
            getString(R.string.yoga_child_MET),
            getString(R.string.yoga_child_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.yoga_cobra_pose),
            getString(R.string.yoga_cobra_pose_json),
            getString(R.string.yoga_cobra_time),
            getString(R.string.yoga_cobra_MET),
            getString(R.string.yoga_cobra_desc)
        )
        workoutListTableList.add(workoutListTable)


    }

    /*
   * arm workout list
   * */
    private fun addArmIntermediate() {
        var workoutListTable = WorkoutListTable(
            getString(R.string.arm_dynamic_chest),
            getString(R.string.arm_dynamic_chest_stretch_json),
            getString(R.string.arm_dynamic_chest_time),
            getString(R.string.arm_dynamic_chest_MET),
            getString(R.string.arm_dynamic_chest_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.arm_jumping_jacks),
            getString(R.string.arm_jumping_jacks_json),
            getString(R.string.arm_jumping_jacks_time),
            getString(R.string.arm_jumping_jacks_MET),
            getString(R.string.arm_jumping_jacks_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.arm_shoulder_rotation),
            getString(R.string.arm_shoulder_rotation_json),
            getString(R.string.arm_shoulder_rotation_time),
            getString(R.string.arm_shoulder_rotation_MET),
            getString(R.string.arm_shoulder_rotation_desc)
        )
        workoutListTableList.add(workoutListTable)


        workoutListTable = WorkoutListTable(
            getString(R.string.arm_dynamic_chest_shoulders_strech),
            getString(R.string.arm_dynamic_chest_shoulders_strech_json),
            getString(R.string.arm_dynamic_chest_shoulders_strech_time),
            getString(R.string.arm_dynamic_chest_shoulders_strech_MET),
            getString(R.string.arm_dynamic_chest_shoulders_strech_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.arm_chest_shoulders_strech),
            getString(R.string.arm_chest_shoulders_strech_json),
            getString(R.string.arm_chest_shoulders_strech_time),
            getString(R.string.arm_chest_shoulders_strech_MET),
            getString(R.string.arm_chest_shoulders_strech_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.arm_neck_strech_left),
            getString(R.string.arm_neck_stretch_left_json),
            getString(R.string.arm_neck_stretch_left_time),
            getString(R.string.arm_neck_stretch_left_MET),
            getString(R.string.arm_neck_stretch_left_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.arm_neck_strech_right),
            getString(R.string.arm_neck_stretch_right_json),
            getString(R.string.arm_neck_stretch_right_time),
            getString(R.string.arm_neck_stretch_right_MET),
            getString(R.string.arm_neck_stretch_right_desc)
        )
        workoutListTableList.add(workoutListTable)


    }

    /*
   * abs beginner
   * */
    private fun addAbsBeginner(){
        var workoutListTable = WorkoutListTable(
            getString(R.string.arm_jumping_jacks),
            getString(R.string.arm_jumping_jacks_json),
            getString(R.string.arm_jumping_jacks_time),
            getString(R.string.arm_jumping_jacks_MET),
            getString(R.string.arm_jumping_jacks_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.yoga_cobra_pose),
            getString(R.string.yoga_cobra_pose_json),
            getString(R.string.yoga_cobra_time),
            getString(R.string.yoga_cobra_MET),
            getString(R.string.yoga_cobra_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.abs_diagonal_mountain_climbers),
            getString(R.string.abs_diagonal_mountain_climbers_json),
            getString(R.string.abs_diagonal_mountain_climbers_time),
            getString(R.string.abs_diagonal_mountain_climbers_MET),
            getString(R.string.abs_diagonal_mountain_climbers_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.abs_russian_twists),
            getString(R.string.abs_russian_twists_json),
            getString(R.string.abs_russian_twists_time),
            getString(R.string.abs_russian_twists_MET),
            getString(R.string.abs_russian_twists_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.abs_spinal_rotation_left),
            getString(R.string.abs_spinal_rotation_left_json),
            getString(R.string.abs_spinal_rotation_left_time),
            getString(R.string.abs_spinal_rotation_left_MET),
            getString(R.string.abs_spinal_rotation_left_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.abs_spinal_rotation_right),
            getString(R.string.abs_spinal_rotation_right_json),
            getString(R.string.abs_spinal_rotation_right_time),
            getString(R.string.abs_spinal_rotation_right_MET),
            getString(R.string.abs_spinal_rotation_right_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.abs_cocoons),
            getString(R.string.abs_cocoons_json),
            getString(R.string.abs_cocoons_time),
            getString(R.string.abs_cocoons_MET),
            getString(R.string.abs_cocoons_desc)
        )
        workoutListTableList.add(workoutListTable)
    }

    /*
    * abs advanced
    * */
    private fun addAbsAdvanced(){
        var workoutListTable = WorkoutListTable(
            getString(R.string.arm_jumping_jacks),
            getString(R.string.arm_jumping_jacks_json),
            getString(R.string.arm_jumping_jacks_time),
            getString(R.string.arm_jumping_jacks_MET),
            getString(R.string.arm_jumping_jacks_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.abs_diagonal_mountain_climbers),
            getString(R.string.abs_diagonal_mountain_climbers_json),
            getString(R.string.abs_diagonal_mountain_climbers_time),
            getString(R.string.abs_diagonal_mountain_climbers_MET),
            getString(R.string.abs_diagonal_mountain_climbers_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.abs_wipers),
            getString(R.string.abs_wipers_json),
            getString(R.string.abs_wipers_time),
            getString(R.string.abs_wipers_MET),
            getString(R.string.abs_wipers_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.abs_elbow_to_knee_crunch_left),
            getString(R.string.abs_elbow_to_knee_crunch_left_json),
            getString(R.string.abs_elbow_to_knee_crunch_left_time),
            getString(R.string.abs_elbow_to_knee_crunch_left_MET),
            getString(R.string.abs_elbow_to_knee_crunch_left_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.abs_elbow_to_knee_crunch_right),
            getString(R.string.abs_elbow_to_knee_crunch_right_json),
            getString(R.string.abs_elbow_to_knee_crunch_right_time),
            getString(R.string.abs_elbow_to_knee_crunch_right_MET),
            getString(R.string.abs_elbow_to_knee_crunch_right_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.abs_groin_crunch),
            getString(R.string.abs_groin_crunch_json),
            getString(R.string.abs_groin_crunch_time),
            getString(R.string.abs_groin_crunch_MET),
            getString(R.string.abs_groin_crunch_desc)
        )
        workoutListTableList.add(workoutListTable)

         workoutListTable = WorkoutListTable(
            getString(R.string.abs_boat_hold_leg_flutters),
            getString(R.string.abs_boat_hold_leg_flutters_json),
            getString(R.string.abs_boat_hold_leg_flutters_time),
            getString(R.string.abs_boat_hold_leg_flutters_MET),
            getString(R.string.abs_boat_hold_leg_flutters_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.abs_spinal_rotation_left),
            getString(R.string.abs_spinal_rotation_left_json),
            getString(R.string.abs_spinal_rotation_left_time),
            getString(R.string.abs_spinal_rotation_left_MET),
            getString(R.string.abs_spinal_rotation_left_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.abs_spinal_rotation_right),
            getString(R.string.abs_spinal_rotation_right_json),
            getString(R.string.abs_spinal_rotation_right_time),
            getString(R.string.abs_spinal_rotation_right_MET),
            getString(R.string.abs_spinal_rotation_right_desc)
        )
        workoutListTableList.add(workoutListTable)
    }
    /*
    * abs intermediate
    * */
    private fun addAbsIntermediate() {

         var workoutListTable = WorkoutListTable(
            getString(R.string.arm_jumping_jacks),
            getString(R.string.arm_jumping_jacks_json),
            getString(R.string.arm_jumping_jacks_time),
            getString(R.string.arm_jumping_jacks_MET),
            getString(R.string.arm_jumping_jacks_desc)
        )
        workoutListTableList.add(workoutListTable)
        workoutListTable = WorkoutListTable(
            getString(R.string.abs_cocoons),
            getString(R.string.abs_cocoons_json),
            getString(R.string.abs_cocoons_time),
            getString(R.string.abs_cocoons_MET),
            getString(R.string.abs_cocoons_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.abs_crunches),
            getString(R.string.abs_crunches_json),
            getString(R.string.abs_crunches_time),
            getString(R.string.abs_crunches_MET),
            getString(R.string.abs_crunches_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.abs_diagonal_mountain_climbers),
            getString(R.string.abs_diagonal_mountain_climbers_json),
            getString(R.string.abs_diagonal_mountain_climbers_time),
            getString(R.string.abs_diagonal_mountain_climbers_MET),
            getString(R.string.abs_diagonal_mountain_climbers_desc)
        )
        workoutListTableList.add(workoutListTable)



        workoutListTable = WorkoutListTable(
            getString(R.string.abs_flutter_kicks),
            getString(R.string.abs_flutter_kicks_json),
            getString(R.string.abs_flutter_kicks_time),
            getString(R.string.abs_flutter_kicks_MET),
            getString(R.string.abs_flutter_kicks_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.abs_groin_crunch),
            getString(R.string.abs_groin_crunch_json),
            getString(R.string.abs_groin_crunch_time),
            getString(R.string.abs_groin_crunch_MET),
            getString(R.string.abs_groin_crunch_desc)
        )
        workoutListTableList.add(workoutListTable)


        workoutListTable = WorkoutListTable(
            getString(R.string.abs_russian_twists),
            getString(R.string.abs_russian_twists_json),
            getString(R.string.abs_russian_twists_time),
            getString(R.string.abs_russian_twists_MET),
            getString(R.string.abs_russian_twists_desc)
        )
        workoutListTableList.add(workoutListTable)



        workoutListTable = WorkoutListTable(
            getString(R.string.abs_star_plank),
            getString(R.string.abs_star_plank_json),
            getString(R.string.abs_star_plank_time),
            getString(R.string.abs_star_plank_MET),
            getString(R.string.abs_star_plank_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.abs_spinal_rotation_left),
            getString(R.string.abs_spinal_rotation_left_json),
            getString(R.string.abs_spinal_rotation_left_time),
            getString(R.string.abs_spinal_rotation_left_MET),
            getString(R.string.abs_spinal_rotation_left_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.abs_spinal_rotation_right),
            getString(R.string.abs_spinal_rotation_right_json),
            getString(R.string.abs_spinal_rotation_right_time),
            getString(R.string.abs_spinal_rotation_right_MET),
            getString(R.string.abs_spinal_rotation_right_desc)
        )
        workoutListTableList.add(workoutListTable)

    }

    /*
  * chest BEGINNER
  * */
    private fun addChestBeginner() {
        var workoutListTable = WorkoutListTable(
            getString(R.string.arm_jumping_jacks),
            getString(R.string.arm_jumping_jacks_json),
            getString(R.string.arm_jumping_jacks_time),
            getString(R.string.arm_jumping_jacks_MET),
            getString(R.string.arm_jumping_jacks_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.yoga_cobra_pose),
            getString(R.string.yoga_cobra_pose_json),
            getString(R.string.yoga_cobra_time),
            getString(R.string.yoga_cobra_MET),
            getString(R.string.yoga_cobra_desc)
        )
        workoutListTableList.add(workoutListTable)


        workoutListTable = WorkoutListTable(
            getString(R.string.chest_plank_lunges),
            getString(R.string.chest_plank_lunges_json),
            getString(R.string.chest_plank_lunges_time),
            getString(R.string.chest_plank_lunges_MET),
            getString(R.string.chest_plank_lunges_desc)
        )
        workoutListTableList.add(workoutListTable)
        workoutListTable = WorkoutListTable(
            getString(R.string.chest_plank_low_to_high),
            getString(R.string.chest_plank_low_to_high_json),
            getString(R.string.chest_plank_low_to_high_time),
            getString(R.string.chest_plank_low_to_high_MET),
            getString(R.string.chest_plank_low_to_high_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.chest_cross_tricep_extension),
            getString(R.string.chest_cross_tricep_extension_json),
            getString(R.string.chest_cross_tricep_extension_time),
            getString(R.string.chest_cross_tricep_extension_MET),
            getString(R.string.chest_cross_tricep_extension_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.chest_bench_kneeling_lat_stretch_left),
            getString(R.string.chest_bench_kneeling_lat_stretch_left_json),
            getString(R.string.chest_bench_kneeling_lat_stretch_left_time),
            getString(R.string.chest_bench_kneeling_lat_stretch_left_MET),
            getString(R.string.chest_bench_kneeling_lat_stretch_left_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.chest_bench_kneeling_lat_stretch_right),
            getString(R.string.chest_bench_kneeling_lat_stretch_right_json),
            getString(R.string.chest_bench_kneeling_lat_stretch_right_time),
            getString(R.string.chest_bench_kneeling_lat_stretch_right_MET),
            getString(R.string.chest_bench_kneeling_lat_stretch_right_desc)
        )
        workoutListTableList.add(workoutListTable)
        workoutListTable = WorkoutListTable(
            getString(R.string.chest_stretch_left),
            getString(R.string.chest_stretch_left_json),
            getString(R.string.chest_stretch_left_time),
            getString(R.string.chest_stretch_left_MET),
            getString(R.string.chest_stretch_left_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.chest_stretch_right),
            getString(R.string.chest_stretch_right_json),
            getString(R.string.chest_stretch_right_time),
            getString(R.string.chest_stretch_right_MET),
            getString(R.string.chest_stretch_right_desc)
        )
        workoutListTableList.add(workoutListTable)


    }
    /*
  * chest ADVANCED
  * */
    private fun addChestAdvanced() {
        var workoutListTable = WorkoutListTable(
            getString(R.string.arm_jumping_jacks),
            getString(R.string.arm_jumping_jacks_json),
            getString(R.string.arm_jumping_jacks_time),
            getString(R.string.arm_jumping_jacks_MET),
            getString(R.string.arm_jumping_jacks_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.chest_plank_jumping_lunges),
            getString(R.string.chest_plank_jumping_lunges_json),
            getString(R.string.chest_plank_jumping_lunges_time),
            getString(R.string.chest_plank_jumping_lunges_MET),
            getString(R.string.chest_plank_jumping_lunges_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.chest_incline_close_push_up),
            getString(R.string.chest_incline_close_push_up_json),
            getString(R.string.chest_incline_close_push_up_time),
            getString(R.string.chest_incline_close_push_up_MET),
            getString(R.string.chest_incline_close_push_up_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.chest_tap_push_up),
            getString(R.string.chest_tap_push_up_json),
            getString(R.string.chest_tap_push_up_time),
            getString(R.string.chest_tap_push_up_MET),
            getString(R.string.chest_tap_push_up_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.chest_decline_close_push_up),
            getString(R.string.chest_decline_close_push_up_json),
            getString(R.string.chest_decline_close_push_up_time),
            getString(R.string.chest_decline_close_push_up_MET),
            getString(R.string.chest_decline_close_push_up_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.chest_archer_push_up),
            getString(R.string.chest_archer_push_up_json),
            getString(R.string.chest_archer_push_up_time),
            getString(R.string.chest_archer_push_up_MET),
            getString(R.string.chest_archer_push_up_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.chest_burpee_with_push_up),
            getString(R.string.chest_burpee_with_push_up_json),
            getString(R.string.chest_burpee_with_push_up_time),
            getString(R.string.chest_burpee_with_push_up_MET),
            getString(R.string.chest_burpee_with_push_up_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.chest_cross_tricep_extension),
            getString(R.string.chest_cross_tricep_extension_json),
            getString(R.string.chest_cross_tricep_extension_time),
            getString(R.string.chest_cross_tricep_extension_MET),
            getString(R.string.chest_cross_tricep_extension_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.chest_box_glute_stretch_left),
            getString(R.string.chest_box_glute_stretch_left_json),
            getString(R.string.chest_box_glute_stretch_left_time),
            getString(R.string.chest_box_glute_stretch_left_MET),
            getString(R.string.chest_box_glute_stretch_left_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.chest_box_glute_stretch_right),
            getString(R.string.chest_box_glute_stretch_right_json),
            getString(R.string.chest_box_glute_stretch_right_time),
            getString(R.string.chest_box_glute_stretch_right_MET),
            getString(R.string.chest_box_glute_stretch_right_desc)
        )
        workoutListTableList.add(workoutListTable)

    }
    /*
    * chest INTERMEDIATE
    * */
    private fun addChestIntermediate() {
        var workoutListTable = WorkoutListTable(
            getString(R.string.chest_archer_push_up),
            getString(R.string.chest_archer_push_up_json),
            getString(R.string.chest_archer_push_up_time),
            getString(R.string.chest_archer_push_up_MET),
            getString(R.string.chest_archer_push_up_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.arm_jumping_jacks),
            getString(R.string.arm_jumping_jacks_json),
            getString(R.string.arm_jumping_jacks_time),
            getString(R.string.arm_jumping_jacks_MET),
            getString(R.string.arm_jumping_jacks_desc)
        )
        workoutListTableList.add(workoutListTable)



        workoutListTable = WorkoutListTable(
            getString(R.string.chest_clap_push_up),
            getString(R.string.chest_clap_push_up_json),
            getString(R.string.chest_clap_push_up_time),
            getString(R.string.chest_clap_push_up_MET),
            getString(R.string.cchest_clap_push_up_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.chest_cross_knee_tricep_extension),
            getString(R.string.chest_cross_knee_tricep_extension_json),
            getString(R.string.chest_cross_knee_tricep_extension_time),
            getString(R.string.chest_cross_knee_tricep_extension_MET),
            getString(R.string.chest_cross_knee_tricep_extension_desc)
        )
        workoutListTableList.add(workoutListTable)




        workoutListTable = WorkoutListTable(
            getString(R.string.chest_decline_wide_push_up),
            getString(R.string.chest_decline_wide_push_up_json),
            getString(R.string.chest_decline_wide_push_up_time),
            getString(R.string.chest_decline_wide_push_up_MET),
            getString(R.string.chest_decline_wide_push_up_desc)
        )
        workoutListTableList.add(workoutListTable)




        workoutListTable = WorkoutListTable(
            getString(R.string.chest_incline_push_up),
            getString(R.string.chest_incline_push_up_json),
            getString(R.string.chest_incline_push_up_time),
            getString(R.string.chest_incline_push_up_MET),
            getString(R.string.chest_incline_push_up_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.chest_modified_burpee),
            getString(R.string.chest_modified_burpee_json),
            getString(R.string.chest_modified_burpee_time),
            getString(R.string.chest_modified_burpee_MET),
            getString(R.string.chest_modified_burpee_desc)
        )
        workoutListTableList.add(workoutListTable)



        workoutListTable = WorkoutListTable(
            getString(R.string.chest_plank_low_to_high),
            getString(R.string.chest_plank_low_to_high_json),
            getString(R.string.chest_plank_low_to_high_time),
            getString(R.string.chest_plank_low_to_high_MET),
            getString(R.string.chest_plank_low_to_high_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.chest_plank_lunges),
            getString(R.string.chest_plank_lunges_json),
            getString(R.string.chest_plank_lunges_time),
            getString(R.string.chest_plank_lunges_MET),
            getString(R.string.chest_plank_lunges_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.chest_stretch_left),
            getString(R.string.chest_stretch_left_json),
            getString(R.string.chest_stretch_left_time),
            getString(R.string.chest_stretch_left_MET),
            getString(R.string.chest_stretch_left_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.chest_stretch_right),
            getString(R.string.chest_stretch_right_json),
            getString(R.string.chest_stretch_right_time),
            getString(R.string.chest_stretch_right_MET),
            getString(R.string.chest_stretch_right_desc)
        )
        workoutListTableList.add(workoutListTable)
        workoutListTable = WorkoutListTable(
            getString(R.string.chest_bench_kneeling_lat_stretch_hold_left),
            getString(R.string.chest_bench_kneeling_lat_stretch_hold_left_json),
            getString(R.string.chest_bench_kneeling_lat_stretch_hold_left_time),
            getString(R.string.chest_bench_kneeling_lat_stretch_hold_left_MET),
            getString(R.string.chest_bench_kneeling_lat_stretch_hold_left_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.chest_bench_kneeling_lat_stretch_hold_right),
            getString(R.string.chest_bench_kneeling_lat_stretch_hold_right_json),
            getString(R.string.chest_bench_kneeling_lat_stretch_hold_right_time),
            getString(R.string.chest_bench_kneeling_lat_stretch_hold_right_MET),
            getString(R.string.chest_bench_kneeling_lat_stretch_hold_right_desc)
        )
        workoutListTableList.add(workoutListTable)


        workoutListTable = WorkoutListTable(
            getString(R.string.chest_box_glute_stretch_hold_left),
            getString(R.string.chest_box_glute_stretch_hold_left_json),
            getString(R.string.chest_box_glute_stretch_hold_left_time),
            getString(R.string.chest_box_glute_stretch_hold_left_MET),
            getString(R.string.chest_box_glute_stretch_hold_left_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.chest_box_glute_stretch_hold_right),
            getString(R.string.chest_box_glute_stretch_hold_right_json),
            getString(R.string.chest_box_glute_stretch_hold_right_time),
            getString(R.string.chest_box_glute_stretch_hold_right_MET),
            getString(R.string.chest_box_glute_stretch_hold_right_desc)
        )
        workoutListTableList.add(workoutListTable)

    }

    /*
  * leg beginner
  * */
    private fun addLegBeginner() {
        var workoutListTable = WorkoutListTable(
            getString(R.string.leg_side_lunges_with_twist),
            getString(R.string.leg_side_lunges_with_twist_json),
            getString(R.string.leg_side_lunges_with_twist_time),
            getString(R.string.leg_side_lunges_with_twist_MET),
            getString(R.string.leg_side_lunges_with_twist_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.leg_high_stepping),
            getString(R.string.leg_high_stepping_json),
            getString(R.string.leg_high_stepping_time),
            getString(R.string.leg_high_stepping_MET),
            getString(R.string.leg_high_stepping_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.leg_lunges),
            getString(R.string.leg_lunges_json),
            getString(R.string.leg_lunges_time),
            getString(R.string.leg_lunges_MET),
            getString(R.string.leg_lunges_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.leg_chair_stand_excercise),
            getString(R.string.leg_chair_stand_excercise_json),
            getString(R.string.leg_chair_stand_excercise_time),
            getString(R.string.leg_chair_stand_excercise_MET),
            getString(R.string.leg_chair_stand_excercise_desc)
        )
        workoutListTableList.add(workoutListTable)
        workoutListTable = WorkoutListTable(
            getString(R.string.leg_curtsy_lunge_left),
            getString(R.string.leg_curtsy_lunge_left_json),
            getString(R.string.leg_curtsy_lunge_left_time),
            getString(R.string.leg_curtsy_lunge_left_MET),
            getString(R.string.leg_curtsy_lunge_left_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.leg_curtsy_lunge_right),
            getString(R.string.leg_curtsy_lunge_right_json),
            getString(R.string.leg_curtsy_lunge_right_time),
            getString(R.string.leg_curtsy_lunge_rightt_MET),
            getString(R.string.leg_curtsy_lunge_right_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.leg_high_knee_taps),
            getString(R.string.leg_high_knee_taps_json),
            getString(R.string.leg_high_knee_taps_time),
            getString(R.string.leg_high_knee_taps_MET),
            getString(R.string.leg_high_knee_taps_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.leg_dynamic_hamstring_stretch_left),
            getString(R.string.leg_dynamic_hamstring_stretch_left_json),
            getString(R.string.leg_dynamic_hamstring_stretch_left_time),
            getString(R.string.leg_dynamic_hamstring_stretch_left_MET),
            getString(R.string.leg_dynamic_hamstring_stretch_left_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.leg_dynamic_hamstring_stretch_right),
            getString(R.string.leg_dynamic_hamstring_stretch_right_json),
            getString(R.string.leg_dynamic_hamstring_stretch_right_time),
            getString(R.string.leg_dynamic_hamstring_stretch_right_MET),
            getString(R.string.leg_dynamic_hamstring_stretch_right_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.leg_butt_kicks),
            getString(R.string.leg_butt_kicks_json),
            getString(R.string.leg_butt_kicks_time),
            getString(R.string.leg_butt_kicks_MET),
            getString(R.string.leg_butt_kicks_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.leg_dead_bug_leg_lowering),
            getString(R.string.leg_dead_bug_leg_lowering_json),
            getString(R.string.leg_dead_bug_leg_lowering_time),
            getString(R.string.leg_dead_bug_leg_lowering_MET),
            getString(R.string.leg_dead_bug_leg_lowering_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.leg_hip_flexor_stretch_left),
            getString(R.string.leg_hip_flexor_stretch_left_json),
            getString(R.string.leg_hip_flexor_stretch_left_time),
            getString(R.string.leg_hip_flexor_stretch_left_MET),
            getString(R.string.leg_hip_flexor_stretch_left_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.leg_hip_flexor_stretch_right),
            getString(R.string.leg_hip_flexor_stretch_right_json),
            getString(R.string.leg_hip_flexor_stretch_right_time),
            getString(R.string.leg_hip_flexor_stretch_right_MET),
            getString(R.string.leg_hip_flexor_stretch_right_desc)
        )
        workoutListTableList.add(workoutListTable)

    }

    /*
   * leg advanced
   * */
    private fun addLegAdvanced() {
        var workoutListTable = WorkoutListTable(
            getString(R.string.leg_side_plank),
            getString(R.string.leg_side_plank_json),
            getString(R.string.leg_side_plank_time),
            getString(R.string.leg_side_plank_MET),
            getString(R.string.leg_side_plank_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.leg_lunges),
            getString(R.string.leg_lunges_json),
            getString(R.string.leg_lunges_time),
            getString(R.string.leg_lunges_MET),
            getString(R.string.leg_lunges_desc)
        )
        workoutListTableList.add(workoutListTable)
        workoutListTable = WorkoutListTable(
            getString(R.string.leg_high_knee_taps),
            getString(R.string.leg_high_knee_taps_json),
            getString(R.string.leg_high_knee_taps_time),
            getString(R.string.leg_high_knee_taps_MET),
            getString(R.string.leg_high_knee_taps_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.leg_curtsy_lunge_alternating),
            getString(R.string.leg_curtsy_lunge_alternating_json),
            getString(R.string.leg_curtsy_lunge_alternating_time),
            getString(R.string.leg_curtsy_lunge_alternating_MET),
            getString(R.string.leg_curtsy_lunge_alternatinge_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.leg_bulgarian_split_squat_jump_left),
            getString(R.string.leg_bulgarian_split_squat_jump_left_json),
            getString(R.string.leg_bulgarian_split_squat_jump_left_time),
            getString(R.string.leg_bulgarian_split_squat_jump_left_MET),
            getString(R.string.leg_bulgarian_split_squat_jump_left_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.leg_bulgarian_split_squat_jump_right),
            getString(R.string.leg_bulgarian_split_squat_jump_right_json),
            getString(R.string.leg_bulgarian_split_squat_jump_right_time),
            getString(R.string.leg_bulgarian_split_squat_jump_right_MET),
            getString(R.string.leg_bulgarian_split_squat_jump_right_desc)
        )
        workoutListTableList.add(workoutListTable)
        workoutListTable = WorkoutListTable(
            getString(R.string.leg_butt_kicks),
            getString(R.string.leg_butt_kicks_json),
            getString(R.string.leg_butt_kicks_time),
            getString(R.string.leg_butt_kicks_MET),
            getString(R.string.leg_butt_kicks_desc)
        )
        workoutListTableList.add(workoutListTable)
        workoutListTable = WorkoutListTable(
            getString(R.string.leg_dynamic_hip_flexor_stretch_left),
            getString(R.string.leg_dynamic_hip_flexor_stretch_left_json),
            getString(R.string.leg_dynamic_hip_flexor_stretch_left_time),
            getString(R.string.leg_dynamic_hip_flexor_stretch_left_MET),
            getString(R.string.leg_dynamic_hip_flexor_stretch_left_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.leg_dynamic_hip_flexor_stretch_right),
            getString(R.string.leg_dynamic_hip_flexor_stretch_right_json),
            getString(R.string.leg_dynamic_hip_flexor_stretch_right_time),
            getString(R.string.leg_dynamic_hip_flexor_stretch_right_MET),
            getString(R.string.leg_dynamic_hip_flexor_stretch_right_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.leg_assisted_pistol_left),
            getString(R.string.leg_assisted_pistol_left_json),
            getString(R.string.leg_assisted_pistol_left_time),
            getString(R.string.leg_assisted_pistol_left_MET),
            getString(R.string.leg_assisted_pistol_left_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.leg_assisted_pistol_right),
            getString(R.string.leg_assisted_pistol_right_json),
            getString(R.string.leg_assisted_pistol_right_time),
            getString(R.string.leg_assisted_pistol_right_MET),
            getString(R.string.leg_assisted_pistol_right_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.leg_kneeling_achilles_stretch_left),
            getString(R.string.leg_kneeling_achilles_stretch_left_json),
            getString(R.string.leg_kneeling_achilles_stretch_left_time),
            getString(R.string.leg_kneeling_achilles_stretch_left_MET),
            getString(R.string.leg_kneeling_achilles_stretch_left_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.leg_kneeling_achilles_stretch_right),
            getString(R.string.leg_kneeling_achilles_stretch_right_json),
            getString(R.string.leg_kneeling_achilles_stretch_right_time),
            getString(R.string.leg_kneeling_achilles_stretch_right_MET),
            getString(R.string.leg_kneeling_achilles_stretch_right_desc)
        )
        workoutListTableList.add(workoutListTable)

    }

    /*
    * leg intermediate
    * */
    private fun addLegIntermediate() {


        var workoutListTable = WorkoutListTable(
            getString(R.string.leg_bench_hamstring_stretch_left),
            getString(R.string.leg_bench_hamstring_stretch_left_json),
            getString(R.string.leg_bench_hamstring_stretch_left_time),
            getString(R.string.leg_bench_hamstring_stretch_left_MET),
            getString(R.string.leg_bench_hamstring_stretch_left_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.leg_bench_hamstring_stretch_right),
            getString(R.string.leg_bench_hamstring_stretch_right_json),
            getString(R.string.leg_bench_hamstring_stretch_right_time),
            getString(R.string.leg_bench_hamstring_stretch_right_MET),
            getString(R.string.leg_bench_hamstring_stretch_right_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.leg_butt_kicks),
            getString(R.string.leg_butt_kicks_json),
            getString(R.string.leg_butt_kicks_time),
            getString(R.string.leg_butt_kicks_MET),
            getString(R.string.leg_butt_kicks_desc)
        )
        workoutListTableList.add(workoutListTable)


        workoutListTable = WorkoutListTable(
            getString(R.string.leg_curtsy_lunge_alternating),
            getString(R.string.leg_curtsy_lunge_alternating_json),
            getString(R.string.leg_curtsy_lunge_alternating_time),
            getString(R.string.leg_curtsy_lunge_alternating_MET),
            getString(R.string.leg_curtsy_lunge_alternatinge_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.leg_dead_bug),
            getString(R.string.leg_dead_bug_json),
            getString(R.string.leg_dead_bug_time),
            getString(R.string.leg_dead_bug_MET),
            getString(R.string.leg_dead_bug_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.leg_hamstring_stretch_left),
            getString(R.string.lleg_hamstring_stretch_left_json),
            getString(R.string.leg_hamstring_stretch_left_time),
            getString(R.string.leg_hamstring_stretch_left_MET),
            getString(R.string.leg_hamstring_stretch_left_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.leg_hamstring_stretch_right),
            getString(R.string.leg_hamstring_stretch_right_json),
            getString(R.string.leg_hamstring_stretch_right_time),
            getString(R.string.leg_hamstring_stretch_right_MET),
            getString(R.string.leg_hamstring_stretch_right_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.leg_high_knee_taps),
            getString(R.string.leg_high_knee_taps_json),
            getString(R.string.leg_high_knee_taps_time),
            getString(R.string.leg_high_knee_taps_MET),
            getString(R.string.leg_high_knee_taps_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.leg_lunges),
            getString(R.string.leg_lunges_json),
            getString(R.string.leg_lunges_time),
            getString(R.string.leg_lunges_MET),
            getString(R.string.leg_lunges_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.leg_torso_twist_stretch_left),
            getString(R.string.leg_torso_twist_stretch_left_json),
            getString(R.string.leg_torso_twist_stretch_left_time),
            getString(R.string.leg_torso_twist_stretch_left_MET),
            getString(R.string.leg_torso_twist_stretch_left_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.leg_torso_twist_stretch_right),
            getString(R.string.leg_torso_twist_stretch_right_json),
            getString(R.string.leg_torso_twist_stretch_right_time),
            getString(R.string.leg_torso_twist_stretch_right_MET),
            getString(R.string.leg_torso_twist_stretch_right_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.leg_wall_sitting),
            getString(R.string.leg_wall_sitting_json),
            getString(R.string.leg_wall_sitting_time),
            getString(R.string.leg_wall_sitting_MET),
            getString(R.string.leg_wall_sitting_desc)
        )
        workoutListTableList.add(workoutListTable)
    }

    /*
    * Shoulder & Back Advanced
    * */
    private fun addShoulderBackAdvanced(){
        var workoutListTable = WorkoutListTable(
            getString(R.string.arm_jumping_jacks),
            getString(R.string.arm_jumping_jacks_json),
            getString(R.string.arm_jumping_jacks_time),
            getString(R.string.arm_jumping_jacks_MET),
            getString(R.string.arm_jumping_jacks_desc)
        )
        workoutListTableList.add(workoutListTable)


        workoutListTable = WorkoutListTable(
            getString(R.string.sb_floor_dips),
            getString(R.string.sb_floor_dips_json),
            getString(R.string.sb_floor_dips_time),
            getString(R.string.sb_floor_dips_MET),
            getString(R.string.sb_floor_dips_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.sb_crab_toe_touches),
            getString(R.string.sb_crab_toe_touches_json),
            getString(R.string.sb_crab_toe_touches_time),
            getString(R.string.sb_crab_toe_touches_MET),
            getString(R.string.sb_crab_toe_touches_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.sb_high_plank_jacks),
            getString(R.string.sb_high_plank_jacks_json),
            getString(R.string.sb_high_plank_jacks_time),
            getString(R.string.sb_high_plank_jacks_MET),
            getString(R.string.sb_high_plank_jacks_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.sb_iron_man_pose),
            getString(R.string.sb_iron_man_pose_json),
            getString(R.string.sb_iron_man_pose_time),
            getString(R.string.sb_iron_man_pose_MET),
            getString(R.string.sb_iron_man_pose_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.sb_prone_chest_stretch_left),
            getString(R.string.sb_prone_chest_stretch_left_json),
            getString(R.string.sb_prone_chest_stretch_left_time),
            getString(R.string.sb_prone_chest_stretch_left_MET),
            getString(R.string.sb_prone_chest_stretch_left_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.sb_prone_chest_stretch_right),
            getString(R.string.sb_prone_chest_stretch_right_json),
            getString(R.string.sb_prone_chest_stretch_right_time),
            getString(R.string.sb_prone_chest_stretch_right_MET),
            getString(R.string.sb_prone_chest_stretch_right_desc)
        )
        workoutListTableList.add(workoutListTable)

    }

    /*
    * Shoulder & Back Intermediate
    * */
    private fun addShoulderBackIntermediate() {
        var workoutListTable = WorkoutListTable(
            getString(R.string.sb_camel_pose),
            getString(R.string.sb_camel_pose_json),
            getString(R.string.sb_camel_pose_time),
            getString(R.string.sb_camel_pose_MET),
            getString(R.string.sb_camel_pose_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.arm_jumping_jacks),
            getString(R.string.arm_jumping_jacks_json),
            getString(R.string.arm_jumping_jacks_time),
            getString(R.string.arm_jumping_jacks_MET),
            getString(R.string.arm_jumping_jacks_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.yoga_child_pose),
            getString(R.string.yoga_child_pose_json),
            getString(R.string.yoga_child_time),
            getString(R.string.yoga_child_MET),
            getString(R.string.yoga_child_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.sb_crab_toe_touches),
            getString(R.string.sb_crab_toe_touches_json),
            getString(R.string.sb_crab_toe_touches_time),
            getString(R.string.sb_crab_toe_touches_MET),
            getString(R.string.sb_crab_toe_touches_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.sb_elevated_pike_push_up),
            getString(R.string.sb_elevated_pike_push_up_json),
            getString(R.string.sb_elevated_pike_push_up_time),
            getString(R.string.sb_elevated_pike_push_up_MET),
            getString(R.string.sb_elevated_pike_push_up_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.sb_iron_man),
            getString(R.string.sb_iron_man_json),
            getString(R.string.sb_iron_man_time),
            getString(R.string.sb_iron_man_MET),
            getString(R.string.sb_iron_man_desc)
        )
        workoutListTableList.add(workoutListTable)


        workoutListTable = WorkoutListTable(
            getString(R.string.sb_kneeling_back_rotation_left),
            getString(R.string.sb_kneeling_back_rotation_left_json),
            getString(R.string.sb_kneeling_back_rotation_left_time),
            getString(R.string.sb_kneeling_back_rotation_left_MET),
            getString(R.string.sb_kneeling_back_rotation_left_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.sb_kneeling_back_rotation_right),
            getString(R.string.sb_kneeling_back_rotation_right_json),
            getString(R.string.sb_kneeling_back_rotation_right_time),
            getString(R.string.sb_kneeling_back_rotation_right_MET),
            getString(R.string.sb_kneeling_back_rotation_right_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.sb_pike_push_up),
            getString(R.string.sb_pike_push_up_json),
            getString(R.string.sb_pike_push_up_time),
            getString(R.string.sb_pike_push_up_MET),
            getString(R.string.sb_pike_push_up_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.sb_prone_chest_stretch_left),
            getString(R.string.sb_prone_chest_stretch_left_json),
            getString(R.string.sb_prone_chest_stretch_left_time),
            getString(R.string.sb_prone_chest_stretch_left_MET),
            getString(R.string.sb_prone_chest_stretch_left_desc)
        )
        workoutListTableList.add(workoutListTable)

        workoutListTable = WorkoutListTable(
            getString(R.string.sb_prone_chest_stretch_right),
            getString(R.string.sb_prone_chest_stretch_right_json),
            getString(R.string.sb_prone_chest_stretch_right_time),
            getString(R.string.sb_prone_chest_stretch_right_MET),
            getString(R.string.sb_prone_chest_stretch_right_desc)
        )
        workoutListTableList.add(workoutListTable)
    }

    fun onStartClick() {
        val intent = Intent(this, ReadyToGoActivity::class.java)
        intent.putExtra("exerciseList", workoutListTableList);
        intent.putExtra("workoutName", workoutName)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }

    @SuppressWarnings("kotlin:S3776")
    //dialogWorkout() method have Cognitive Complexity
    public fun dialogWorkout(
        workoutListTableList: ArrayList<WorkoutListTable>,
        position: Int
    ) {
        val dialogWorkout = Dialog(this@WorkoutListActivity, android.R.style.Theme_Dialog)
        dialogWorkout.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogWorkout.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogWorkout.setContentView(R.layout.dialog_workout_list)

        var ltvDialogWorkoutImage =
            dialogWorkout.findViewById<LottieAnimationView>(R.id.ltvDialogWorkoutImage)
        var tvDialogWorkoutName = dialogWorkout.findViewById<TextView>(R.id.tvDialogWorkoutName)
        var tvDialogWorkoutDesc = dialogWorkout.findViewById<TextView>(R.id.tvDialogWorkoutDesc)
        var tvClose = dialogWorkout.findViewById<TextView>(R.id.tvClose)
        var tvDialogWorkoutPosition =
            dialogWorkout.findViewById<TextView>(R.id.tvDialogWorkoutPosition)
        var tvDialogTotalWorkout = dialogWorkout.findViewById<TextView>(R.id.tvDialogTotalWorkout)
        var ivNext = dialogWorkout.findViewById<ImageView>(R.id.ivNext)
        var ivBack = dialogWorkout.findViewById<ImageView>(R.id.ivBack)


        positionForWorkout = position
        tvDialogWorkoutName.text = workoutListTableList[positionForWorkout].execriseName
        tvDialogWorkoutDesc.text = workoutListTableList[positionForWorkout].exerciseDesc
        tvDialogTotalWorkout.text = workoutListTableList.size.toString()
        tvDialogWorkoutPosition.text = (position + 1).toString()
        ltvDialogWorkoutImage.setAnimation(workoutListTableList[positionForWorkout].workoutTypeImage)


        tvClose.setOnClickListener(View.OnClickListener { view ->
            dialogWorkout.dismiss()
        })

        if (positionForWorkout == workoutListTableList.size-1) {
            ivNext.visibility = View.INVISIBLE
        } else {
            ivNext.visibility = View.VISIBLE
        }
        if (positionForWorkout == 0) {
            ivBack.visibility = View.INVISIBLE
        } else {
            ivBack.visibility = View.VISIBLE
        }
        ivNext.setOnClickListener(View.OnClickListener { view ->
            if (positionForWorkout < workoutListTableList.size - 1) {
                if(positionForWorkout == workoutListTableList.size - 2){
                    ivNext.visibility = View.INVISIBLE
                    ivBack.visibility = View.VISIBLE
                }else {
                    ivNext.visibility = View.VISIBLE
                    ivBack.visibility = View.VISIBLE
                }
                positionForWorkout = positionForWorkout + 1
                tvDialogWorkoutName.text = workoutListTableList[positionForWorkout].execriseName
                tvDialogWorkoutDesc.text = workoutListTableList[positionForWorkout].exerciseDesc
                tvDialogTotalWorkout.text = workoutListTableList.size.toString()
                tvDialogWorkoutPosition.text = (positionForWorkout + 1).toString()

                ltvDialogWorkoutImage.setAnimation(workoutListTableList[positionForWorkout].workoutTypeImage)
                ltvDialogWorkoutImage.playAnimation()
            } else {
                ivNext.visibility = View.INVISIBLE
                ivBack.visibility = View.VISIBLE
            }
        })

        ivBack.setOnClickListener(View.OnClickListener { view ->
            if (positionForWorkout > 0) {
                if (positionForWorkout == 1){
                    ivBack.visibility = View.INVISIBLE
                    ivNext.visibility = View.VISIBLE
                }else {
                    ivBack.visibility = View.VISIBLE
                    ivNext.visibility = View.VISIBLE
                }
                positionForWorkout = positionForWorkout - 1
                tvDialogWorkoutName.text = workoutListTableList[positionForWorkout].execriseName
                tvDialogWorkoutDesc.text = workoutListTableList[positionForWorkout].exerciseDesc
                tvDialogTotalWorkout.text = workoutListTableList.size.toString()
                tvDialogWorkoutPosition.text = (positionForWorkout + 1).toString()

                ltvDialogWorkoutImage.setAnimation(workoutListTableList[positionForWorkout].workoutTypeImage)
                ltvDialogWorkoutImage.playAnimation()
            } else {
                ivBack.visibility = View.INVISIBLE
                ivNext.visibility = View.VISIBLE
            }
        })


        dialogWorkout.show()
        dialogWorkout.setCancelable(true)

    }

}