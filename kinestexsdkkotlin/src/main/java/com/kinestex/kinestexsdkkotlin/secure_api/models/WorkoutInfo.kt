package com.kinestex.kinestexsdkkotlin.secure_api.models


/*
 * Created by shagi on 26.03.2024 01:32
 */

data class WorkoutInfo(
    val body_parts: String,
    val dif_level: String,
    val description: String,
    val equipment: Equipment
) {
    constructor() : this(
        "", "", "", Equipment("", "")
    )
}