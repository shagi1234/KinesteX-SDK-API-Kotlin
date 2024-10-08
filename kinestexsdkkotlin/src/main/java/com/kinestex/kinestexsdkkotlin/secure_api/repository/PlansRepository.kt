package com.kinestex.kinestexsdkkotlin.secure_api.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.kinestex.kinestexsdkkotlin.secure_api.mapper.ConvertSnapshotToPlan
import com.kinestex.kinestexsdkkotlin.secure_api.models.Plan
import com.kinestex.kinestexsdkkotlin.secure_api.models.Resource
import com.kinestex.kinestexsdkkotlin.secure_api.utils.ReferenceKeys
import com.kinestex.kinestexsdkkotlin.secure_api.utils.ReferenceKeys.Companion.rootRef
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

/*
 * Created by shagi on 26.03.2024 01:35
 */

class PlansRepository(
    private val convertSnapshotToPlan: ConvertSnapshotToPlan
) {
    private val plansRef: DatabaseReference = rootRef.child(ReferenceKeys.PLANS)

    fun getAllPlans(): Flow<Resource<List<Plan>>> {
        val resource: MutableStateFlow<Resource<List<Plan>>> =
            MutableStateFlow(Resource.Loading())

        plansRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val response = mutableListOf<Plan>()
                for (snapShot in dataSnapshot.children) {
                    try {
                        val plan = convertSnapshotToPlan.toPlan(snapShot)
                        response.add(plan)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                resource.value = Resource.Success(data = response)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                resource.value = Resource.Failure(exception = databaseError.toException())
            }
        })

        return resource
    }
}
