package com.kinestex.kinestexsdkkotlin.secure_api.repository

import com.google.firebase.database.DatabaseReference
import com.kinestex.kinestexsdkkotlin.secure_api.mapper.ConvertSnapshotToWorkout
import com.kinestex.kinestexsdkkotlin.secure_api.models.Resource
import com.kinestex.kinestexsdkkotlin.secure_api.models.Workout
import com.kinestex.kinestexsdkkotlin.secure_api.utils.ReferenceKeys.Companion.WORKOUTS_UPD
import com.kinestex.kinestexsdkkotlin.secure_api.utils.ReferenceKeys.Companion.WORKOUT_LIST
import com.kinestex.kinestexsdkkotlin.secure_api.utils.ReferenceKeys.Companion.rootRef
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow


/*
 * Created by shagi on 10.02.2024 01:30
*/

class WorkoutsRepository(
    private val convertSnapshotToWorkout: ConvertSnapshotToWorkout
) {
    private val workoutsRef: DatabaseReference = rootRef.child(WORKOUTS_UPD).child(WORKOUT_LIST)

    fun getAllWorkouts(): Flow<Resource<List<Workout>>> {
        val resource: MutableStateFlow<Resource<List<Workout>>> =
            MutableStateFlow(Resource.Loading())

        workoutsRef.get().addOnCompleteListener { task ->
            var response = listOf<Workout>()

            if (task.isSuccessful) {
                val result = task.result
                result?.let {
                    //logic here
                    response = result.children.map { snapShot ->
                        try {
                            convertSnapshotToWorkout.toWorkout(snapShot)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Workout()
                        }

                    }
                }
                resource.value = Resource.Success(data = response)
            } else {
                resource.value = Resource.Failure(exception = task.exception)
            }
        }

        return resource
    }

    //need to change here
    fun getWorkouts(titles: List<String>): Flow<Resource<List<Workout>>> {
        val resource: MutableStateFlow<Resource<List<Workout>>> =
            MutableStateFlow(Resource.Loading())

        val response = mutableListOf<Workout>()
        var queryCount = 0
        val totalQueries = titles.size

        for (title in titles) {
            val query = workoutsRef.orderByChild("title").equalTo(title)
            query.get().addOnCompleteListener { task ->
                queryCount++
                if (task.isSuccessful) {
                    val result = task.result
                    result?.let { snapshot ->
                        snapshot.children.forEach { snapshot ->
                            try {
                                val workout = convertSnapshotToWorkout.toWorkout(snapshot)
                                response.add(workout)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                } else {
                    resource.value = Resource.Failure(exception = task.exception)
                    return@addOnCompleteListener
                }

                if (queryCount == totalQueries) {
                    // All queries have completed
                    resource.value = Resource.Success(data = response)
                }
            }
        }

        return resource
    }


    fun getWorkout(title: String): Flow<Resource<Workout>> {
        val resource: MutableStateFlow<Resource<Workout>> = MutableStateFlow(Resource.Loading())

        val query = workoutsRef.child(title)

        query.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val result = task.result
                result?.let {
                    val workout = convertSnapshotToWorkout.toWorkout(result)
                    resource.value = Resource.Success(data = workout)
                }
            } else {
                resource.value = Resource.Failure(exception = task.exception)
            }
        }

        return resource
    }

}