package com.kinestex.kinestexsdkkotlin.secure_api.models


data class Exercise(
    var repeats: Int? = null,
    var countdown: Long? = null,
    var title: String? = null,
    var videoUrl: String? = null,
    val calories: Double? = null,
    val correctSecond: Double? = null,
    val ru: ExerciseInfo? = null,
    val en: ExerciseInfo? = null
) {
    override fun toString(): String {
        return """
            |Exercise Details:
            |  Repeats: $repeats
            |  Countdown: $countdown
            |  Title: $title
            |  Description (RU): ${ru?.description}
            |  Description (EN): ${en?.description}
            |  Video URL: $videoUrl
            |  Calories Burned: $calories
            |  Tips (RU): ${ru?.tips}
            |  Tips (EN): ${en?.tips}
            |  Common Mistakes (RU): ${ru?.common_mistakes}
            |  Common Mistakes (EN): ${en?.common_mistakes}
            |  Rest Speech (RU): ${ru?.rest_speech}
            |  Rest Speech (EN): ${en?.rest_speech}
            |  Rep Speech (RU): ${ru?.rep_speech}
            |  Rep Speech (EN): ${en?.rep_speech}
            |  Speech Second (RU): ${ru?.speech_second}
            |  Speech Second (EN): ${en?.speech_second}
            |  Body Parts (RU): ${ru?.body_parts}
            |  Body Parts (EN): ${en?.body_parts}
            |  Steps (RU): ${ru?.steps}
            |  Steps (EN): ${en?.steps}
        """.trimMargin()
    }
}