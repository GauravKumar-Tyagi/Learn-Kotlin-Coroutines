package com.gaurav.learn.kotlin.coroutines.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gaurav.learn.kotlin.coroutines.data.api.ApiHelper
import com.gaurav.learn.kotlin.coroutines.data.local.DatabaseHelper
import com.gaurav.learn.kotlin.coroutines.ui.errorhandling.exceptionhandler.ExceptionHandlerViewModel
import com.gaurav.learn.kotlin.coroutines.ui.errorhandling.supervisorscope.IgnoreErrorAndContinueViewModel
import com.gaurav.learn.kotlin.coroutines.ui.retrofit.parallel.ParallelNetworkCallsViewModel
import com.gaurav.learn.kotlin.coroutines.ui.retrofit.series.SeriesNetworkCallsViewModel
import com.gaurav.learn.kotlin.coroutines.ui.retrofit.single.SingleNetworkCallViewModel
import com.gaurav.learn.kotlin.coroutines.ui.room.RoomDBViewModel
import com.gaurav.learn.kotlin.coroutines.ui.timeout.TimeoutViewModel


class ViewModelFactory(private val apiHelper: ApiHelper, private val dbHelper: DatabaseHelper) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SingleNetworkCallViewModel::class.java)) {
            return SingleNetworkCallViewModel(apiHelper, dbHelper) as T
        }
        else if (modelClass.isAssignableFrom(SeriesNetworkCallsViewModel::class.java)) {
            return SeriesNetworkCallsViewModel(apiHelper, dbHelper) as T
        }
        else if (modelClass.isAssignableFrom(ParallelNetworkCallsViewModel::class.java)) {
            return ParallelNetworkCallsViewModel(apiHelper, dbHelper) as T
        }
        else if (modelClass.isAssignableFrom(RoomDBViewModel::class.java)) {
            return RoomDBViewModel(apiHelper, dbHelper) as T
        } else if (modelClass.isAssignableFrom(ExceptionHandlerViewModel::class.java)) {
            return ExceptionHandlerViewModel(apiHelper, dbHelper) as T
        } else if (modelClass.isAssignableFrom(IgnoreErrorAndContinueViewModel::class.java)) {
            return IgnoreErrorAndContinueViewModel(apiHelper, dbHelper) as T
        } else if (modelClass.isAssignableFrom(TimeoutViewModel::class.java)) {
            return TimeoutViewModel(apiHelper, dbHelper) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}