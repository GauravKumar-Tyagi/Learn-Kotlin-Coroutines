package com.gaurav.learn.kotlin.coroutines.ui.timeout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gaurav.learn.kotlin.coroutines.data.api.ApiHelper
import com.gaurav.learn.kotlin.coroutines.data.local.DatabaseHelper
import com.gaurav.learn.kotlin.coroutines.data.model.ApiUser
import com.gaurav.learn.kotlin.coroutines.ui.base.UiState
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

class TimeoutViewModel(
    private val apiHelper: ApiHelper,
    private val dbHelper: DatabaseHelper
) : ViewModel() {

    private val uiState = MutableLiveData<UiState<List<ApiUser>>>()

    init {
        fetchUsers()
    }

    private fun fetchUsers() {

        viewModelScope.launch {
            uiState.postValue(UiState.Loading)
            try {
                withTimeout(1400) {
                    val usersFromApi = apiHelper.getUsers()
                    uiState.postValue(UiState.Success(usersFromApi))
                }
            } catch (e: TimeoutCancellationException) {
                uiState.postValue(UiState.Error("TimeoutCancellationException"))
            } catch (e: Exception) {
                uiState.postValue(UiState.Error("Something Went Wrong"))
            }
        }

    }

    fun getUiState(): LiveData<UiState<List<ApiUser>>> {
        return uiState
    }

}