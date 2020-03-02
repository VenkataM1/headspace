package com.learning.headspace.repository.dataBase

import com.learning.headspace.model.PicSum
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class AppRepository(private  val mDao:PicSumDao) {

    suspend fun getAllPicSums(): List<PicSum> {
        return GlobalScope.async(Dispatchers.IO) {
            mDao.getAll()
        }.await()
    }

    suspend fun insertAllPicSums(picSums: List<PicSum> ) {
        return GlobalScope.async(Dispatchers.IO) {
            mDao.insertAll(picSums)
        }.await()
    }
}