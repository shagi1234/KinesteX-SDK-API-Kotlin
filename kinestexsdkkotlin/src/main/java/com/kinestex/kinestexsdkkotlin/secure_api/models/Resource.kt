package com.kinestex.kinestexsdkkotlin.secure_api.models


/**
 * Represents a generic resource wrapper for API responses.
 *
 * @param T The type of data wrapped by this resource
 * @property data The data payload of the resource
 * @property exception Any exception that occurred during the operation
 */
sealed class Resource<T>(
    var data: T? = null,
    var exception: Exception? = null
) {
    /** Represents a successful operation with data */
    class Success<T>(data: T? = null) : Resource<T>(data = data)

    /** Represents an ongoing operation */
    class Loading<T> : Resource<T>()

    /** Represents a failed operation with an exception */
    class Failure<T>(exception: Exception?) : Resource<T>(exception = exception)
}