package com.kinestex.kinestexsdkkotlin.secure_api.mapper

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.kinestex.kinestexsdkkotlin.secure_api.models.Exercise
import com.kinestex.kinestexsdkkotlin.secure_api.models.ExerciseInfo

/*
 * Created by shagi on 25.03.2024 02:29
 */

class ConvertSnapshotToExercise {

    fun toExercise(snapshot: DataSnapshot): Exercise {
        Log.e("TAG_exercise_snapshot", "toExercise: " + snapshot.value )
        val ruDataSnapshot = snapshot.child("ru")
        val enDataSnapshot = snapshot.child("en")
        val correctSecond = snapshot.child("correct_second").getValue(Double::class.java) ?: 0.0
        val calories = snapshot.child("calories").getValue(Double::class.java) ?: 0.0
        val videoURL = snapshot.child("video_URL").getValue(String::class.java) ?: ""

        val ruInfo = extractExerciseInfo(ruDataSnapshot)
        val enInfo = extractExerciseInfo(enDataSnapshot)

        return Exercise(
            videoUrl = videoURL,
            calories = calories,
            correctSecond = correctSecond,
            ru = ruInfo,
            en = enInfo
        )
    }

    private fun extractExerciseInfo(dataSnapshot: DataSnapshot): ExerciseInfo {
        val repSpeechSnapshot = dataSnapshot.child("rep_speech")
        val repSpeech = mutableMapOf<String, String>()
        repSpeechSnapshot.children.forEach { child ->
            repSpeech[child.key ?: ""] = child.getValue(String::class.java) ?: ""
        }

        val speechSecondSnapshot = dataSnapshot.child("speech_second")
        val speechSecond = mutableMapOf<String, String>()
        speechSecondSnapshot.children.forEach { child ->
            speechSecond[child.key ?: ""] = child.getValue(String::class.java) ?: ""
        }

        val bodyPartsSnapshot = dataSnapshot.child("body_parts")
        val bodyParts = mutableListOf<String>()
        bodyPartsSnapshot.children.forEach { child ->
            bodyParts.add(child.getValue(String::class.java) ?: "")
        }

        val stepsSnapshot = dataSnapshot.child("steps")
        val steps = mutableListOf<String>()
        stepsSnapshot.children.forEach { child ->
            steps.add(child.getValue(String::class.java) ?: "")
        }

        return ExerciseInfo(
            common_mistakes = dataSnapshot.child("common_mistakes").getValue(String::class.java)
                ?: "",
            rest_speech = dataSnapshot.child("rest_speech").getValue(String::class.java) ?: "",
            rep_speech = repSpeech,
            speech_second = speechSecond,
            body_parts = bodyParts,
            description = dataSnapshot.child("description").getValue(String::class.java) ?: "",
            slow_down_phrases = dataSnapshot.child("slow_down_phrases").getValue(String::class.java)
                ?: "",
            title = dataSnapshot.child("title").getValue(String::class.java) ?: "",
            steps = steps,
            tips = dataSnapshot.child("tips").getValue(String::class.java) ?: ""
        )
    }

}
