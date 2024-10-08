package com.kinestex.kinestexsdkkotlin.secure_api.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.kinestex.kinestexsdkkotlin.secure_api.mapper.ConvertSnapshotToExercise
import com.kinestex.kinestexsdkkotlin.secure_api.models.Exercise
import com.kinestex.kinestexsdkkotlin.secure_api.models.Resource
import com.kinestex.kinestexsdkkotlin.secure_api.utils.ReferenceKeys
import com.kinestex.kinestexsdkkotlin.secure_api.utils.ReferenceKeys.Companion.rootRef
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class ExercisesRepository @Inject constructor(
    private val convertSnapshotToExercise: ConvertSnapshotToExercise
) {
    private val exercisesRef: DatabaseReference = rootRef.child(ReferenceKeys.EXERCISES)
    fun getExercise(title: String): Flow<Resource<Exercise>> {
        val resource: MutableStateFlow<Resource<Exercise>> = MutableStateFlow(Resource.Loading())

        val currentRef = exercisesRef.child(title)

        currentRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val exercise = convertSnapshotToExercise.toExercise(dataSnapshot)
                resource.value = Resource.Success(data = exercise)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                resource.value = Resource.Failure(exception = databaseError.toException())
            }
        })

        return resource
    }

    suspend fun getExerciseWithoutFlow(title: String): Resource<Exercise> {
        return suspendCancellableCoroutine { continuation ->
            val currentRef = exercisesRef.child(title)

            currentRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    val exercise = convertSnapshotToExercise.toExercise(dataSnapshot)
                    continuation.resume(Resource.Success(data = exercise))
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    continuation.resumeWithException(databaseError.toException())
                }
            })
        }
    }

    fun getAllExerciseName(): Flow<Resource<List<String>>> {
        val resource: MutableStateFlow<Resource<List<String>>> =
            MutableStateFlow(Resource.Loading())

        val currentRef = exercisesRef

        currentRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val exerciseNames = mutableListOf<String>()

                // Iterate through all children of the dataSnapshot and add the keys to the list
                for (snapshot in dataSnapshot.children) {
                    val key = snapshot.key
                    if (key != null) {
                        exerciseNames.add(key)
                    }
                }

                resource.value = Resource.Success(data = exerciseNames)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                resource.value = Resource.Failure(exception = databaseError.toException())
            }
        })
        return resource
    }

}