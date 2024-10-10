package com.kinestex.kinestexsdkkotlin.secure_api.utils

import com.google.firebase.firestore.FirebaseFirestore

class ReferenceKeys {
    companion object {
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()

        const val WORKOUTS_COLLECTION = "workouts"
        const val PLANS_COLLECTION = "plans"
        const val EXERCISES_COLLECTION = "exercises"
    }
}