package com.gaurav.learn.kotlin.coroutines.ui.errorhandling.exceptionhandler

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gaurav.learn.kotlin.coroutines.data.api.ApiHelper
import com.gaurav.learn.kotlin.coroutines.data.local.DatabaseHelper
import com.gaurav.learn.kotlin.coroutines.data.model.ApiUser
import com.gaurav.learn.kotlin.coroutines.ui.base.UiState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch


class ExceptionHandlerViewModel(
    private val apiHelper: ApiHelper,
    private val dbHelper: DatabaseHelper
) : ViewModel() {

    private val uiState = MutableLiveData<UiState<List<ApiUser>>>()

    private val exceptionHandler = CoroutineExceptionHandler { _, e ->
        uiState.postValue(UiState.Error("exception handler: $e"))
    }

    init {
        //fetchUsersWithHandler()
        fetchUsersWithTryCatch()
    }

    private fun fetchUsersWithHandler() {
        viewModelScope.launch(exceptionHandler) {
            uiState.postValue(UiState.Loading)
            val usersFromApi = apiHelper.getUsersWithError()
            uiState.postValue(UiState.Success(usersFromApi))
        }
    }

    private fun fetchUsersWithTryCatch() {
        viewModelScope.launch {
            uiState.postValue(UiState.Loading)
            try {
                val usersFromApi = apiHelper.getUsersWithError()
                uiState.postValue(UiState.Success(usersFromApi))
            } catch (e: Exception) {
                uiState.postValue(UiState.Error("Something Went Wrong"))
            }
        }
    }

    fun getUiState(): LiveData<UiState<List<ApiUser>>> {
        return uiState
    }

}