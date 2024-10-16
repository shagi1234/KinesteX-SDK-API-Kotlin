package com.kinestex.kinestexsdkkotlin.secure_api.mapper

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.gson.GsonBuilder
import com.kinestex.kinestexsdkkotlin.secure_api.models.DayInfo
import com.kinestex.kinestexsdkkotlin.secure_api.models.Exercise
import com.kinestex.kinestexsdkkotlin.secure_api.models.Plan
import com.kinestex.kinestexsdkkotlin.secure_api.models.TranslationsExercise
import com.kinestex.kinestexsdkkotlin.secure_api.models.TranslationsPlan
import com.kinestex.kinestexsdkkotlin.secure_api.models.TranslationsWorkout
import com.kinestex.kinestexsdkkotlin.secure_api.models.WeekInfo
import com.kinestex.kinestexsdkkotlin.secure_api.models.Workout
import com.kinestex.kinestexsdkkotlin.secure_api.models.WorkoutExercise

fun DocumentSnapshot.toWorkout(): Workout? {
    return try {
        val data = this.data ?: mapOf()
        val gson = GsonBuilder().setPrettyPrinting().create()
        val prettyJson = gson.toJson(data)
        Log.d("DocumentSnapshot.toWorkout", "JSON data: $prettyJson")

        val enData = get("en") as? Map<String, Any> ?: mapOf()

        Workout(
            id = id,
            imgURL = getString("body_img") ?: "",
            category = getString("category") ?: "",
            total_minutes = getLong("total_minutes")?.toInt() ?: 0,
            total_calories = getLong("calories")?.toInt() ?: 0,
            sequence = (get("sequence") as? Map<String, Any>)?.map { (_, value) ->
                val exercise = value as? Map<String, Any> ?: mapOf()
                WorkoutExercise(
                    title = exercise["title"] as? String ?: "",
                    id = exercise["id"] as? String ?: "",
                    countdown = (exercise["countdown"] as? Long)?.toInt(),
                    repeats = (exercise["repeats"] as? Long)?.toInt()
                )
            }?.toList() ?: listOf(),
            en = TranslationsWorkout(
                title = enData["title"] as? String ?: "",
                body_parts = enData["body_parts_array"] as? List<String>
                    ?: (enData["body_parts"] as? String)?.split(", ")
                    ?: listOf(),
                description = enData["description"] as? String ?: "",
                dif_level = enData["dif_level"] as? String ?: ""
            )
        )
    } catch (e: Exception) {
        Log.e("WorkoutsRepository", "Error converting document to Workout", e)
        null
    }
}

fun DocumentSnapshot.toExercise(): Exercise {
    val data = this.data ?: mapOf()
    val gson = GsonBuilder().setPrettyPrinting().create()
    val prettyJson = gson.toJson(data)
    Log.d("DocumentSnapshot.toExercise", "JSON data: $prettyJson")

    val enFields = data["en"] as? Map<String, Any> ?: mapOf()

    return Exercise(
        id = id,
        thumbnailURL = data["thumbnail_URL"] as? String ?: "",
        videoURL = data["video_URL"] as? String ?: "",
        avg_reps = (data["repeats"] as? Number)?.toInt(),
        avg_countdown = (data["correct_second"] as? Number)?.toInt(),
        rest_duration = (enFields["rest_duration"] as? Number)?.toInt() ?: 0,
        en = TranslationsExercise(
            title = enFields["title"] as? String ?: "",
            body_parts = enFields["body_parts"] as? List<String> ?: listOf(),
            description = enFields["description"] as? String ?: "",
            dif_level = enFields["dif_level"] as? String ?: "",
            common_mistakes = enFields["common_mistakes"] as? String ?: "",
            steps = enFields["steps"] as? List<String> ?: listOf(),
            tips = enFields["tips"] as? String ?: ""
        ),
    )
}

fun DocumentSnapshot.toPlan(): Plan? {
    return try {
        val enData = get("en") as? Map<String, Any> ?: return null
        val categoryData = enData["category"] as? Map<String, Any> ?: return null
        val levelsData = categoryData["levels"] as? Map<String, Long> ?: return null

        Plan(
            id = id,
            imgURL = getString("img_URL") ?: "",
            description = categoryData["description"] as? String ?: "",
            en = TranslationsPlan(
                title = enData["title"] as? String ?: "",
                description = categoryData["description"] as? String ?: "",
                body_parts = (enData["body_parts"] as? String)?.split(", ") ?: listOf(),
                categories = levelsData,
                weeks = (enData["levels"] as? Map<String, Any>)?.map { (_, weekData) ->
                    weekData as? Map<String, Any>
                }?.mapNotNull { weekData ->
                    if (weekData == null) null
                    else WeekInfo(
                        title = weekData["title"] as? String ?: "",
                        description = weekData["description"] as? String ?: "",
                        days = (weekData["days"] as? Map<String, Any>)?.map { (_, dayData) ->
                            dayData as? Map<String, Any>
                        }?.mapNotNull { dayData ->
                            if (dayData == null) null
                            else DayInfo(
                                title = dayData["title"] as? String ?: "",
                                description = dayData["description"] as? String ?: "",
                                workout = (dayData["workouts"] as? List<String>) ?: listOf()
                            )
                        } ?: listOf()
                    )
                } ?: listOf()
            ),
        )
    } catch (e: Exception) {
        null
    }
}