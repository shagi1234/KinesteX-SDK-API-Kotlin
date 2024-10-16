package com.kinestex.kinestexsdkkotlin.secure_api.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.kinestex.kinestexsdkkotlin.secure_api.mapper.toExercise
import com.kinestex.kinestexsdkkotlin.secure_api.models.Exercise
import com.kinestex.kinestexsdkkotlin.secure_api.models.Resource
import com.kinestex.kinestexsdkkotlin.secure_api.utils.ReferenceKeys
import kotlinx.coroutines.tasks.await


class ExercisesRepository(
    db: FirebaseFirestore
) {
    private val exercisesCollection = db.collection(ReferenceKeys.EXERCISES_COLLECTION)
    private val exercisesCollectionUpd = db.collection(ReferenceKeys.EXERCISES_COLLECTION_UPD)

    suspend fun getExerciseByName(name: String): Resource<Exercise> {
        return try {
            val documentSnapshot = exercisesCollection.document(name).get().await()
            if (documentSnapshot.exists()) {
                Resource.Success(data = documentSnapshot.toExercise())
            } else {
                Resource.Failure(exception = Exception("No exercise found with name: $name"))
            }
        } catch (e: Exception) {
            Resource.Failure(exception = e)
        }
    }

    suspend fun getExerciseById(id: String): Resource<Exercise> {
        return try {
            val translationsSnapshot =
                exercisesCollectionUpd.document(id).collection("translations").document("en").get()
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