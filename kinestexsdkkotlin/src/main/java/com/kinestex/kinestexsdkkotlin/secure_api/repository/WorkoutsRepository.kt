package com.kinestex.kinestexsdkkotlin.secure_api.repository


/*
 * Created by shagi on 10.02.2024 01:30
*/

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.kinestex.kinestexsdkkotlin.secure_api.mapper.ConvertDocumentToWorkout
import com.kinestex.kinestexsdkkotlin.secure_api.models.Resource
import com.kinestex.kinestexsdkkotlin.secure_api.models.Workout
import com.kinestex.kinestexsdkkotlin.secure_api.utils.ReferenceKeys
import kotlinx.coroutines.tasks.await

class WorkoutsRepository(
    private val convertDocumentToWorkout: ConvertDocumentToWorkout,
    db: FirebaseFirestore
) {
    private val workoutsCollection = db.collection(ReferenceKeys.WORKOUTS_COLLECTION)
    private val workoutsCollectionUpd = db.collection(ReferenceKeys.WORKOUTS_COLLECTION_UPD)

    suspend fun getWorkoutByTitle(title: String): Resource<Workout> {
        return try {
            val querySnapshot = workoutsCollection.document(title).get().await()
            if (querySnapshot.exists()) {
                val workout = convertDocumentToWorkout.toWorkout(querySnapshot)
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
            val documentSnapshot = workoutsCollectionUpd.document(id).collection("translations").document("en").get().await()
            if (documentSnapshot.exists()) {
                Log.e("getWorkoutByID", "English translation data: ${Gson().toJson(documentSnapshot.data)}")
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