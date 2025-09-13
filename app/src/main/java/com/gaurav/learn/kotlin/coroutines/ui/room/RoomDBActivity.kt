package com.gaurav.learn.kotlin.coroutines.ui.room

import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.gaurav.learn.kotlin.coroutines.data.api.ApiHelperImpl
import com.gaurav.learn.kotlin.coroutines.data.api.RetrofitBuilder
import com.gaurav.learn.kotlin.coroutines.data.local.DatabaseBuilder
import com.gaurav.learn.kotlin.coroutines.data.local.DatabaseHelperImpl
import com.gaurav.learn.kotlin.coroutines.ui.base.BaseActivity
import com.gaurav.learn.kotlin.coroutines.ui.base.UiState
import com.gaurav.learn.kotlin.coroutines.ui.base.ViewModelFactory


class RoomDBActivity : BaseActivity() {

    private lateinit var viewModel: RoomDBViewModel


    override fun setupObserver() {
        viewModel.getUiState().observe(this) {
            when (it) {
                is UiState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    renderDBList(it.data)
                    binding.recyclerView.visibility = View.VISIBLE
                }
                is UiState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.recyclerView.visibility = View.GONE
                }
                is UiState.Error -> {
                    //Handle Error
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }



    override fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(
                ApiHelperImpl(RetrofitBuilder.apiService),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(applicationContext))
            )
        )[RoomDBViewModel::class.java]
    }
}
