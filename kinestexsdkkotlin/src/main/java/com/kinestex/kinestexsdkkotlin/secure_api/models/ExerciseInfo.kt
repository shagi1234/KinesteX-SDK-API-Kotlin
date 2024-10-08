package com.kinestex.kinestexsdkkotlin.secure_api.models


/*
 * Created by shagi on 26.03.2024 01:32
 */

data class ExerciseInfo(
    val common_mistakes: String?,
    val rest_speech: String?,
    val rep_speech: Map<String, String>?,
    val speech_second: Map<String, String>?,
    val body_parts: List<String>?,
    val description: String?,
    val slow_down_phrases: String?,
    val title: String?,
    val steps: List<String>?,
    val tips: String?,
) {
    override fun toString(): String {
        return """
            |Description: $description
            |Tips: $tips
            |Common Mistakes: $common_mistakes
            |Rest Speech: $rest_speech
            |Rep Speech: $rep_speech
            |Speech Second: $speech_second
            |Body Parts: $body_parts
            |Steps: $steps
        """.trimMargin()
    }
}