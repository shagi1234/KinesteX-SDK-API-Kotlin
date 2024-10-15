package com.kinestex.kinestexsdkkotlin.secure_api

import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.kinestex.kinestexsdkkotlin.secure_api.mapper.ConvertDocumentToExercise
import com.kinestex.kinestexsdkkotlin.secure_api.mapper.ConvertDocumentToPlan
import com.kinestex.kinestexsdkkotlin.secure_api.mapper.ConvertDocumentToWorkout
import com.kinestex.kinestexsdkkotlin.secure_api.repository.ExercisesRepository
import com.kinestex.kinestexsdkkotlin.secure_api.repository.PlansRepository
import com.kinestex.kinestexsdkkotlin.secure_api.repository.WorkoutsRepository
import com.kinestex.kinestexsdkkotlin.secure_api.models.Exercise
import com.kinestex.kinestexsdkkotlin.secure_api.models.Plan
import com.kinestex.kinestexsdkkotlin.secure_api.models.Workout
import com.kinestex.kinestexsdkkotlin.secure_api.models.Resource

/**
 *  Initializing example:
 * class MyApplication : Application() {
 *     override fun onCreate() {
 *         super.onCreate()
 *
 *         KinesteXSDKAPI.createAndInitialize()
 *     }
 * }

 *  Calling functions:
 *  lifecycleScope.launch {
 *      val result = KinesteXSDKAPI.getExerciseByTitle("Push-up")
 *      when (result) {
 *          is Resource.Success -> // Handle success
 *          is Resource.Loading -> // Handle loading state
 *          is Resource.Failure -> // Handle error
 *      }
 *  }
 */
object KinesteXSDKAPI {
    private lateinit var exercisesRepository: ExercisesRepository
    private lateinit var plansRepository: PlansRepository
    private lateinit var workoutsRepository: WorkoutsRepository
    private var isInitialized = false

    fun createAndInitialize(context: android.content.Context) {
        if (!isInitialized) {
            Log.e("KinesteXSDKAPI", "createAndInitialize")

            // Ensure Firebase is initialized
            if (FirebaseApp.getApps(context).isEmpty()) {
                FirebaseApp.initializeApp(context)
            }

            val convertDocumentToExercise = ConvertDocumentToExercise()
            val convertDocumentToPlan = ConvertDocumentToPlan()
            val convertDocumentToWorkout = ConvertDocumentToWorkout()

            // Use FirebaseFirestore.getInstance() after ensuring Firebase is initialized
            val db: FirebaseFirestore = FirebaseFirestore.getInstance()

            exercisesRepository = ExercisesRepository(convertDocumentToExercise = convertDocumentToExercise, db)
            plansRepository = PlansRepository(convertDocumentToPlan = convertDocumentToPlan, db)
            workoutsRepository = WorkoutsRepository(convertDocumentToWorkout = convertDocumentToWorkout, db)
            isInitialized = true
        }
    }

    private fun checkInitialization() {
        if (!isInitialized) {
            Log.e("KinesteXSDKAPI", "checkInitialization: notInitialized", )
        }
    }

    // Exercise-related functions
    suspend fun getExerciseByTitle(name: String): Resource<Exercise> {
        checkInitialization()
        return exercisesRepository.getExerciseByName(name)
    }

    suspend fun getExerciseById(id: String): Resource<Exercise> {
        checkInitialization()
        return exercisesRepository.getExerciseById(id)
    }

    // Plan-related functions
    suspend fun getPlanByTitle(name: String): Resource<Plan> {
        checkInitialization()
        return plansRepository.getPlanByName(name)
    }

    suspend fun getPlanById(id: String): Resource<Plan> {
        checkInitialization()
        return plansRepository.getPlanById(id)
    }

    suspend fun getPlansByCategory(category: String): Resource<List<Plan>> {
        checkInitialization()
        return plansRepository.getPlansByCategory(category)
    }

    // Workout-related functions
    suspend fun getWorkoutByTitle(title: String): Resource<Workout> {
        checkInitialization()
        return workoutsRepository.getWorkoutByTitle(title)
    }

    suspend fun getWorkoutById(id: String): Resource<Workout> {
        checkInitialization()
        return workoutsRepository.getWorkoutByID(id)
    }

    suspend fun getWorkoutsByCategory(category: String): Resource<List<Workout>> {
        checkInitialization()
        return workoutsRepository.getWorkoutsByCategory(category)
    }
}