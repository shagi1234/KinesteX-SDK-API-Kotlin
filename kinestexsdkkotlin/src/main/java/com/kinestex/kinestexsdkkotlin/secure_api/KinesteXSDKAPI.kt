package com.kinestex.kinestexsdkkotlin.secure_api

import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.kinestex.kinestexsdkkotlin.secure_api.repository.ExercisesRepository
import com.kinestex.kinestexsdkkotlin.secure_api.repository.PlansRepository
import com.kinestex.kinestexsdkkotlin.secure_api.repository.WorkoutsRepository
import com.kinestex.kinestexsdkkotlin.secure_api.models.Exercise
import com.kinestex.kinestexsdkkotlin.secure_api.models.Plan
import com.kinestex.kinestexsdkkotlin.secure_api.models.Workout
import com.kinestex.kinestexsdkkotlin.secure_api.models.Resource


/**
 * Main API object for the KinesteX SDK.
 *
 * Initialization example:
 * ```
 * class MyApplication : Application() {
 *     override fun onCreate() {
 *         super.onCreate()
 *         KinesteXSDKAPI.createAndInitialize(this)
 *     }
 * }
 * ```
 *
 * Usage example:
 * ```
 * lifecycleScope.launch {
 *     val result = KinesteXSDKAPI.getExerciseByTitle("Push-up")
 *     when (result) {
 *         is Resource.Success -> // Handle success
 *         is Resource.Loading -> // Handle loading state
 *         is Resource.Failure -> // Handle error
 *     }
 * }
 * ```
 */
object KinesteXSDKAPI {
    private lateinit var exercisesRepository: ExercisesRepository
    private lateinit var plansRepository: PlansRepository
    private lateinit var workoutsRepository: WorkoutsRepository
    private var isInitialized = false

    /**
     * Initializes the SDK with the given context.
     * This method should be called before using any other SDK functions.
     *
     * @param context The application context
     */
    fun createAndInitialize(context: android.content.Context) {
        if (!isInitialized) {
            if (FirebaseApp.getApps(context).isEmpty()) {
                FirebaseApp.initializeApp(context)
            }
            val db: FirebaseFirestore = FirebaseFirestore.getInstance()
            exercisesRepository = ExercisesRepository(db)
            plansRepository = PlansRepository(db)
            workoutsRepository = WorkoutsRepository(db)
            isInitialized = true
        }
    }

    /**
     * Checks if the SDK has been initialized.
     * Logs an error if not initialized.
     */
    private fun checkInitialization() {
        if (!isInitialized) {
            Log.e("KinesteXSDKAPI", "SDK not initialized. Call createAndInitialize() first.")
        }
    }

    // Exercise-related functions

    /**
     * Fetches an exercise by its title.
     *
     * @param name The title of the exercise
     * @return Resource<Exercise> representing the result of the operation
     */
    suspend fun getExerciseByTitle(name: String): Resource<Exercise> {
        checkInitialization()
        return exercisesRepository.getExerciseByName(name)
    }

    /**
     * Fetches an exercise by its ID.
     *
     * @param id The ID of the exercise
     * @return Resource<Exercise> representing the result of the operation
     */
    suspend fun getExerciseById(id: String): Resource<Exercise> {
        checkInitialization()
        return exercisesRepository.getExerciseById(id)
    }

    // Plan-related functions

    /**
     * Fetches a plan by its title.
     *
     * @param name The title of the plan
     * @return Resource<Plan> representing the result of the operation
     */
    suspend fun getPlanByTitle(name: String): Resource<Plan> {
        checkInitialization()
        return plansRepository.getPlanByName(name)
    }

    /**
     * Fetches a plan by its ID.
     *
     * @param id The ID of the plan
     * @return Resource<Plan> representing the result of the operation
     */
    suspend fun getPlanById(id: String): Resource<Plan> {
        checkInitialization()
        return plansRepository.getPlanById(id)
    }

    /**
     * Fetches plans by category.
     *
     * @param category The category of plans to fetch
     * @return Resource<List<Plan>> representing the result of the operation
     */
    suspend fun getPlansByCategory(category: String): Resource<List<Plan>> {
        checkInitialization()
        return plansRepository.getPlansByCategory(category)
    }

    // Workout-related functions

    /**
     * Fetches a workout by its title.
     *
     * @param title The title of the workout
     * @return Resource<Workout> representing the result of the operation
     */
    suspend fun getWorkoutByTitle(title: String): Resource<Workout> {
        checkInitialization()
        return workoutsRepository.getWorkoutByTitle(title)
    }

    /**
     * Fetches a workout by its ID.
     *
     * @param id The ID of the workout
     * @return Resource<Workout> representing the result of the operation
     */
    suspend fun getWorkoutById(id: String): Resource<Workout> {
        checkInitialization()
        return workoutsRepository.getWorkoutByID(id)
    }

    /**
     * Fetches workouts by category.
     *
     * @param category The category of workouts to fetch
     * @return Resource<List<Workout>> representing the result of the operation
     */
    suspend fun getWorkoutsByCategory(category: String): Resource<List<Workout>> {
        checkInitialization()
        return workoutsRepository.getWorkoutsByCategory(category)
    }
}