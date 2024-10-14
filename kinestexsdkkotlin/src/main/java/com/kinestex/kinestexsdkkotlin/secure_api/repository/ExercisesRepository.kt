package com.kinestex.kinestexsdkkotlin.secure_api.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.kinestex.kinestexsdkkotlin.secure_api.mapper.ConvertDocumentToExercise
import com.kinestex.kinestexsdkkotlin.secure_api.models.Exercise
import com.kinestex.kinestexsdkkotlin.secure_api.models.Resource
import com.kinestex.kinestexsdkkotlin.secure_api.utils.ReferenceKeys
import kotlinx.coroutines.tasks.await


class ExercisesRepository(
    private val convertDocumentToExercise: ConvertDocumentToExercise,
    db: FirebaseFirestore
) {
    private val exercisesCollection = db.collection(ReferenceKeys.EXERCISES_COLLECTION)
    private val exercisesCollectionUpd = db.collection(ReferenceKeys.EXERCISES_COLLECTION_UPD)

    suspend fun getExerciseByName(name: String): Resource<Exercise> {
        return try {
            val documentSnapshot = exercisesCollection.document(name).get().await()

            if (documentSnapshot.exists()) {
                val exercise = convertDocumentToExercise.toExercise(documentSnapshot)
                Resource.Success(data = exercise)
            } else {
                Resource.Failure(exception = Exception("No exercise found with name: $name"))
            }
        } catch (e: Exception) {
            Resource.Failure(exception = e)
        }
    }

    suspend fun getExerciseById(id: String): Resource<Exercise> {
        return try {
            val documentSnapshot = exercisesCollectionUpd.document(id).get().await()

            if (documentSnapshot.exists()) {
                val exercise = convertDocumentToExercise.toExercise(documentSnapshot)
                Resource.Success(data = exercise)
            } else {
                Resource.Failure(exception = Exception("No exercise found with name: $id"))
            }
        } catch (e: Exception) {
            Resource.Failure(exception = e)
        }
    }
}