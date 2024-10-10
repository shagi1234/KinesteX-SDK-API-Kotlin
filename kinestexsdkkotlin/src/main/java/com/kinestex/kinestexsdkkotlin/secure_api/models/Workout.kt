package com.kinestex.kinestexsdkkotlin.secure_api.models

data class Workout(
    val id: String,
    val imgURL: String,
    val category: String,
    val total_minutes: Int,
    val total_calories: Int,
    val sequence: List<Exercise>,
    val en: En,
    val title: String,
    val description: String,
    val body_parts: List<String>,
    val dif_level: String
)
data class En(
    val title: String,
    val body_parts: List<String>,
    val description: String
)
