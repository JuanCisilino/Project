package com.frost.project_wm.ui.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.frost.project_wm.model.Product
import com.frost.project_wm.model.User
import com.frost.project_wm.model.UserUpdateRequest
import com.frost.project_wm.network.RepoInstance
import com.frost.project_wm.network.Repository
import rx.schedulers.Schedulers

class DetailViewModel: ViewModel() {

    private val instance = RepoInstance.getRetrofitInstance().create(Repository::class.java)
    var user: User?= null
    var product: Product?= null
    var userLiveData = MutableLiveData<User?>()
    var productLiveData = MutableLiveData<Product?>()

    fun changeRole(newRole: String) =
        instance.modifyRole(UserUpdateRequest(user!!.email, newRole))
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe({
                user = it
                userLiveData.postValue(it)
            },{
                userLiveData.postValue(null)
            })

}