package com.learning.headspace.repository.dataBase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.learning.headspace.model.PicSum

@Database(entities = arrayOf(PicSum::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun picSumDao() : PicSumDao
}