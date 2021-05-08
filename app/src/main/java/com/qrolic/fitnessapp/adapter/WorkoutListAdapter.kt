package com.qrolic.fitnessapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.qrolic.fitnessapp.R
import com.qrolic.fitnessapp.model.WorkoutListTable

internal class WorkoutListAdapter(private var workoutListTable: List<WorkoutListTable>,
                                  val clickListener: OnTypeClickListner
) : RecyclerView.Adapter<WorkoutListAdapter.MyViewHolder>() {


    open interface OnTypeClickListner {
        fun onItemClick(position: Int)
    }
    internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvExerciseTime: TextView = view.findViewById(R.id.tvExerciseTime)
        var tvExerciseName: TextView = view.findViewById(R.id.tvExerciseName)
        var llExcersice: LinearLayout = view.findViewById(R.id.llExcersice)
        var ltvExcersise: LottieAnimationView = view.findViewById(R.id.ltvExcersise)
    }

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.inflater_workout_list, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val workoutListTable = workoutListTable[position]
        holder.tvExerciseName.text = workoutListTable.execriseName
        holder.tvExerciseTime.text = workoutListTable.execrsieTime
        holder.ltvExcersise.setAnimation(workoutListTable.workoutTypeImage)
        holder.llExcersice.setOnClickListener(View.OnClickListener { view ->
            clickListener?.onItemClick(position)
        })



    }

    override fun getItemCount(): Int {
        return workoutListTable.size
    }
}