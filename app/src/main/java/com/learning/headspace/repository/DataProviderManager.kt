package com.learning.headspace.repository

import android.content.Context
import androidx.room.Room
import com.learning.headspace.repository.dataBase.AppDatabase

object DataProviderManager {
    fun getDb(context: Context): AppDatabase  =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java, "picsubdb"
        ).build()
}