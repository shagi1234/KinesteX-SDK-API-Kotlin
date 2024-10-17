package com.kinestex.kinestexsdkkotlin.secure_api.models

/**
 * Represents a workout plan in the Kinestex system.
 *
 * @property id Unique identifier for the plan
 * @property imgURL URL of the plan image
 * @property en Translations for the plan details
 * @property description Description of the plan
 */
data class Plan(
    val id: String,
    val imgURL: String,
    val en: TranslationsPlan,
    val description: String
)

/**
 * Represents translations for plan details.
 *
 * @property title Translated title of the plan
 * @property body_parts List of body parts targeted in the plan
 * @property description Translated description of the plan
 * @property categories Map of categories and their levels (e.g., {"Cardio": 3})
 * @property weeks List of week information for the plan
 */
data class TranslationsPlan(
    val title: String,
    val body_parts: List<String>,
    val description: String,
    val categories: Map<String, Long>,
    val weeks: List<WeekInfo>
)
