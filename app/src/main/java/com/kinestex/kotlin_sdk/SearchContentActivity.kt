package com.kinestex.kotlin_sdk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.kinestex.kinestexsdkkotlin.secure_api.KinesteXSDKAPI
import com.kinestex.kinestexsdkkotlin.secure_api.models.Resource
import com.kinestex.kotlin_sdk.data.ContentType
import com.kinestex.kotlin_sdk_secure_api.R
import com.kinestex.kotlin_sdk_secure_api.databinding.ActivitySearchContentBinding
import kotlinx.coroutines.launch

class SearchContentActivity : AppCompatActivity() {
    private var contentType: String? = null
    private var searchText: String? = null
    private lateinit var binding: ActivitySearchContentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchContentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve the passed arguments from the intent
        contentType = intent.getStringExtra("content_type")
        searchText = intent.getStringExtra("search_text")

        Log.d("SearchContentActivity", "Content Type: $contentType, Search Text: $searchText")

        fetchData()

    }

    private fun fetchData() {
        contentType?.let {
            when (it) {
                ContentType.EXERCISE.name -> {
                    fetchExercise()
                }

                ContentType.WORKOUT.name -> {
                    fetchWorkout()
                }

                ContentType.PLAN.name -> {
                    fetchPlan()
                }
            }
        }

    }

    private fun fetchPlan() {
        lifecycleScope.launch {
            searchText?.let {title ->
                when (val result = KinesteXSDKAPI.getPlanByTitle(title)) {
                    is Resource.Success -> {
                        val prettyJson = GsonBuilder().setPrettyPrinting().create().toJson(result.data)
                        binding.content.text = prettyJson
                    }
                    is Resource.Loading -> {
                        binding.content.text = "Loading ..."
                    }
                    is Resource.Failure -> {
                        binding.content.text = result.exception.toString()
                    }
                }
            }

        }
    }

    private fun fetchWorkout() {
        lifecycleScope.launch {
            searchText?.let {title ->
                when (val result = KinesteXSDKAPI.getWorkoutByTitle(title)) {
                    is Resource.Success -> {
                        val prettyJson = GsonBuilder().setPrettyPrinting().create().toJson(result.data)
                        binding.content.text = prettyJson
                    }
                    is Resource.Loading -> {
                        binding.content.text = "Loading ..."
                    }
                    is Resource.Failure -> {
                        binding.content.text = result.exception.toString()
                    }
                }
            }

        }
    }

    private fun fetchExercise() {
        lifecycleScope.launch {
            searchText?.let {title ->
                when (val result = KinesteXSDKAPI.getExerciseByTitle(title)) {
                    is Resource.Success -> {
                        val prettyJson = GsonBuilder().setPrettyPrinting().create().toJson(result.data)
                        binding.content.text = prettyJson
                    }
                    is Resource.Loading -> {
                        binding.content.text = "Loading ..."
                    }
                    is Resource.Failure -> {
                        binding.content.text = result.exception.toString()
                    }
                }
            }

        }
    }
}