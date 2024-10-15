package com.kinestex.kinestexsdkkotlin.secure_api.mapper

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.kinestex.kinestexsdkkotlin.secure_api.models.En
import com.kinestex.kinestexsdkkotlin.secure_api.models.Exercise

class ConvertDocumentToExercise {
    fun toExercise(document: DocumentSnapshot): Exercise {
        val data = document.data ?: mapOf()
        val gson: Gson = GsonBuilder().setPrettyPrinting().create()
        val prettyJson = gson.toJson(data)
        Log.e("ConvertDocumentToExercise", "toExercise: $prettyJson")

        return convertToExercise(data, document.id)
    }

    private fun convertToExercise(exerciseMap: Map<String, Any>, id: String): Exercise {
        val translations = exerciseMap["translations"] as? Map<String, Any>
        val enFields = translations?.get("en") as? Map<String, Any> ?: exerciseMap

        return Exercise(
            id = id,
            thumbnailURL = exerciseMap["thumbnail_URL"] as? String
                ?: exerciseMap["thumbnail_url"] as? String ?: "",
            videoURL = exerciseMap["video_URL"] as? String
                ?: exerciseMap["video_url"] as? String ?: "",
            avg_reps = (exerciseMap["repeats"] as? Double)?.toInt(),
            avg_countdown = (exerciseMap["correct_second"] as? Double)?.toInt(),
            rest_duration = (enFields["rest_duration"] as? Double)?.toInt() ?: 0,
            en = En(
                title = enFields["title"] as? String ?: enFields["name"] as? String ?: "",
                body_parts = enFields["body_parts"] as? List<String> ?: listOf(),
                description = enFields["description"] as? String ?: ""
            ),
            title = enFields["title"] as? String ?: enFields["name"] as? String ?: "",
            body_parts = enFields["body_parts"] as? List<String> ?: listOf(),
            description = enFields["description"] as? String ?: "",
            dif_level = enFields["dif_level"] as? String ?: "",
            common_mistakes = enFields["common_mistakes"] as? String ?: "",
            steps = enFields["steps"] as? List<String> ?: listOf(),
            tips = enFields["tips"] as? String ?: ""
        )
    }
}