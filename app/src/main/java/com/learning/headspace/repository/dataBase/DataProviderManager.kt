package com.learning.headspace.repository.dataBase

import android.content.Context
import androidx.room.Room

object DataProviderManager {
    fun getDb(context: Context): AppDatabase  =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java, "picsubdb"
        ).build()
}