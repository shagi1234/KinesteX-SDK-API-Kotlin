package com.kinestex.kinestexsdkkotlin.secure_api.models

/**
 * Represents information about a specific day in a workout plan.
 *
 * @property title Title of the day's workout
 * @property description Description of the day's workout
 * @property workout List of workout identifiers for the day
 */
data class DayInfo(
    val title: String,
    val description: String,
    val workout: List<String>
)