package com.kinestex.kinestexsdkkotlin.secure_api.mapper

import com.google.firebase.firestore.DocumentSnapshot
import com.kinestex.kinestexsdkkotlin.secure_api.models.En
import com.kinestex.kinestexsdkkotlin.secure_api.models.Exercise
import com.kinestex.kinestexsdkkotlin.secure_api.models.Workout

class ConvertDocumentToWorkout {
    fun toWorkout(document: DocumentSnapshot): Workout {
        return Workout(
            id = document.id,
            imgURL = document.getString("img_URL") ?: "",
            category = document.getString("category") ?: "",
            total_minutes = document.getLong("total_minutes")?.toInt() ?: 0,
            total_calories = document.getLong("total_calories")?.toInt() ?: 0,
            sequence = extractExerciseSequence(document),
            en = extractLanguageInfo(document),
            title = document.getString("title") ?: "",
            description = document.getString("description") ?: "",
            body_parts = document.get("body_parts") as? List<String> ?: listOf(),
            dif_level = document.getString("dif_level") ?: ""
        )
    }

    private fun extractLanguageInfo(document: DocumentSnapshot): En {
        val enMap = document.get("en") as? Map<String, Any> ?: mapOf()
        return En(
            title = enMap["title"] as? String ?: "",
            body_parts = (enMap["body_parts"] as? List<String>) ?: listOf(),
            description = enMap["description"] as? String ?: ""
        )
    }

    private fun extractExerciseSequence(document: DocumentSnapshot): List<Exercise> {
        val sequenceList = document.get("sequence") as? List<Map<String, Any>> ?: listOf()
        val exerciseConverter = ConvertDocumentToExercise()
        return sequenceList.map { exerciseMap ->
            exerciseConverter.toExercise(exerciseMap)
        }
    }
}