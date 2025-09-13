package com.gaurav.learn.kotlin.coroutines.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gaurav.learn.kotlin.coroutines.data.local.dao.UserDao
import com.gaurav.learn.kotlin.coroutines.data.local.entity.User

@Database(entities = [User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

}