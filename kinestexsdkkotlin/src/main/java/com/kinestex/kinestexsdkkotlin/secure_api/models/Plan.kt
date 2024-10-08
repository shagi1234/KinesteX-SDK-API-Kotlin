package com.kinestex.kinestexsdkkotlin.secure_api.models

data class Plan(
    val imgURL: String? = null,
    val en: PlanInfo? = null,
    val ru: PlanInfo? = null,
) {
    constructor() : this(
        imgURL = "", en = null, ru = null
    )
}

data class PlanInfo(
    var bodyParts: String? = null,
    var category: Category? = null,
    var levels: List<Level>? = null,
    var title: String? = null
)

data class Level(
    val title: String? = null,
    val description: String? = null,
    val days: List<Day>? = null
)

data class Day(
    val duration: Int? = null,
    val workouts: List<String>? = null,
    val description: String? = null,
    val title: String? = null
)

data class Category(
    val description: String? = null,
    val levels: Map<String, Long>? = null
)
