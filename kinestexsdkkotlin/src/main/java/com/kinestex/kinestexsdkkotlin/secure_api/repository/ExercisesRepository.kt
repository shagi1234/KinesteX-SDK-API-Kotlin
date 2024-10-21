package com.kinestex.kinestexsdkkotlin.secure_api.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.kinestex.kinestexsdkkotlin.secure_api.mapper.toExercise
import com.kinestex.kinestexsdkkotlin.secure_api.mapper.toWorkout
import com.kinestex.kinestexsdkkotlin.secure_api.models.Exercise
import com.kinestex.kinestexsdkkotlin.secure_api.models.Resource
import com.kinestex.kinestexsdkkotlin.secure_api.models.Workout
import com.kinestex.kinestexsdkkotlin.secure_api.utils.ReferenceKeys
import kotlinx.coroutines.tasks.await

/**
 * Repository class for handling Exercise-related operations with Firestore.
 *
 * @param db FirebaseFirestore instance for database operations
 */
class ExercisesRepository(db: FirebaseFirestore) {
    // Reference to the main exercises collection
    private val exercisesCollectionUpd = db.collection(ReferenceKeys.EXERCISES_COLLECTION_UPD)

    /**
     * Fetches an exercise by its name from the main collection.
     *
     * @param name The name of the exercise to fetch
     * @return Resource<Exercise> representing the result of the operation
     */

    suspend fun getExerciseByName(name: String): Resource<Exercise> {
        return try {
            val querySnapshot = exercisesCollectionUpd
                .whereEqualTo("name", name)
                .get()
                .await()

            if (querySnapshot.documents.isNotEmpty()) {
                val document = querySnapshot.documents.first()
                val exercise = document.toExercise()
                Resource.Success(data = exercise)
            } else {
                // If not found by direct name, check translations
                val translationQuery = exercisesCollectionUpd
                    .whereEqualTo("translations.en.title", name)
                    .get()
                    .await()

                if (translationQuery.documents.isNotEmpty()) {
                    val document = translationQuery.documents.first()
                    val exercise = document.toExercise()
                    Resource.Success(data = exercise)
                } else {
                    Resource.Failure(exception = Exception("No workout found with title: $name"))
                }
            }
        } catch (e: Exception) {
            Resource.Failure(exception = e)
        }
    }

    /**
     * Fetches an exercise by its ID from the updated collection, specifically the English translation.
     *
     * @param id The ID of the exercise to fetch
     * @return Resource<Exercise> representing the result of the operation
     */
    suspend fun getExerciseById(id: String): Resource<Exercise> {
        return try {
            val translationsSnapshot = exercisesCollectionUpd.document(id)
                .collection("translations")
                .document("en")
                .get()
                .await()
            if (translationsSnapshot.exists()) {
                Resource.Success(data = translationsSnapshot.toExercise())
            } else {
                Resource.Failure(exception = Exception("No English translation found for exercise with ID: $id"))
            }
        } catch (e: Exception) {
            Log.e("GetExerciseById", "Error: ${e.message}", e)
            Resource.Failure(exception = e)
        }
    }
}