package com.gaurav.learn.kotlin.coroutines.data.local

import com.gaurav.learn.kotlin.coroutines.data.local.entity.User

interface DatabaseHelper {

    suspend fun getUsers(): List<User>

    suspend fun insertAll(users: List<User>)

}