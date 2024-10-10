package com.kinestex.kinestexsdkkotlin.secure_api.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.kinestex.kinestexsdkkotlin.secure_api.mapper.ConvertDocumentToExercise
import com.kinestex.kinestexsdkkotlin.secure_api.models.Exercise
import com.kinestex.kinestexsdkkotlin.secure_api.models.Resource
import com.kinestex.kinestexsdkkotlin.secure_api.utils.ReferenceKeys
import kotlinx.coroutines.tasks.await


class ExercisesRepository(
    private val convertDocumentToExercise: ConvertDocumentToExercise
) {
    private val exercisesCollection = ReferenceKeys.db.collection(ReferenceKeys.EXERCISES_COLLECTION)

    suspend fun getExerciseByName(name: String, isEnglish: Boolean = true): Resource<Exercise> {
        return try {
            val field = if (isEnglish) "en.title" else "title"
            val querySnapshot = exercisesCollection.whereEqualTo(field, name).get().await()
            if (!querySnapshot.isEmpty) {
                val exercise = convertDocumentToExercise.toExercise(querySnapshot.documents.first())
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
            val documentSnapshot = exercisesCollection.document(id).get().await()
            if (documentSnapshot.exists()) {
                val exercise = convertDocumentToExercise.toExercise(documentSnapshot)
                Resource.Success(data = exercise)
            } else {
                Resource.Failure(exception = Exception("No exercise found with ID: $id"))
            }
        } catch (e: Exception) {
            Resource.Failure(exception = e)
        }
    }
}