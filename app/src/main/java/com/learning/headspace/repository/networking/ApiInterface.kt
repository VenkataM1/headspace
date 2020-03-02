package com.learning.headspace.repository.networking

import com.learning.headspace.model.PicSum
import io.reactivex.Observable
import retrofit2.http.GET


interface ApiInterface {
    @GET("v2/list")
    fun getPicSum(): Observable<List<PicSum>>
}