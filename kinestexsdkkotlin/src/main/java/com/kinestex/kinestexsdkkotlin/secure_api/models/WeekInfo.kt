package com.kinestex.kinestexsdkkotlin.secure_api.models

/**
 * Represents information about a week in a workout plan.
 *
 * @property title Title of the week (e.g., "Week 1")
 * @property description Description of the week's workouts
 * @property days List of daily workout information
 */
data class WeekInfo(
    val title: String,
    val description: String,
    val days: List<DayInfo>
)