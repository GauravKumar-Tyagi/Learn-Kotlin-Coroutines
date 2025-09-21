package com.gaurav.learn.kotlin.coroutines.ui.errorhandling.supervisorscope

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gaurav.learn.kotlin.coroutines.data.api.ApiHelper
import com.gaurav.learn.kotlin.coroutines.data.local.DatabaseHelper
import com.gaurav.learn.kotlin.coroutines.data.model.ApiUser
import com.gaurav.learn.kotlin.coroutines.ui.base.UiState
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

class IgnoreErrorAndContinueViewModel(
    private val apiHelper: ApiHelper,
    private val dbHelper: DatabaseHelper
) : ViewModel() {

    private val uiState = MutableLiveData<UiState<List<ApiUser>>>()

    init {
        fetchUsersWithSupervisorScope()
        //fetchUsersWithSupervisorJob()
    }

    private fun fetchUsersWithSupervisorScope() {
        viewModelScope.launch {
            uiState.postValue(UiState.Loading)
            // supervisorScope is needed, so that we can ignore error and continue
            // here, more than two child jobs are running in parallel under a supervisor, one child job gets failed, we can continue with other.
            // Official Doc: supervisorScope is nothing but to creates a CoroutineScope with SupervisorJob
            supervisorScope {
                val usersFromApiDeferred = async { apiHelper.getUsersWithError() }
                val moreUsersFromApiDeferred = async { apiHelper.getMoreUsers() }

                val usersFromApi = try {
                    usersFromApiDeferred.await()
                } catch (e: Exception) {
                    emptyList()
                }

                val moreUsersFromApi = try {
                    moreUsersFromApiDeferred.await()
                } catch (e: Exception) {
                    emptyList()
                }

                val allUsersFromApi = mutableListOf<ApiUser>()
                allUsersFromApi.addAll(usersFromApi)
                allUsersFromApi.addAll(moreUsersFromApi)

                uiState.postValue(UiState.Success(allUsersFromApi))
            }
        }
    }

    private fun fetchUsersWithSupervisorJob() {
        viewModelScope.launch {
            uiState.postValue(UiState.Loading)

            val usersFromApiDeferred = viewModelScope.async { apiHelper.getUsersWithError() }
            val moreUsersFromApiDeferred = viewModelScope.async { apiHelper.getMoreUsers() }

            val usersFromApi = try {
                usersFromApiDeferred.await()
            } catch (e: Exception) {
                emptyList()
            }

            val moreUsersFromApi = try {
                moreUsersFromApiDeferred.await()
            } catch (e: Exception) {
                emptyList()
            }

            val allUsersFromApi = mutableListOf<ApiUser>()
            allUsersFromApi.addAll(usersFromApi)
            allUsersFromApi.addAll(moreUsersFromApi)

            uiState.postValue(UiState.Success(allUsersFromApi))


        }
    }

    fun getUiState(): LiveData<UiState<List<ApiUser>>> {
        return uiState
    }

}