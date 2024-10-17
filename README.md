# [Precise Motion Tracking and Analysis SDK](https://kinestex.com)
## Stay Ahead with KinesteX AI Motion Tracking.
### Easily transform your platform with our SDK: white-labeled workouts with precise motion tracking and real-time feedback tailored for accuracy and engagement

[Video demonstration]

## Overview

Welcome to the documentation for the KinesteX SDK for Kotlin. This SDK allows you to integrate AI-powered fitness and physio workouts into your app with ease. The KinesteX SDK offers a variety of integration options, detailed user feedback, and customizable features to create an immersive fitness experience.

## Configuration

### Permissions

Add the following permissions to your `AndroidManifest.xml`:

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.CAMERA" />
```

### Project Setup

Add the JitPack repository to your project's `settings.gradle` or `build.gradle` (if `settings.gradle` is not your primary dependency resolution file):

```gradle
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
```

Add the KinesteX SDK dependency in your app's `build.gradle`:

```gradle
implementation("com.github.KinesteX:KinesteXSDKKotlin:1.1.2")
```

## Usage

### Initial Setup

1. **Prerequisites**: Ensure you've added the necessary permissions in `AndroidManifest.xml`.

2. **Launching the view**: To display KinesteX, we will be using WebView. To launch Complete UX call `createMainView` in KinesteXSDK:

```kotlin
private var kinesteXWebView: WebView? = null

@SuppressLint("SetJavaScriptEnabled", "MissingInflatedId")
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
   
  // OPTIONAL: Custom Parameters
   val data = mutableMapOf<String, Any>()
   data["isHideHeaderMain"] = false // should display header in main screen

    kinesteXWebView = KinesteXSDK.createMainView(
                 this,
                 apiKey,
                 company,
                 userId,
                 getPlanCategory(subOption),
                 null,
                 customParams = data, // example of using custom parameters. CAN BE NULL
                 viewModel.isLoading,
                 ::handleWebViewMessage
             )
}
```

3. **Handling the data**: Use a ViewModel to handle changes:

```kotlin
class ContentViewModel : ViewModel() {
    val showWebView: MutableStateFlow<WebViewState> = MutableStateFlow(WebViewState.LOADING) // state of the webview: LOADING, ERROR, SUCCESS

