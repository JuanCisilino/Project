package com.frost.project_wm.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.frost.project_wm.model.User
import com.frost.project_wm.network.RepoInstance
import com.frost.project_wm.network.Repository
import rx.schedulers.Schedulers

class LoginViewModel: ViewModel() {

    private val instance = RepoInstance.getRetrofitInstance().create(Repository::class.java)
    var userLiveData = MutableLiveData<User?>()

    fun saveUser(user: User) =
        instance.createUser(user)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe(
                {userLiveData.postValue(it)},
                {userLiveData.postValue(null)})

    fun getUserByEmail(email: String) =
        instance.getByEmail(email)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe(
                {userLiveData.postValue(it.body)},
                {userLiveData.postValue(null)})
}