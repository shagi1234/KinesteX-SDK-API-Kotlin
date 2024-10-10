package com.kinestex.kinestexsdkkotlin.secure_api.repository


/*
 * Created by shagi on 10.02.2024 01:30
*/

import com.kinestex.kinestexsdkkotlin.secure_api.mapper.ConvertDocumentToWorkout
import com.kinestex.kinestexsdkkotlin.secure_api.models.Resource
import com.kinestex.kinestexsdkkotlin.secure_api.models.Workout
import com.kinestex.kinestexsdkkotlin.secure_api.utils.ReferenceKeys
import kotlinx.coroutines.tasks.await

class WorkoutsRepository(
    private val convertDocumentToWorkout: ConvertDocumentToWorkout
) {
    private val workoutsCollection = ReferenceKeys.db.collection(ReferenceKeys.WORKOUTS_COLLECTION)

    suspend fun getWorkoutByTitle(title: String): Resource<Workout> {
        return try {
            val querySnapshot = workoutsCollection.whereEqualTo("title", title).get().await()
            if (!querySnapshot.isEmpty) {
                val workout = convertDocumentToWorkout.toWorkout(querySnapshot.documents.first())
                Resource.Success(data = workout)
            } else {
                Resource.Failure(exception = Exception("No workout found with title: $title"))
            }
        } catch (e: Exception) {
            Resource.Failure(exception = e)
        }
    }

    suspend fun getWorkoutByID(id: String): Resource<Workout> {
        return try {
            val documentSnapshot = workoutsCollection.document(id).get().await()
            if (documentSnapshot.exists()) {
                val workout = convertDocumentToWorkout.toWorkout(documentSnapshot)
                Resource.Success(data = workout)
            } else {
                Resource.Failure(exception = Exception("No workout found with ID: $id"))
            }
        } catch (e: Exception) {
            Resource.Failure(exception = e)
        }
    }

    suspend fun getWorkoutsByCategory(category: String): Resource<List<Workout>> {
        if (!isValidCategory(category)) {
            return Resource.Failure(
                exception = IllegalArgumentException(
                    "Invalid category. Must be one of: Stretching, Cardio, Yoga, Strength, Mobility"
                )
            )
        }

        return try {
            val querySnapshot = workoutsCollection.whereEqualTo("category", category).get().await()
            val workouts = querySnapshot.documents.map { convertDocumentToWorkout.toWorkout(it) }
            Resource.Success(data = workouts)
        } catch (e: Exception) {
            Resource.Failure(exception = e)
        }
    }

    private fun isValidCategory(category: String): Boolean {
        return category in setOf("Stretching", "Cardio", "Yoga", "Strength", "Mobility")
    }
}