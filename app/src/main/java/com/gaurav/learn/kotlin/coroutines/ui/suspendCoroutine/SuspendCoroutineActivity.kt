package com.gaurav.learn.kotlin.coroutines.ui.suspendCoroutine

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.gaurav.learn.kotlin.coroutines.data.api.ApiHelperImpl
import com.gaurav.learn.kotlin.coroutines.data.api.RetrofitBuilder
import com.gaurav.learn.kotlin.coroutines.data.local.DatabaseBuilder
import com.gaurav.learn.kotlin.coroutines.data.local.DatabaseHelperImpl
import com.gaurav.learn.kotlin.coroutines.ui.base.BaseActivity
import com.gaurav.learn.kotlin.coroutines.ui.base.ViewModelFactory

/**
 * learn how to convert any Callback to Coroutines using suspendCoroutine
 */
class SuspendCoroutineActivity : BaseActivity() {

    companion object {
        private const val TAG = "SuspendCoroutineActivity"
    }

    private lateinit var viewModel: SuspendCoroutineViewModel

    override fun setupObserver() {
        viewModel.getUiState().observe(this) {
            binding.progressBar.visibility = View.GONE
            Log.d(TAG, "Data received: ${it.first}, ${it.second}")
            // Output: Data received: Data from source 1, Data from source 2
            // OR Output: Data received: Error, Invalid request
            Toast.makeText(this, "Data received: ${it.first}, ${it.second}", Toast.LENGTH_LONG)
                .show()
        }
        viewModel.getUiStateFunctional().observe(this) {
            binding.progressBar.visibility = View.GONE
            Log.d(TAG, "Functional Data received: ${it.first}, ${it.second?.message}")
            // Output: Functional Data received: Data from source 1, null
            // OR Output: Functional Data received: null, Invalid request
            Toast.makeText(
                this,
                "Functional Data received: ${it.first}, ${it.second?.message}",
                Toast.LENGTH_LONG
            ).show()
        }
    }


    override fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(
                ApiHelperImpl(RetrofitBuilder.apiService),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(applicationContext))
            )
        )[SuspendCoroutineViewModel::class.java]
    }


}