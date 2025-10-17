package com.gaurav.learn.kotlin.coroutines.ui.suspendCoroutine

object Library {


    fun fetchData(request: Int, callback: DataCallback) {
        // Simulate network call or long-running operation
        try {
            if (request < 0)
                throw IllegalArgumentException("Invalid request")
            val data1 = "Data from source 1"
            val data2 = "Data from source 2"
            callback.onSuccess(data1, data2)
        } catch (e: Exception) {
            callback.onError(e)
        }
    }


    fun fetchFunctionalData(request: Int, callback: DataFunctionalCallback) {
        var response: String? = null
        var error: Throwable? = null
        // Simulate network call or long-running operation
        try {
            if (request < 0)
                throw IllegalArgumentException("Invalid request")
            response = "Data from source 1"
        } catch (e: Exception) {
            error = e
        }
        callback.onSuccess(response, error)
    }


}

interface DataCallback {

    fun onSuccess(data1: String, data2: String)
    fun onError(error: Throwable)

}


fun interface DataFunctionalCallback {

    fun onSuccess(response: String?, error: Throwable?)

}