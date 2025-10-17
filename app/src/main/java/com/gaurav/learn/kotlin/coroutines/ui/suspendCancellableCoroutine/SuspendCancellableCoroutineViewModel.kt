package com.gaurav.learn.kotlin.coroutines.ui.suspendCancellableCoroutine

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gaurav.learn.kotlin.coroutines.data.api.ApiHelper
import com.gaurav.learn.kotlin.coroutines.data.local.DatabaseHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


class SuspendCancellableCoroutineViewModel(
    private val apiHelper: ApiHelper,
    private val dbHelper: DatabaseHelper
) : ViewModel() {

    private val uiStateFetchData = MutableLiveData<Pair<String, String>>()

    private var currentDownloadId: MutableMap<Int, Int> = mutableMapOf() // [request, downloadId]

    fun getUiState(): LiveData<Pair<String, String>> {
        return uiStateFetchData
    }

    fun startDownload(request: Int) {
        viewModelScope.launch {
            try {
                val result = fetchDataAsync(request)
                delay(3000)
                uiStateFetchData.postValue(result)
            } catch (e: InterruptedException) {
                delay(3000)
                // Cancelled
                uiStateFetchData.postValue(Pair("Error", "File downloading CANCELLED by User"))
            } catch (e: Exception) {
                delay(3000)
                // Some Other Issue
                uiStateFetchData.postValue(
                    Pair(
                        "Some other ERROR in File Downloading :: ",
                        e.message ?: "Unknown error"
                    )
                )
            }
        }
    }


    private suspend fun fetchDataAsync(request: Int): Pair<String, String> {
        return suspendCancellableCoroutine { continuation ->

            val downloadId = LibraryCancellable.fetchData(request, object : DataCallback {
                override fun onSuccess(data1: String, data2: String) {
                    continuation.resume(Pair(data1, data2))
                }

                override fun onError(errorMessage: String, error: Throwable) {
                    continuation.resumeWithException(error)
                }
            })

            currentDownloadId[request] = downloadId

            continuation.invokeOnCancellation {
                LibraryCancellable.cancel(downloadId)
            }

        }
    }

    fun cancelDownload(request: Int) {
        if (currentDownloadId.contains(request)) {
            currentDownloadId[request]?.let { downloadId ->
                LibraryCancellable.cancel(downloadId)
            }
            currentDownloadId.remove(request)
        }

    }


}