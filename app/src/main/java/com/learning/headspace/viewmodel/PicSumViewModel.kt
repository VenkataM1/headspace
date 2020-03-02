package com.learning.headspace.viewmodel

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.learning.headspace.repository.Repository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import androidx.lifecycle.MutableLiveData
import com.learning.headspace.model.PicSum
import com.learning.headspace.R
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class PicSumViewModel(private val repo: Repository): ViewModel() {

    private val disposables = CompositeDisposable()
    private val responseLiveData = MutableLiveData<List<PicSum>>()
    private val  statusMessage = MutableLiveData<String>()

    fun fetchPicSumData(context:Context) {
         GlobalScope.launch(Dispatchers.Main) {
             val picSums =  repo.getAllPicSums()
             if (picSums.isEmpty()) {
                 getPicSumFromApi(context)
             }else {
                 responseLiveData.value = picSums
             }
         }
    }
    fun getPicSumList(): LiveData<List<PicSum>> {
        return responseLiveData
    }
    fun getStatusMessage(): LiveData<String> {
        return statusMessage
    }
   private fun getPicSumFromApi(context:Context) {
       if (isOnline(context)) {
           disposables.add(
               repo.loadPicSumData().observeOn(AndroidSchedulers.mainThread())
                   .subscribeOn(Schedulers.io())
                   .subscribe({ picSums ->
                       if (picSums.count() > 0 ) {
                           responseLiveData.value = picSums
                           GlobalScope.launch(Dispatchers.Main) {
                               repo.insertAllPicSums(picSums)
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


   private fun isOnline(context: Context): Boolean {
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