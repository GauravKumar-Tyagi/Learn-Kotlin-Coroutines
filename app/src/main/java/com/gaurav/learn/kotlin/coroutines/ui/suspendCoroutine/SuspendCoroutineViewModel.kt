package com.gaurav.learn.kotlin.coroutines.ui.suspendCoroutine

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gaurav.learn.kotlin.coroutines.data.api.ApiHelper
import com.gaurav.learn.kotlin.coroutines.data.local.DatabaseHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


class SuspendCoroutineViewModel(
    private val apiHelper: ApiHelper,
    private val dbHelper: DatabaseHelper
) : ViewModel() {

    private val uiStateFetchData = MutableLiveData<Pair<String, String>>()
    private val uiStateFetchFunctionalData = MutableLiveData<Pair<String?, Throwable?>>()

    fun getUiState(): LiveData<Pair<String, String>> {
        return uiStateFetchData
    }

    fun getUiStateFunctional(): LiveData<Pair<String?, Throwable?>> {
        return uiStateFetchFunctionalData
    }

    init {
        //fetchUsers(5)
        //fetchUsers(-5)
        //fetchFunctionalUsers(5)
        fetchFunctionalUsers(-5)
    }

    private fun fetchUsers(request: Int) {
        viewModelScope.launch {
            try {
                val result = fetchDataAsync(request)
                delay(3000)
                uiStateFetchData.postValue(result)
            } catch (e: Exception) {
                delay(3000)
                uiStateFetchData.postValue(Pair("Error", e.message ?: "Unknown error"))
                return@launch
            }
        }
    }

    private fun fetchFunctionalUsers(request: Int) {
        viewModelScope.launch {
            val result = fetchFunctionalDataAsync(request)
            delay(3000)
            uiStateFetchFunctionalData.postValue(result)
        }
    }


    private suspend fun fetchDataAsync(request: Int): Pair<String, String> {
        return suspendCoroutine { continuation ->
            Library.fetchData(request, object : DataCallback {
                override fun onSuccess(data1: String, data2: String) {
                    continuation.resume(Pair(data1, data2))
                }

                override fun onError(error: Throwable) {
                    continuation.resumeWithException(error)
                }

            })
        }
    }

    private suspend fun fetchFunctionalDataAsync(request: Int): Pair<String?, Throwable?> =
        suspendCoroutine { continuation ->
            Library.fetchFunctionalData(request) { response, error ->
                continuation.resume(Pair(response, error))
            }
        }


}