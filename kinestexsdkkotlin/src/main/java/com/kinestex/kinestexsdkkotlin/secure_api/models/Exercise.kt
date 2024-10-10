package com.kinestex.kinestexsdkkotlin.secure_api.models


data class Exercise(
    val id: String,
    val thumbnailURL: String,
    val videoURL: String,
    val avg_reps: Int?,
    val avg_countdown: Int?,
    val rest_duration: Int,
    val en: En,
    val title: String,
    val body_parts: List<String>,
    val description: String,
    val dif_level: String,
    val common_mistakes: String,
    val steps: List<String>,
    val tips: String
)