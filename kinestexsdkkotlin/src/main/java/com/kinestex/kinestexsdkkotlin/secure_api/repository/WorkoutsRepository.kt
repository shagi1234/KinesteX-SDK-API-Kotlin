package com.kinestex.kinestexsdkkotlin.secure_api.repository


/*
 * Created by shagi on 10.02.2024 01:30
*/

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.kinestex.kinestexsdkkotlin.secure_api.mapper.toWorkout
import com.kinestex.kinestexsdkkotlin.secure_api.models.Resource
import com.kinestex.kinestexsdkkotlin.secure_api.models.Workout
import com.kinestex.kinestexsdkkotlin.secure_api.utils.ReferenceKeys
import kotlinx.coroutines.tasks.await

class WorkoutsRepository(
    db: FirebaseFirestore
) {
    private val workoutsCollection = db.collection(ReferenceKeys.WORKOUTS_COLLECTION)
    private val workoutsCollectionUpd = db.collection(ReferenceKeys.WORKOUTS_COLLECTION_UPD)

    suspend fun getWorkoutByTitle(title: String): Resource<Workout> {
        return try {
            val querySnapshot = workoutsCollection.document(title).get().await()
            if (querySnapshot.exists()) {
                Resource.Success(data = querySnapshot.toWorkout())
            } else {
                Resource.Failure(exception = Exception("No workout found with title: $title"))
            }
        } catch (e: Exception) {
            Resource.Failure(exception = e)
        }
    }

    suspend fun getWorkoutByID(id: String): Resource<Workout> {
        return try {
            val documentSnapshot =
                workoutsCollectionUpd.document(id).collection("translations").document("en").get()
                    .await()
            if (documentSnapshot.exists()) {
                Resource.Success(data = documentSnapshot.toWorkout())
            } else {
                Resource.Failure(exception = Exception("No workout found with ID: $id"))
            }
        } catch (e: Exception) {
            Resource.Failure(exception = e)
        }
    }

    suspend fun getWorkoutsByCategory(category: String): Resource<List<Workout>> {
        return try {
            val querySnapshot =
                workoutsCollectionUpd.whereEqualTo("category", category).get().await()

            if (querySnapshot.isEmpty) {
                return Resource.Success(data = emptyList())
            }

            var listOfWorkouts = mutableListOf<Workout>()

            val workout = querySnapshot.documents.first().toWorkout()

            workout?.let { listOfWorkouts.add(it) }

            Log.e("Ejen sikeyn", "getWorkoutsByCategory: ${listOfWorkouts.size}")
            Resource.Success(data = listOfWorkouts)

        } catch (e: Exception) {
            Resource.Failure(exception = e)
        }
    }
}