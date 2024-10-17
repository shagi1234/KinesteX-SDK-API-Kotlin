package com.kinestex.kinestexsdkkotlin.secure_api.models

/**
 * Represents a workout in the Kinestex system.
 *
 * @property id Unique identifier for the workout
 * @property imgURL URL of the workout image
 * @property category Category of the workout
 * @property total_minutes Total duration of the workout in minutes
 * @property total_calories Total calories burned during the workout
 * @property sequence List of exercises in the workout
 * @property en Translations for the workout details
 */
data class Workout(
    val id: String,
    val imgURL: String,
    val category: String,
    val total_minutes: Int,
    val total_calories: Int,
    val sequence: List<WorkoutExercise>,
    val en: TranslationsWorkout,
)

/**
 * Represents an exercise within a workout.
 *
 * @property title Title of the exercise
 * @property id Unique identifier for the exercise
 * @property countdown Countdown time for the exercise (in seconds)
 * @property repeats Number of repetitions for the exercise
 * @property videoURL URL of the exercise video
 */
data class WorkoutExercise(
    val title: String?,
    val id: String?,
    val countdown: Int?,
    val repeats: Int?,
    val videoURL: String?
)

/**
 * Represents translations for workout details.
 *
 * @property title Translated title of the workout
 * @property body_parts List of body parts targeted in the workout
 * @property description Translated description of the workout
 * @property dif_level Difficulty level of the workout
 */
data class TranslationsWorkout(
    val title: String?,
    val body_parts: List<String>?,
    val description: String?,
    val dif_level: String?,
)
