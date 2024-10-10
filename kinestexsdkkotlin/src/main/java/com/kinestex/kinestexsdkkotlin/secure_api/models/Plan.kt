package com.kinestex.kinestexsdkkotlin.secure_api.models

data class Plan(
    val id: String,
    val imgURL: String,
    val en: En,
    val description: String,
    val categories: Map<String, Int>, // ex: {"Cardio": 3}
    val weeks: List<WeekInfo>
)
