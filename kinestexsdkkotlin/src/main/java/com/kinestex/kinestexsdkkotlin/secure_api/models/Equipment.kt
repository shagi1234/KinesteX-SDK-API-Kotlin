package com.kinestex.kinestexsdkkotlin.secure_api.models

data class Equipment(
    val description: String,
    val equipment: String
) {
    constructor() : this(
        "", ""
    )

}

