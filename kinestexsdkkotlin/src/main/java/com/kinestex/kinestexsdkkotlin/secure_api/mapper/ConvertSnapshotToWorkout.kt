package com.kinestex.kinestexsdkkotlin.secure_api.mapper

import com.google.firebase.database.DataSnapshot
import com.kinestex.kinestexsdkkotlin.secure_api.models.Equipment
import com.kinestex.kinestexsdkkotlin.secure_api.models.Exercise
import com.kinestex.kinestexsdkkotlin.secure_api.models.WorkoutInfo
import com.kinestex.kinestexsdkkotlin.secure_api.models.Workout


/*
 * Created by shagi on 24.03.2024 18:20
 */

class ConvertSnapshotToWorkout {
    fun toWorkout(snapshot: DataSnapshot): Workout {
        // Extracting sequence
        val title = snapshot.key
        val sequenceMap = mutableMapOf<String, Exercise>()
        val sequenceSnapshot = snapshot.child("sequence")
        sequenceSnapshot.children.forEach { exerciseSnapshot ->
            val exerciseKey = exerciseSnapshot.key ?: ""
            val exerciseValue = convertToExercise(exerciseSnapshot)
            sequenceMap[exerciseKey] = exerciseValue
        }

        val ruSnapshot = convertToWorkoutInfo(snapshot.child("ru"))
        val enSnapshot = convertToWorkoutInfo(snapshot.child("en"))

        val equipment = convertToEquipment(snapshot.child("equipment"))

        val totalMinutes = snapshot.child("total_minutes").getValue(Int::class.java) ?: 0
        val bodyImg = snapshot.child("body_img").getValue(String::class.java) ?: ""
        val workoutType = snapshot.child("workout_type").getValue(String::class.java) ?: ""
        val calories = snapshot.child("calories").getValue(Double::class.java) ?: 0.0
        val category = snapshot.child("category").getValue(String::class.java) ?: ""
        val workoutDescImg = snapshot.child("workout_desc_img").getValue(String::class.java) ?: ""
        val isTesting = snapshot.child("is_testing").getValue(Boolean::class.java) ?: false

        return Workout(
            title = title,
            sequence = sequenceMap,
            ru = ruSnapshot,
            total_minutes = totalMinutes,
            body_img = bodyImg,
            workout_type = workoutType,
            en = enSnapshot,
            calories = calories,
            category = category,
            workout_desc_img = workoutDescImg,
            is_testing = isTesting
        )
    }

    private fun convertToWorkoutInfo(snapshot: DataSnapshot): WorkoutInfo {
        val bodyParts = snapshot.child("body_parts").getValue(String::class.java) ?: ""
        val difLevel = snapshot.child("dif_level").getValue(String::class.java) ?: ""
        val description = snapshot.child("description").getValue(String::class.java) ?: ""
        val equipment = convertToEquipment(snapshot.child("equipment"))
        return WorkoutInfo(bodyParts, difLevel, description, equipment)
    }

    private fun convertToEquipment(snapshot: DataSnapshot): Equipment {
        val description = snapshot.child("description").getValue(String::class.java) ?: ""
        val equipment = snapshot.child("equipment").getValue(String::class.java) ?: ""
        return Equipment(description, equipment)
    }

    private fun convertToExercise(snapshot: DataSnapshot): Exercise {
        val repeats = snapshot.child("repeats").getValue(Int::class.java) ?: 0
        val countdownValue = snapshot.child("countdown").value

        val countdown = if (countdownValue is Long) countdownValue else null

        val title = snapshot.child("title").getValue(String::class.java) ?: ""

        return Exercise(repeats, countdown, title = title)
    }

}

