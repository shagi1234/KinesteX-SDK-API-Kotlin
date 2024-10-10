package com.kinestex.kinestexsdkkotlin.secure_api

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

    private fun createAndInitialize() {
        val convertDocumentToExercise = ConvertDocumentToExercise()
        val convertDocumentToPlan = ConvertDocumentToPlan()
        val convertDocumentToWorkout = ConvertDocumentToWorkout()

        exercisesRepository = ExercisesRepository(convertDocumentToExercise = convertDocumentToExercise)
        plansRepository = PlansRepository(convertDocumentToPlan = convertDocumentToPlan)
        workoutsRepository = WorkoutsRepository(convertDocumentToWorkout = convertDocumentToWorkout)

        isInitialized = true
    }

    private fun checkInitialization() {
        if (!isInitialized) {
            createAndInitialize()
        }
    }

    // Exercise-related functions
    suspend fun getExerciseByTitle(name: String, isEnglish: Boolean = true): Resource<Exercise> {
        checkInitialization()
        return exercisesRepository.getExerciseByName(name, isEnglish)
    }

    suspend fun getExerciseById(id: String): Resource<Exercise> {
        checkInitialization()
        return exercisesRepository.getExerciseById(id)
    }

    // Plan-related functions
    suspend fun getPlanByTitle(name: String, isEnglish: Boolean = true): Resource<Plan> {
        checkInitialization()
        return plansRepository.getPlanByName(name, isEnglish)
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