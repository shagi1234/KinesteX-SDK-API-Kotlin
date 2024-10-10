package com.kinestex.kinestexsdkkotlin.secure_api.mapper

import com.google.firebase.firestore.DocumentSnapshot
import com.kinestex.kinestexsdkkotlin.secure_api.models.En
import com.kinestex.kinestexsdkkotlin.secure_api.models.Exercise

class ConvertDocumentToExercise {
    fun toExercise(document: DocumentSnapshot): Exercise {
        return toExercise(document.data ?: mapOf())
    }

    fun toExercise(exerciseMap: Map<String, Any>): Exercise {
        return Exercise(
            id = exerciseMap["id"] as? String ?: "",
            thumbnailURL = exerciseMap["thumbnail_URL"] as? String ?: "",
            videoURL = exerciseMap["video_URL"] as? String ?: "",
            avg_reps = (exerciseMap["avg_reps"] as? Long)?.toInt(),
            avg_countdown = (exerciseMap["avg_countdown"] as? Long)?.toInt(),
            rest_duration = (exerciseMap["rest_duration"] as? Long)?.toInt() ?: 0,
            en = extractLanguageInfo(exerciseMap),
            title = exerciseMap["title"] as? String ?: "",
            body_parts = exerciseMap["body_parts"] as? List<String> ?: listOf(),
            description = exerciseMap["description"] as? String ?: "",
            dif_level = exerciseMap["dif_level"] as? String ?: "",
            common_mistakes = exerciseMap["common_mistakes"] as? String ?: "",
            steps = exerciseMap["steps"] as? List<String> ?: listOf(),
            tips = exerciseMap["tips"] as? String ?: ""
        )
    }

    private fun extractLanguageInfo(exerciseMap: Map<String, Any>): En {
        val enMap = exerciseMap["en"] as? Map<String, Any> ?: mapOf()
        return En(
            title = enMap["title"] as? String ?: "",
            body_parts = (enMap["body_parts"] as? List<String>) ?: listOf(),
            description = enMap["description"] as? String ?: ""
        )
    }
}