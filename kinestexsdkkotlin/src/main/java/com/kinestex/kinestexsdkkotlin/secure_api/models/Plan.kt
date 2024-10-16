package com.kinestex.kinestexsdkkotlin.secure_api.models

data class Plan(
    val id: String,
    val imgURL: String,
    val en: TranslationsPlan,
    val description: String
)

data class TranslationsPlan(
    val title: String,
    val body_parts: List<String>,
    val description: String,
    val categories: Map<String, Long>, // ex: {"Cardio": 3}
    val weeks: List<WeekInfo>
)
