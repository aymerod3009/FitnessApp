package com.qrolic.fitnessapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.qrolic.fitnessapp.R
import com.qrolic.fitnessapp.model.ExerciseCompleteTable

internal class ExerciseCompleteListAdapter(private var exerciseCompleteTable: List<ExerciseCompleteTable>,
                                           val context: Context?
) : RecyclerView.Adapter<ExerciseCompleteListAdapter.MyViewHolder>() {



    internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvExerciseDateTime: TextView = view.findViewById(R.id.tvExerciseDateTime)
        var tvExerciseTypeName: TextView = view.findViewById(R.id.tvExerciseTypeName)
        var tvExerciseTotalTime: AppCompatTextView = view.findViewById(R.id.tvExerciseTotalTime)
        var tvExerciseBurnedCalories: AppCompatTextView = view.findViewById(R.id.tvExerciseBurnedCalories)

    }

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.inflater_exercise_complete, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val exerciseCompleteTable = exerciseCompleteTable[position]

        holder.tvExerciseDateTime.text = exerciseCompleteTable.dateTime
        holder.tvExerciseTypeName.text = exerciseCompleteTable.workoutTypeName
        holder.tvExerciseBurnedCalories.text = exerciseCompleteTable.burnedCalories.plus(" calories")
        holder.tvExerciseTotalTime.text = exerciseCompleteTable.totalTime

    }

    override fun getItemCount(): Int {
        return exerciseCompleteTable.size
    }
}