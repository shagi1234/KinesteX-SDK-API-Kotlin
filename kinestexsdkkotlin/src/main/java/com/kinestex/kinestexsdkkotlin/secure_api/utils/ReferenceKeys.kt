package com.kinestex.kinestexsdkkotlin.secure_api.utils
/**
 * Utility class containing constant values for Firestore collection references.
 */
class ReferenceKeys {
    companion object {
        /** Collection name for the main workouts data */
        const val WORKOUTS_COLLECTION = "workouts"

        /** Collection name for the updated workouts data */
        const val WORKOUTS_COLLECTION_UPD = "workouts_upd"

        /** Collection name for plans data */
        const val PLANS_COLLECTION = "plans"

        /** Collection name for the main exercises data */
        const val EXERCISES_COLLECTION = "exercises"

        /** Collection name for the updated exercises data */
        const val EXERCISES_COLLECTION_UPD = "exercises_upd"
    }
}