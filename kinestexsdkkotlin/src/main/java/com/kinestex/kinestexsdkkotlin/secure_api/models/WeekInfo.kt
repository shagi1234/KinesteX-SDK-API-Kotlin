package com.kinestex.kinestexsdkkotlin.secure_api.models

data class WeekInfo(
    val title: String, // ex: Week 1
    val description: String,
    val days: List<DayInfo>
)