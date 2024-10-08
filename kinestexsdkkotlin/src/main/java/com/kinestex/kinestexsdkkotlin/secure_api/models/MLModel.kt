package com.kinestex.kinestexsdkkotlin.secure_api.models

import kotlinx.serialization.Serializable

@Serializable
data class SequenceData(
    val sequences: Map<String, List<String>>
)

@Serializable
data class MLModel(
    val modelType: String,
    val tfliteAesURL: String,
    val en: SequenceData,
    val ru: SequenceData,
    var localPath: String? = null
)