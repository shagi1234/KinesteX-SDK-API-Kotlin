package com.kinestex.kinestexsdkkotlin.secure_api.repository

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.kinestex.kinestexsdkkotlin.secure_api.mapper.toWorkout
import com.kinestex.kinestexsdkkotlin.secure_api.models.Resource
import com.kinestex.kinestexsdkkotlin.secure_api.models.Workout
import com.kinestex.kinestexsdkkotlin.secure_api.utils.ReferenceKeys
import kotlinx.coroutines.tasks.await

/**
 * Repository class for handling Workout-related operations with Firestore.
 *
 * @param db FirebaseFirestore instance for database operations
 */
class WorkoutsRepository(db: FirebaseFirestore) {
    // Reference to the main workouts collection
    private val workoutsCollectionUpd = db.collection(ReferenceKeys.WORKOUTS_COLLECTION_UPD)

    /**
     * Fetches a workout by its title from the main workouts collection.
     *
     * @param title The title of the workout to fetch
     * @return Resource<Workout> representing the result of the operation
     */
    suspend fun getWorkoutByTitle(title: String): Resource<Workout> {
        return try {
            // First, query for workouts with a direct title match
            var querySnapshot = workoutsCollectionUpd
                .whereEqualTo("title", title)
                .get()
                .await()

            if (querySnapshot.documents.isEmpty()) {
                // If no direct match, query for workouts with matching English translation title
                querySnapshot = workoutsCollectionUpd
                    .whereEqualTo("translations.en.title", title)
                    .get()
                    .await()
            }

            println("Total documents retrieved: ${querySnapshot.size()}")

            if (querySnapshot.documents.isNotEmpty()) {
                val document = querySnapshot.documents.first()
                val workout = document.toWorkout()
                if (workout != null) {
                    Resource.Success(data = workout)
                } else {
                    Resource.Failure(exception = Exception("Failed to parse workout data"))
                }
            } else {
                Resource.Failure(exception = Exception("No workout found with title: $title"))
            }
        } catch (e: Exception) {
            println("Error: ${e.message}")
            Resource.Failure(exception = e)
        }
    }
    /**
     * Fetches a workout by its ID from the updated workouts collection, specifically the English translation.
     *
     * @param id The ID of the workout to fetch
     * @return Resource<Workout> representing the result of the operation
     */
    suspend fun getWorkoutByID(id: String): Resource<Workout> {
        return try {
            val documentSnapshot = workoutsCollectionUpd.document(id)
                .get()
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

    /**
     * Fetches all workouts belonging to a specific category from the updated workouts collection.
     *
     * @param category The category of workouts to fetch
     * @return Resource<List<Workout>> representing the result of the operation
     */
    suspend fun getWorkoutsByCategory(category: String): Resource<List<Workout>> {
        return try {
            val querySnapshot = workoutsCollectionUpd.whereEqualTo("category", category).get().await()
            if (querySnapshot.isEmpty) {
                return Resource.Success(data = emptyList())
            }
            val workouts = querySnapshot.documents.mapNotNull { document ->
                document.toWorkout()
            }
            Resource.Success(data = workouts)
        } catch (e: Exception) {
            Resource.Failure(exception = e)
        }
    }
}