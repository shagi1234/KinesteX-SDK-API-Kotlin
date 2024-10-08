package com.kinestex.kinestexsdkkotlin.secure_api.models

data class Workout(
    var title: String? = null,
    val sequence: Map<String, Exercise>,
    val ru: WorkoutInfo,
    val total_minutes: Int,
    val body_img: String,
    val workout_type: String,
    val en: WorkoutInfo,
    val calories: Double,
    val category: String,
    val workout_desc_img: String,
    val is_testing: Boolean
) {
    constructor() : this(
        "",
        mapOf(), WorkoutInfo("", "", "", Equipment("", "")),
        0, "", "", WorkoutInfo("", "", "", Equipment("", "")),
        0.0, "", "", false
    )

    override fun toString(): String {
        val sequenceString = sequence.entries.joinToString(separator = "\n") { (key, value) ->
            "Exercise $key: $value"
        }

        val ruInfoString = ru.toString().prependIndent("|   ")
        val enInfoString = en.toString().prependIndent("|   ")

        return """
        |Sequence:
        $sequenceString
        |RU Info:
        $ruInfoString
        |Total Minutes: $total_minutes
        |Body Image: $body_img
        |Workout Type: $workout_type
        |EN Info:
        $enInfoString
        |Calories: $calories
        |Category: $category
        |Workout Description Image: $workout_desc_img
        |Is Testing: $is_testing
    """.trimMargin()
    }
}
