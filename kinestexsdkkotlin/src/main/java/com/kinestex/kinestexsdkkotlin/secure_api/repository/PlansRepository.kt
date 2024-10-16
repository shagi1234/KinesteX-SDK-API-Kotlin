package com.kinestex.kinestexsdkkotlin.secure_api.repository

import com.kinestex.kinestexsdkkotlin.secure_api.models.Plan
import com.kinestex.kinestexsdkkotlin.secure_api.models.Resource
import com.kinestex.kinestexsdkkotlin.secure_api.utils.ReferenceKeys

/*
 * Created by shagi on 26.03.2024 01:35
 */

import com.google.firebase.firestore.FirebaseFirestore
import com.kinestex.kinestexsdkkotlin.secure_api.mapper.toPlan
import kotlinx.coroutines.tasks.await

class PlansRepository(
    db: FirebaseFirestore
) {
    private val plansCollection = db.collection(ReferenceKeys.PLANS_COLLECTION)

    suspend fun getPlanByName(name: String): Resource<Plan> {
        return try {
            val querySnapshot = plansCollection.document(name).get().await()
            if (querySnapshot.exists()) {
                Resource.Success(data = querySnapshot.toPlan())
            } else {
                Resource.Failure(exception = Exception("No plan found with name: $name"))
            }
        } catch (e: Exception) {
            Resource.Failure(exception = e)
        }
    }

    suspend fun getPlanById(id: String): Resource<Plan> {
        return try {
            val documentSnapshot = plansCollection.document(id).get().await()
            if (documentSnapshot.exists()) {
                Resource.Success(data = documentSnapshot.toPlan())
            } else {
                Resource.Failure(exception = Exception("No plan found with ID: $id"))
            }
        } catch (e: Exception) {
            Resource.Failure(exception = e)
        }
    }

    suspend fun getPlansByCategory(category: String): Resource<List<Plan>> {
        return try {
            val querySnapshot = plansCollection.get().await()
            val plans = querySnapshot.documents.mapNotNull { it.toPlan() }
                .filter { it.en.categories.containsKey(category) }
                .sortedByDescending { it.en.categories[category] }

            if (plans.isNotEmpty()) {
                val highestLevel = plans.first().en.categories[category]
                val highestLevelPlans = plans.filter { it.en.categories[category] == highestLevel }
                Resource.Success(data = highestLevelPlans)
            } else {
                Resource.Failure(exception = Exception("No plans found with category: $category"))
            }
        } catch (e: Exception) {
            Resource.Failure(exception = e)
        }
    }
}