package com.qrolic.fitnessapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.qrolic.fitnessapp.R
import com.qrolic.fitnessapp.model.WorkoutTypeTable

internal class WorkoutTypesAdapter(private var workoutTypeTableList: List<WorkoutTypeTable>,
                                   val clickListener: OnTypeClickListner
) : RecyclerView.Adapter<WorkoutTypesAdapter.MyViewHolder>() {


    open interface OnTypeClickListner {
        fun onItemClick(position: Int)
    }
    internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var ivWorkoutTypeImage: ImageView = view.findViewById(R.id.ivWorkoutTypeImage)
        var tvWoroutTypeName: TextView = view.findViewById(R.id.tvWoroutTypeName)
        var tvWorkoutTypeTime: TextView = view.findViewById(R.id.tvWorkoutTypeTime)
        var flWorkoutType: FrameLayout = view.findViewById(R.id.flWorkoutType)
    }

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.inflater_workout_type, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val workoutNameTypeTable = workoutTypeTableList[position]
        holder.tvWorkoutTypeTime.text = workoutNameTypeTable.workoutTypeTime
        holder.tvWoroutTypeName.text = workoutNameTypeTable.workoutTypeName
        Glide.with(holder.itemView.context)
            .load(workoutNameTypeTable.workoutTypeImage)
            .centerCrop()
            .into(holder.ivWorkoutTypeImage);

        holder.flWorkoutType.setOnClickListener(View.OnClickListener { view ->
            clickListener?.onItemClick(position)
        })


    }

    override fun getItemCount(): Int {
        return workoutTypeTableList.size
    }
}