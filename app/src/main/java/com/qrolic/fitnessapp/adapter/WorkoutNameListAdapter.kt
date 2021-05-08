package com.qrolic.fitnessapp.adapter

import android.content.Context
import android.content.Intent

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.qrolic.fitnessapp.R
import com.qrolic.fitnessapp.activity.WorkoutListActivity
import com.qrolic.fitnessapp.model.WorkoutNameTable


internal class WorkoutNameListAdapter(
    private var workoutNameTableList: List<WorkoutNameTable>,
    private val context: Context?
) : RecyclerView.Adapter<WorkoutNameListAdapter.MyViewHolder>() {

    internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvWorkoutName: TextView = view.findViewById(R.id.tvWorkoutName)
        var rvWorkoutNameTypes: RecyclerView = view.findViewById(R.id.rvWorkoutNameTypes)
    }

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.inflater_workout_name, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val workoutNameTable = workoutNameTableList[position]
        holder.tvWorkoutName.text = workoutNameTable.workoutName

        var workoutTypesAdapter =
            WorkoutTypesAdapter(workoutNameTableList.get(position).workoutTypeList,
                object : WorkoutTypesAdapter.OnTypeClickListner {
                    override fun onItemClick(position1: Int) {
                        val intent = Intent(context, WorkoutListActivity::class.java)
                        intent.putExtra(
                            "workoutName",
                            workoutNameTableList.get(position).workoutName
                        )
                        intent.putExtra(
                            "workoutTypeName",
                            workoutNameTableList.get(position).workoutTypeList.get(position1).workoutTypeName
                        )

                        intent.putExtra(
                            "workoutTypeImage",
                            workoutNameTableList.get(position).workoutTypeList.get(position1).workoutTypeImage
                        )
                        intent.putExtra(
                            "workoutTypeTime",
                            workoutNameTableList.get(position).workoutTypeList.get(position1).workoutTypeTime
                        )
                        intent.putExtra(
                            "workoutType",
                            workoutNameTableList.get(position).workoutTypeList.get(position1).workoutType
                        )

                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        context?.startActivity(intent)
                    }
                })
        val layoutManager = LinearLayoutManager(context)
        holder.rvWorkoutNameTypes.layoutManager = layoutManager
        holder.rvWorkoutNameTypes.itemAnimator = DefaultItemAnimator()
        holder.rvWorkoutNameTypes.adapter = workoutTypesAdapter

    }

    override fun getItemCount(): Int {
        return workoutNameTableList.size
    }
}