package com.learning.headspace.viewmodel

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.learning.headspace.repository.networking.Repository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import androidx.lifecycle.MutableLiveData
import com.learning.headspace.model.PicSum
import com.learning.headspace.R
import com.learning.headspace.repository.dataBase.AppRepository
import com.learning.headspace.repository.dataBase.PicSumDao
import com.learning.headspace.repository.DataProviderManager
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class PicSumViewModel(): ViewModel() {

    private val disposables = CompositeDisposable()
    private val responseLiveData = MutableLiveData<List<PicSum>>()
    lateinit var picSumDao:PicSumDao
    private val  statusMessage = MutableLiveData<String>()

    fun fetchPicSumData(context:Context) {
         picSumDao = DataProviderManager.getDb(context).picSumDao()
         GlobalScope.launch(Dispatchers.Main) {
             val picSums =  AppRepository(picSumDao).getAllPicSums()
             if (picSums.size == 0 ) {
                 getPicSumFromApi(context)
             }else {
                 responseLiveData.value = picSums
             }
         }
    }
    fun getPicSumFromApi(context:Context) {
       if (isOnline(context)) {
           disposables.add(
               Repository().loadPicSumData().observeOn(AndroidSchedulers.mainThread())
                   .subscribeOn(Schedulers.io())
                   .subscribe({ picSums ->
                       if (picSums.count() > 0 ) {
                           responseLiveData.value = picSums
                           GlobalScope.launch(Dispatchers.Main) {
                               AppRepository(picSumDao).insertAllPicSums(picSums)
                           }
                       }else {
                           statusMessage.value = context.getString(R.string.data_empty)
                       }
                   }, { error ->
                       statusMessage.value = error.toString()
                   }))
       }else {
           statusMessage.value = context.getString(R.string.network_offline_message)
       }
    }

    fun getPicSumList(): LiveData<List<PicSum>> {
        return responseLiveData
    }
    fun getStatusMessage(): LiveData<String> {
        return statusMessage
    }
   private fun isOnline(context: Context): Boolean {
       if (context == null) return false
       val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
           val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
           if (capabilities != null) {
               when {
                   capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                       return true
                   }
                   capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                       return true
                   }
                   capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                       return true
                   }
               }
           }
       } else {
           val activeNetworkInfo = connectivityManager.activeNetworkInfo
           if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
               return true
           }
       }
       return false
    }
    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}