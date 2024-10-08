package com.kinestex.kinestexsdkkotlin.secure_api.utils

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ReferenceKeys {
    companion object {
        val rootRef: DatabaseReference = FirebaseDatabase.getInstance().apply {
            setPersistenceEnabled(true) // Enable disk persistence
        }.reference

        const val COLLECTIONS = "collection"
        const val WORKOUTS_UPD = "workouts_upd"
        const val PLANS = "plans"
        const val WORKOUT_LIST = "workouts_list"
        const val EXERCISES = "exercises"
        const val MLModels_world = "MLModels_world"
    }
}