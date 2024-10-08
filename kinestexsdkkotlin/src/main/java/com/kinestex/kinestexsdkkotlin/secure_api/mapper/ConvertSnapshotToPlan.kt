package com.kinestex.kinestexsdkkotlin.secure_api.mapper

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.GenericTypeIndicator
import com.kinestex.kinestexsdkkotlin.secure_api.models.Category
import com.kinestex.kinestexsdkkotlin.secure_api.models.Day
import com.kinestex.kinestexsdkkotlin.secure_api.models.Level
import com.kinestex.kinestexsdkkotlin.secure_api.models.Plan
import com.kinestex.kinestexsdkkotlin.secure_api.models.PlanInfo


/*
 * Created by shagi on 26.03.2024 01:36
 */

class ConvertSnapshotToPlan {
    fun toPlan(snapshot: DataSnapshot): Plan {
        val imgURL = snapshot.child("img_URL").getValue(String::class.java)
        val enSnapshot = snapshot.child("en")
        val ruSnapshot = snapshot.child("ru")

        val enPlanInfo = toPlanInfo(enSnapshot)
        val ruPlanInfo = toPlanInfo(ruSnapshot)

        return Plan(imgURL, enPlanInfo, ruPlanInfo)
    }

    private fun toPlanInfo(snapshot: DataSnapshot): PlanInfo {
        val bodyParts = snapshot.child("body_parts").getValue(String::class.java)
        val categorySnapshot = snapshot.child("category")
        val levelsSnapshot = snapshot.child("levels")
        val titlePlan = snapshot.child("title").getValue(String::class.java)

        val category = Category(
            description = categorySnapshot.child("description").getValue(String::class.java),
            levels = categorySnapshot.child("levels")
                .getValue(object : GenericTypeIndicator<Map<String, Long>>() {})
        )

        val levels = mutableListOf<Level>()
        levelsSnapshot.children.forEach { levelSnapshot ->

            val title = levelSnapshot.child("title").getValue(String::class.java)
            val description = levelSnapshot.child("description").getValue(String::class.java)

            val days = mutableListOf<Day>()
            levelSnapshot.child("days").children.forEach { daySnapshot ->

                val duration = daySnapshot.child("duration").getValue(Int::class.java)

                val workouts: MutableList<String> = mutableListOf()
                val workoutsSnapshot = daySnapshot.child("workouts")

                workoutsSnapshot.children.forEach { workoutSnapshot ->
                    val workout = workoutSnapshot.getValue(String::class.java)
                    workout?.let {
                        workouts.add(it)
                    }
                }

                val dayDescription = daySnapshot.child("description").getValue(String::class.java)
                val dayTitle = daySnapshot.child("title").getValue(String::class.java)

                days.add(Day(duration, workouts, dayDescription, dayTitle))
            }

            levels.add(Level(title, description, days))
        }

        return PlanInfo(bodyParts, category, levels, titlePlan)
    }
}