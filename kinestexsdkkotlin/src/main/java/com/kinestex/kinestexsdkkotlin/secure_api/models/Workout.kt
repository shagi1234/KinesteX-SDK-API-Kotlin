package com.kinestex.kinestexsdkkotlin.secure_api.models

data class Workout(
    val id: String,
    val imgURL: String,
    val category: String,
    val total_minutes: Int,
    val total_calories: Int,
    val sequence: List<WorkoutExercise>,
    val en: TranslationsWorkout,
)

data class WorkoutExercise(
    val title: String?,
    val id: String?,
    val countdown: Int?,
    val repeats: Int?,
    val videoURL: String?
)

data class TranslationsWorkout(
    val title: String?,
    val body_parts: List<String>?,
    val description: String?,
    val dif_level: String?,
)
