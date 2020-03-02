package com.learning.headspace.repository.dataBase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.learning.headspace.model.PicSum

@Dao
interface PicSumDao {
    @Query("SELECT * FROM picsum")
    fun getAll(): List<PicSum>

    @Insert
    suspend fun insertAll(picSum: List<PicSum>)
}