    var selectedOptionPosition: MutableStateFlow<Int> = MutableStateFlow(0) // for the selected integration option (not necessary unless combining our solutions)
    val isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)// loading state of the webview
    // CAMERA COMPONENT SPECIFIC
    val reps: MutableStateFlow<Int> = MutableStateFlow(0) // for reps IF using our camera component
    val mistake: MutableStateFlow<String> = MutableStateFlow("") // for mistakes IF using our camera component
}
```

### Creating Views

The KinesteX SDK provides multiple methods to create different views:

- **Main View (Complete User Experience)**:

```kotlin
kinesteXWebView = KinesteXSDK.createMainView(
                this,
                apiKey,
                company,
                userId,
                PlanCategory.Cardio, // selected to plan category 
                null, // user details
                customParams = data, // example of using custom parameters
                viewModel.isLoading,
                ::handleWebViewMessage // callback function to handle responses
)
```

- **Plan View (Stand-alone workout plan page)**:

```kotlin
kinesteXWebView = KinesteXSDK.createPlanView(
                this,
                apiKey,
                company,
                userId,
                "Circuit Training", // name of the workout plan
                null,
                null, // custom parameters is null
                viewModel.isLoading,
                ::handleWebViewMessage
)
```

- **Workout View (Individual workout page)**:

```kotlin
kinesteXWebView = KinesteXSDK.createWorkoutView(
                this,
                apiKey,
                company,
                userId,
                "Fitness Lite", // name of the workout
                null, 
                isLoading = viewModel.isLoading,
                onMessageReceived = ::handleWebViewMessage
)
```

- **Challenge View**:

```kotlin
kinesteXWebView = KinesteXSDK.createChallengeView(
                this,
                apiKey,
                company,
                userId,
                "Squats", // name of the exercise
                100, // countdown of the challenge
                null, 
                customParams = null,
                viewModel.isLoading,
                ::handleWebViewMessage
)
```

- **Camera Component (Just camera + our motion analysis and feedback)**:

```kotlin
kinesteXWebView = KinesteXSDK.createCameraComponent(
        context = context,
        apiKey = apiKey,
        companyName = company,
        userId = userId,
        currentExercise = "Squats", // current exercise name
        exercises = listOf("Squats","Jumping Jack"), // exercises that user is expected to do
        user = null,
        isLoading = viewModel.isLoading,
        onMessageReceived = ::handleWebViewMessage
)
```

### Handling Messages

The SDK sends various messages through the WebView. Implement a callback to handle these messages:

```kotlin
private fun handleWebViewMessage(message: WebViewMessage) {
when (message) {
    is WebViewMessage.ExitKinestex -> lifecycleScope.launch {
        viewModel.showWebView.emit(
            WebViewState.ERROR
        )
    }

    // FOR CAMERA SPECIFIC INTEGRATION GET NUMBER OF REPS IN REAL-TIME AND MISTAKES
    is WebViewMessage.Reps -> {
        (message.data["value"] as? Int)?.let { viewModel.setReps(it) }
    }

    is WebViewMessage.Mistake -> {
        (message.data["value"] as? String)?.let {
            viewModel.setMistake(
                it
            )
        }
    }

    else -> {
        // handle all other messages
        Log.d("Message received", message.toString())
    }
}
}
```

### Updating the Current Exercise For Camera Component

Use the following method to update the current exercise in the camera component:

```kotlin
KinesteXSDK.updateCurrentExercise("Jumping Jack") // this exercise has to be from the list of exercises we are tracking
```

## API Functions

The KinesteXSDKAPI object provides a set of functions for retrieving exercises, plans, and workouts. Before using any of these functions, make sure to initialize the SDK as described in the Configuration section.

### Initialization

Before using any of the API functions, you need to initialize the SDK. This should typically be done in your Application class:

```kotlin
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        KinesteXSDKAPI.createAndInitialize(this)
    }
}
```

### Exercise-related Functions

#### getExerciseByTitle

Retrieves an exercise by its title.

```kotlin
suspend fun getExerciseByTitle(name: String): Resource<Exercise>
```

- Parameters:
    - `name`: String - The title of the exercise to retrieve.
- Returns: A `Resource<Exercise>` object.

#### getExerciseById

Retrieves an exercise by its ID.

```kotlin
suspend fun getExerciseById(id: String): Resource<Exercise>
```

- Parameters:
    - `id`: String - The ID of the exercise to retrieve.
- Returns: A `Resource<Exercise>` object.

### Plan-related Functions

#### getPlanByTitle

Retrieves a plan by its title.

```kotlin
suspend fun getPlanByTitle(name: String): Resource<Plan>
```

- Parameters:
    - `name`: String - The title of the plan to retrieve.
- Returns: A `Resource<Plan>` object.

#### getPlanById

Retrieves a plan by its ID.

```kotlin
suspend fun getPlanById(id: String): Resource<Plan>
```

- Parameters:
    - `id`: String - The ID of the plan to retrieve.
- Returns: A `Resource<Plan>` object.

#### getPlansByCategory

Retrieves a list of plans by category.

```kotlin
suspend fun getPlansByCategory(category: String): Resource<List<Plan>>
```

- Parameters:
    - `category`: String - The category of plans to retrieve.
- Returns: A `Resource<List<Plan>>` object.

### Workout-related Functions

#### getWorkoutByTitle

Retrieves a workout by its title.

```kotlin
suspend fun getWorkoutByTitle(title: String): Resource<Workout>
```

- Parameters:
    - `title`: String - The title of the workout to retrieve.
- Returns: A `Resource<Workout>` object.

#### getWorkoutById

Retrieves a workout by its ID.

```kotlin
suspend fun getWorkoutById(id: String): Resource<Workout>
```

- Parameters:
    - `id`: String - The ID of the workout to retrieve.
- Returns: A `Resource<Workout>` object.

#### getWorkoutsByCategory

Retrieves a list of workouts by category.

```kotlin
suspend fun getWorkoutsByCategory(category: String): Resource<List<Workout>>
```

- Parameters:
    - `category`: String - The category of workouts to retrieve.
- Returns: A `Resource<List<Workout>>` object.

### Usage Example

Here's an example of how to use the KinesteXSDKAPI in a coroutine:

```kotlin
lifecycleScope.launch {
    val result = KinesteXSDKAPI.getExerciseByTitle("Push-up")
    when (result) {
        is Resource.Success -> {
            // Handle success
            val exercise = result.data
            // Use the exercise data
        }
        is Resource.Loading -> {
            // Handle loading state
        }
        is Resource.Failure -> {
            // Handle error
            val error = result.exception
            // Process the error
        }
    }
}
```

Note: All API functions are suspend functions and should be called from within a coroutine.

### Error Handling

The API uses a `Resource` sealed class to wrap the results. This allows for easy handling of success, loading, and error states. Always check the type of the returned `Resource` object to handle different states appropriately.

### Thread Safety

The KinesteXSDKAPI object is designed to be thread-safe. However, make sure to call the API functions from appropriate coroutine scopes to avoid blocking the main thread.

## Data Points

[... existing content about data points ...]

## License

The KinesteX SDK is licensed under the Apache License, Version 2.0. See the LICENSE file for more details.