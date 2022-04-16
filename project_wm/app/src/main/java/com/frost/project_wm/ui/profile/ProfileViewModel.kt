package com.frost.project_wm.ui.profile

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.frost.project_wm.UserPrefs
import com.frost.project_wm.model.Product
import com.frost.project_wm.model.User
import com.frost.project_wm.network.ProductRepository
import com.frost.project_wm.network.RepoInstance
import com.frost.project_wm.network.UserRepository
import rx.schedulers.Schedulers

class ProfileViewModel: ViewModel() {

    lateinit var userPrefs: UserPrefs
    lateinit var user: User
    private val userInstance = RepoInstance.getRetrofitInstance().create(UserRepository::class.java)
    private val prodInstance = RepoInstance.getRetrofitInstance().create(ProductRepository::class.java)
    val userList = MutableLiveData<List<User>?>()
    val productList = MutableLiveData<List<Product>?>()

    fun getList() = userInstance.getUsers()
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.io())
        .subscribe(
            { userList.postValue(it) },
            { userList.postValue(null)})

    fun getProducts() = prodInstance.getProducts()
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.io())
        .subscribe(
            { productList.postValue(it) },
            { productList.postValue(null)})

   fun setUserPrefs(context: Context) {
       userPrefs = UserPrefs(context)
   }

    fun getData(data: String) = userPrefs.getString(data)

}