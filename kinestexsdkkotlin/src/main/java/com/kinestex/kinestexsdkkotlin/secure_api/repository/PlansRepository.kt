package com.kinestex.kinestexsdkkotlin.secure_api.repository

import com.kinestex.kinestexsdkkotlin.secure_api.models.Plan
import com.kinestex.kinestexsdkkotlin.secure_api.models.Resource
import com.kinestex.kinestexsdkkotlin.secure_api.utils.ReferenceKeys
import com.google.firebase.firestore.FirebaseFirestore
import com.kinestex.kinestexsdkkotlin.secure_api.mapper.toPlan
import kotlinx.coroutines.tasks.await

/**
 * Repository class for handling Plan-related operations with Firestore.
 *
 * @param db FirebaseFirestore instance for database operations
 */
class PlansRepository(db: FirebaseFirestore) {
    // Reference to the plans collection in Firestore
    private val plansCollection = db.collection(ReferenceKeys.PLANS_COLLECTION)

    /**
     * Fetches a plan by its name from the plans collection.
     *
     * @param name The name of the plan to fetch
     * @return Resource<Plan> representing the result of the operation
     */
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

    /**
     * Fetches a plan by its ID from the plans collection.
     *
     * @param id The ID of the plan to fetch
     * @return Resource<Plan> representing the result of the operation
     */
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

    /**
     * Fetches plans by category, returning only the plans with the highest level in that category.
     *
     * @param category The category to filter plans by
     * @return Resource<List<Plan>> representing the result of the operation
     */
    suspend fun getPlansByCategory(category: String): Resource<List<Plan>> {
        return try {
            val querySnapshot = plansCollection.get().await()
            val plans = querySnapshot.documents
                .mapNotNull { it.toPlan() }
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