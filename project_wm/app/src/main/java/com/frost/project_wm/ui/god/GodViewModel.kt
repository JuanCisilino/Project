package com.frost.project_wm.ui.god

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.frost.project_wm.model.User
import com.frost.project_wm.network.RepoInstance
import com.frost.project_wm.network.Repository
import rx.schedulers.Schedulers

class GodViewModel : ViewModel() {

    private val instance = RepoInstance.getRetrofitInstance().create(Repository::class.java)
    val userList = MutableLiveData<List<User>?>()

    fun getList() = instance.getUsers()
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.io())
        .subscribe(
            { userList.postValue(it) },
            { userList.postValue(null)})
}