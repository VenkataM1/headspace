package com.learning.headspace.repository.networking

import com.google.gson.GsonBuilder
import com.learning.headspace.model.PicSum
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class Repository {
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

}