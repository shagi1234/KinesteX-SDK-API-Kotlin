package com.kinestex.kinestexsdkkotlin.secure_api.models


/**
 * Represents an exercise in the Kinestex system.
 *
 * @property id Unique identifier for the exercise
 * @property thumbnailURL URL of the exercise thumbnail image
 * @property videoURL URL of the exercise video
 * @property avg_reps Average number of repetitions for the exercise
 * @property avg_countdown Average countdown time for the exercise (in seconds)
 * @property rest_duration Rest duration after the exercise (in seconds)
 * @property en Translations for the exercise details
 */
data class Exercise(
    val id: String,
    val thumbnailURL: String,
    val videoURL: String,
    val avg_reps: Int?,
    val avg_countdown: Int?,
    val rest_duration: Int,
    val en: TranslationsExercise,
)


/**
 * Represents translations for exercise details.
 *
 * @property title Translated title of the exercise
 * @property body_parts List of body parts targeted by the exercise
 * @property description Translated description of the exercise
 * @property dif_level Difficulty level of the exercise
 * @property common_mistakes Common mistakes to avoid during the exercise
 * @property steps List of steps to perform the exercise
 * @property tips Tips for performing the exercise correctly
 */
data class TranslationsExercise(
    val title: String,
    val body_parts: List<String>,
    val description: String,
    val dif_level: String,
    val common_mistakes: String,
    val steps: List<String>,
    val tips: String
)