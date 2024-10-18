package com.kinestex.kinestexsdkkotlin.secure_api.mapper

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.gson.GsonBuilder
import com.kinestex.kinestexsdkkotlin.secure_api.models.*

/**
 * Extension function to convert a Firestore DocumentSnapshot to a Workout object.
 * This function handles the mapping of various fields from the document to the Workout model.
 *
 * @return Workout object or null if conversion fails
 */
fun DocumentSnapshot.toWorkout(): Workout? {
    return try {
        val data = this.data ?: mapOf()
        val gson = GsonBuilder().setPrettyPrinting().create()
        val prettyJson = gson.toJson(data)
        Log.d("DocumentSnapshot.toWorkout", "JSON data: $prettyJson")

        Workout(
            id = id,
            imgURL = getString("body_img") ?: getString("imgURL") ?: "",
            category = getString("category") ?: "",
            total_minutes = getLong("total_minutes")?.toInt() ?: 0,
            total_calories = getLong("calories")?.toInt() ?: getLong("total_calories")?.toInt() ?: 0,
            sequence = (get("sequence") as? Map<String, Any>)?.let { sequenceMap ->
                sequenceMap.entries.sortedBy { it.key }.mapNotNull { (_, exercise) ->
                    (exercise as? Map<String, Any>)?.let { exerciseMap ->
                        WorkoutExercise(
                            title = exerciseMap["title"] as? String ?: "",
                            id = "", // ID is not present in the exercise map, so we're leaving it empty
                            countdown = (exerciseMap["countdown"] as? Number)?.toInt(),
                            repeats = (exerciseMap["repeats"] as? Number)?.toInt(),
                            videoURL = exerciseMap["video_URL"] as? String
                        )
                    }
                }
            } ?: listOf(),
            en = TranslationsWorkout(
                title = (get("en") as? Map<String, Any>)?.get("title") as? String ?: getString("title") ?: "",
                body_parts = ((get("en") as? Map<String, Any>)?.get("body_parts_array") as? List<String>)
                    ?: ((get("filter_fields") as? Map<String, Any>)?.get("en") as? Map<String, Any>)?.get("body_parts_array") as? List<String>
                    ?: ((get("filter_fields") as? Map<String, Any>)?.get("en") as? Map<String, Any>)?.get("body_parts") as? List<String>
                    ?: (((get("filter_fields") as? Map<String, Any>)?.get("en") as? Map<String, Any>)?.get("body_parts") as? String)?.split(",")
                    ?: ((get("en") as? Map<String, Any>)?.get("body_parts") as? String)?.split(",")
                    ?: emptyList(),
                description = ((get("en") as? Map<String, Any>)?.get("description") as? String)
                    ?: ((get("filter_fields") as? Map<String, Any>)?.get("en") as? Map<String, Any>)?.get("description") as? String
                    ?: "",
                dif_level = ((get("en") as? Map<String, Any>)?.get("dif_level") as? String)
                    ?: ((get("filter_fields") as? Map<String, Any>)?.get("en") as? Map<String, Any>)?.get("dif_level") as? String
                    ?: ""
            )
        )
    } catch (e: Exception) {
        Log.e("WorkoutsRepository", "Error converting document to Workout", e)
        null
    }
}

/**
 * Extension function to convert a Firestore DocumentSnapshot to an Exercise object.
 * This function maps the document fields to the Exercise model, including translations.
 *
 * @return Exercise object
 */
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

/**
 * Extension function to convert a Firestore DocumentSnapshot to a Plan object.
 * This function handles the complex nested structure of the Plan model, including weeks and days.
 *
 * @return Plan object or null if conversion fails
 */
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