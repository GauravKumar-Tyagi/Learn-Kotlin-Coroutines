package com.gaurav.learn.kotlin.coroutines.ui.suspendCancellableCoroutine

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.gaurav.learn.kotlin.coroutines.R
import com.gaurav.learn.kotlin.coroutines.data.api.ApiHelperImpl
import com.gaurav.learn.kotlin.coroutines.data.api.RetrofitBuilder
import com.gaurav.learn.kotlin.coroutines.data.local.DatabaseBuilder
import com.gaurav.learn.kotlin.coroutines.data.local.DatabaseHelperImpl
import com.gaurav.learn.kotlin.coroutines.ui.base.ViewModelFactory

/**
 * learn how to convert any Callback to Coroutines using suspendCancellableCoroutine
 */
class SuspendCancellableCoroutineActivity : AppCompatActivity() {

    companion object {
        const val TAG = "SuspendCancellableCoroutineActivity"
    }

    private lateinit var viewModel: SuspendCancellableCoroutineViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_suspend_cancellable_coroutine)
        setupViewModel()
        setupObserver()
    }

    fun setupObserver() {
        viewModel.getUiState().observe(this) {
            Log.d(TAG, "Data received: ${it.first}, ${it.second}")
            // Output: Data received: Data from source 1, Data from source 2
            // OR Output: Data received: Error, File downloading CANCELLED by User
            // OR Output: Data received: Some other ERROR in File Downloading :: ,  e.message ?: "Unknown error"
            Toast.makeText(this, "Data received: ${it.first}, ${it.second}", Toast.LENGTH_LONG).show()
        }
    }

    fun startDownloadTask1(view: View) {
        viewModel.startDownload(1)
    }

    fun startDownloadTask2(view: View) {
        viewModel.startDownload(2)
    }

    fun endDownloadTask1(view: View) {
        viewModel.cancelDownload(1)
    }

    fun endDownloadTask2(view: View) {
        viewModel.cancelDownload(2)
    }


    fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(
                ApiHelperImpl(RetrofitBuilder.apiService),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(applicationContext))
            )
        )[SuspendCancellableCoroutineViewModel::class.java]
    }


}