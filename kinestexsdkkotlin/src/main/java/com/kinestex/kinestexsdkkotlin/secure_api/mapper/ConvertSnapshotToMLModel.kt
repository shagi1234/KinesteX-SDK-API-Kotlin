package com.kinestex.kinestexsdkkotlin.secure_api.mapper

import com.google.firebase.database.DataSnapshot
import com.kinestex.kinestexsdkkotlin.secure_api.models.MLModel
import com.kinestex.kinestexsdkkotlin.secure_api.models.SequenceData


class ConvertSnapshotToMLModel {

    fun toMLModel(snapshot: DataSnapshot): MLModel {
        val ruSequences = extractSequences(snapshot.child("ru").child("sequences"))
        val enSequences = extractSequences(snapshot.child("en").child("sequences"))

        val tfliteAesURL = snapshot.child("tflite_aes_URL").getValue(String::class.java) ?: ""
        val modelType = snapshot.child("model_type").getValue(String::class.java) ?: ""

        return MLModel(
            modelType = modelType,
            tfliteAesURL = tfliteAesURL,
            en = SequenceData(sequences = enSequences),
            ru = SequenceData(sequences = ruSequences)
        )
    }

    private fun extractSequences(sequencesSnapshot: DataSnapshot): Map<String, List<String>> {
        val sequences = mutableMapOf<String, List<String>>()

        for (sequence in sequencesSnapshot.children) {
            val key = sequence.key ?: continue
            val values = sequence.children.mapNotNull { it.getValue(String::class.java) }

            sequences[key] = values
        }
        return sequences
    }
}