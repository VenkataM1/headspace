package com.learning.headspace.repository

import com.google.gson.GsonBuilder
import com.learning.headspace.model.PicSum
import com.learning.headspace.repository.dataBase.PicSumDao
import com.learning.headspace.repository.networking.ApiInterface
import io.reactivex.Observable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class Repository(private val mDao: PicSumDao) {
    companion object Factory {
        private var gson = GsonBuilder().setLenient().create()
        fun create(): ApiInterface {
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl("https://picsum.photos")
                .build()
            return  retrofit.create(ApiInterface::class.java)
        }
    }

    fun loadPicSumData() : Observable<List<PicSum>> =  create().getPicSum()

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