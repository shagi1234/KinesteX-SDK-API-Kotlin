package com.kinestex.kinestexsdkkotlin.secure_api.mapper

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.kinestex.kinestexsdkkotlin.secure_api.models.En
import com.kinestex.kinestexsdkkotlin.secure_api.models.Workout
import com.kinestex.kinestexsdkkotlin.secure_api.models.WorkoutExercise

class ConvertDocumentToWorkout {
    fun toWorkout(document: DocumentSnapshot): Workout {

        val data = document.data ?: mapOf()

        return Workout(
            id = document.id,
            imgURL = data["body_img"] as? String ?: "",
            category = data["category"] as? String ?: "",
            total_minutes = (data["total_minutes"] as? Number)?.toInt() ?: 0,
            total_calories = (data["calories"] as? Number)?.toInt() ?: 0,
            sequence = extractExerciseSequence(data),
            en = extractLanguageInfo(data),
            title = (data["en"] as? Map<*, *>)?.get("title") as? String ?: "",
            description = (data["en"] as? Map<*, *>)?.get("description") as? String ?: "",
            body_parts = (data["en"] as? Map<*, *>)?.get("body_parts_array") as? List<String> ?: listOf(),
            dif_level = (data["en"] as? Map<*, *>)?.get("dif_level") as? String ?: ""
        )
    }

    private fun extractLanguageInfo(data: Map<String, Any>): En {
        val enMap = data["en"] as? Map<String, Any> ?: mapOf()
        return En(
            title = enMap["title"] as? String ?: "",
            body_parts = (enMap["body_parts_array"] as? List<String>) ?: listOf(),
            description = enMap["description"] as? String ?: ""
        )
    }

    private fun extractExerciseSequence(data: Map<String, Any>): List<WorkoutExercise> {
        val sequenceMap = data["sequence"] as? Map<String, Map<String, Any>> ?: mapOf()
        return sequenceMap.values.mapNotNull { exerciseMap ->
            WorkoutExercise(
                title = exerciseMap["title"] as? String ?: return@mapNotNull null,
                countdown = (exerciseMap["countdown"] as? Number)?.toInt(),
                repeats = (exerciseMap["repeats"] as? Number)?.toInt()
            )
        }
    }
}