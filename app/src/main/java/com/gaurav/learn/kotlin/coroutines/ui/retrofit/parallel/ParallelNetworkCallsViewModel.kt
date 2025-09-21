package com.gaurav.learn.kotlin.coroutines.ui.retrofit.parallel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gaurav.learn.kotlin.coroutines.data.api.ApiHelper
import com.gaurav.learn.kotlin.coroutines.data.local.DatabaseHelper
import com.gaurav.learn.kotlin.coroutines.data.model.ApiUser
import com.gaurav.learn.kotlin.coroutines.ui.base.UiState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class ParallelNetworkCallsViewModel(
    private val apiHelper: ApiHelper,
    private val dbHelper: DatabaseHelper
) : ViewModel() {

    private val uiState = MutableLiveData<UiState<List<ApiUser>>>()

    private val exceptionHandler = CoroutineExceptionHandler { _, e ->
        uiState.postValue(UiState.Error("exception handler: $e"))
    }

    init {
        fetchUsersWithHandlers()
        //fetchUsersWithTryCatch()
    }

    private fun fetchUsersWithHandlers() {
        viewModelScope.launch(exceptionHandler) {
            uiState.postValue(UiState.Loading)

            val usersFromApiDeferred = async { apiHelper.getUsers() }
            val moreUsersFromApiDeferred = async { apiHelper.getMoreUsers() }

            val usersFromApi = usersFromApiDeferred.await()
            val moreUsersFromApi = moreUsersFromApiDeferred.await()

            val allUsersFromApi = mutableListOf<ApiUser>()
            allUsersFromApi.addAll(usersFromApi)
            allUsersFromApi.addAll(moreUsersFromApi)

            uiState.postValue(UiState.Success(allUsersFromApi))
        }
    }


    private fun fetchUsersWithTryCatch() {
        viewModelScope.launch {
            uiState.postValue(UiState.Loading)
            try {
                // do long running tasks, for example network calls
                val usersFromApiDeferred = async { apiHelper.getUsers() }
                val moreUsersFromApiDeferred = async { apiHelper.getMoreUsers() }

                val usersFromApi = usersFromApiDeferred.await()
                val moreUsersFromApi = moreUsersFromApiDeferred.await()

                val allUsersFromApi = mutableListOf<ApiUser>()
                allUsersFromApi.addAll(usersFromApi)
                allUsersFromApi.addAll(moreUsersFromApi)

                uiState.postValue(UiState.Success(allUsersFromApi))

            } catch (e: Exception) {
                uiState.postValue(UiState.Error("Something Went Wrong: ${e.message}"))
            }
        }
    }

    fun getUiState(): LiveData<UiState<List<ApiUser>>> {
        return uiState
    }

}