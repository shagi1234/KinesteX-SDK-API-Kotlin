package com.kinestex.kinestexsdkkotlin.secure_api.models


sealed class Resource<T>(
    var data: T? = null,
    var exception: Exception? = null
) {
    class Success<T>(data: T? = null) : Resource<T>(data = data)
    class Loading<T> : Resource<T>()

    class Failure<T>(exception: Exception?) :
        Resource<T>(exception = exception)
}