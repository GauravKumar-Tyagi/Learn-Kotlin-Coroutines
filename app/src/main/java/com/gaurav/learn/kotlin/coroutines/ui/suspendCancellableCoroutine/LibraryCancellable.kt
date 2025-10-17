package com.gaurav.learn.kotlin.coroutines.ui.suspendCancellableCoroutine

import android.util.Log

object LibraryCancellable {

    private val callbacks = mutableMapOf<Int, DataCallback>() // [downloadId, callback]
    private val threads = mutableMapOf<Int, Thread>() // [downloadId, thread]
    private var nextId = 1

    fun fetchData(request: Int, callback: DataCallback): Int {
        val downloadId = nextId++
        callbacks[downloadId] = callback

        val thread = Thread {
            try {
                for (i in 1..30) {
                    Thread.sleep(500) // Simulate download chunk
                    Log.e(
                        SuspendCancellableCoroutineActivity.TAG,
                        " Downloading... $i/30 for request $request"
                    )
                    //if(!callbacks.containsKey(downloadId))
                        //return@Thread // Cancelled
                }
                if (callbacks.containsKey(downloadId)) {
                    callback.onSuccess("File downloaded Successfully", "Size: 10MB")
                }
            } catch (ex: InterruptedException) {
                callback.onError("File downloading CANCELLED :: ${ex.message}", ex) // Cancelled
            } catch (e: Exception) {
                if (callbacks.containsKey(downloadId)) {
                    callback.onError("Some other ERROR in File Downloading :: ${e.message}", e)
                }
            } finally {
                callbacks.remove(downloadId)
                threads.remove(downloadId)
            }
        }
        threads[downloadId] = thread
        thread.start()
        return downloadId
    }

    fun cancel(downloadId: Int) {
        Log.e(
            SuspendCancellableCoroutineActivity.TAG,
            " Cancelling the downloading request $downloadId"
        )
        callbacks.remove(downloadId)
        threads[downloadId]?.interrupt()
        threads.remove(downloadId)
    }

}

interface DataCallback {

    fun onSuccess(data1: String, data2: String)
    fun onError(errorMessage: String, error: Throwable)

}




