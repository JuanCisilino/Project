package com.frost.project_wm.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.frost.project_wm.model.Product
import com.frost.project_wm.network.RepoInstance
import com.frost.project_wm.network.Repository
import rx.schedulers.Schedulers

class HomeViewModel : ViewModel() {

    private val instance = RepoInstance.getRetrofitInstance().create(Repository::class.java)
    val productList = MutableLiveData<List<Product>?>()

    fun getList() = instance.getProducts()
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.io())
        .subscribe(
            { productList.postValue(it) },
            { productList.postValue(null)})
}