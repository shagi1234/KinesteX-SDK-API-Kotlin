package com.kinestex.kinestexsdkkotlin.secure_api.repository

import com.kinestex.kinestexsdkkotlin.secure_api.models.Plan
import com.kinestex.kinestexsdkkotlin.secure_api.models.Resource
import com.kinestex.kinestexsdkkotlin.secure_api.utils.ReferenceKeys

/*
 * Created by shagi on 26.03.2024 01:35
 */

import com.google.firebase.firestore.FirebaseFirestore
import com.kinestex.kinestexsdkkotlin.secure_api.mapper.ConvertDocumentToPlan
import kotlinx.coroutines.tasks.await

class PlansRepository(
    private val convertDocumentToPlan: ConvertDocumentToPlan
) {
    private val plansCollection = ReferenceKeys.db.collection(ReferenceKeys.PLANS_COLLECTION)

    suspend fun getPlanByName(name: String, isEnglish: Boolean = true): Resource<Plan> {
        return try {
            val field = if (isEnglish) "en.title" else "title"
            val querySnapshot = plansCollection.whereEqualTo(field, name).get().await()
            if (!querySnapshot.isEmpty) {
                val plan = convertDocumentToPlan.toPlan(querySnapshot.documents.first())
                Resource.Success(data = plan)
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
                val plan = convertDocumentToPlan.toPlan(documentSnapshot)
                Resource.Success(data = plan)
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
            val plans = querySnapshot.documents.mapNotNull { document ->
                val plan = convertDocumentToPlan.toPlan(document)
                plan.takeIf { it.categories.containsKey(category) }
            }.sortedByDescending { it.categories[category] }

            if (plans.isNotEmpty()) {
                val highestLevel = plans.first().categories[category]
                val highestLevelPlans = plans.filter { it.categories[category] == highestLevel }
                Resource.Success(data = highestLevelPlans)
            } else {
                Resource.Failure(exception = Exception("No plans found with category: $category"))
            }
        } catch (e: Exception) {
            Resource.Failure(exception = e)
        }
    }
}