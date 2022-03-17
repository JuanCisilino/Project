package com.frost.project_wm.ui.login

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.frost.project_wm.R
import com.frost.project_wm.UserPrefs
import com.frost.project_wm.model.User
import com.frost.project_wm.network.RepoInstance
import com.frost.project_wm.network.UserRepository
import rx.schedulers.Schedulers

class LoginViewModel: ViewModel() {

    private val instance = RepoInstance.getRetrofitInstance().create(UserRepository::class.java)
    var userLiveData = MutableLiveData<User?>()
    var userListLiveData = MutableLiveData<List<User>?>()
    var user : User ?= null
    lateinit var userPrefs: UserPrefs
    lateinit var contextApp: Context

    fun setUserPrefs(context: Context) {
        contextApp = context
        userPrefs = UserPrefs(context)
    }

    fun initServer(){
        instance.getUsers()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe(
                {userListLiveData.postValue(it)},
                {userListLiveData.postValue(null)})
    }

    fun getData(data: String) = userPrefs.getString(data)

    fun save(user: User) {
        userPrefs.save(contextApp.getString(R.string.shared_pref_email), user.email)
        userPrefs.save(contextApp.getString(R.string.shared_pref_name), user.nombre)
        userPrefs.save(contextApp.getString(R.string.shared_pref_role), user.rol)
        userPrefs.save(contextApp.getString(R.string.shared_pref_company), user.empresa)
    }

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
                {
                    user = it.body
                    it.body?.let { user -> save(user) }
                },
                {userLiveData.postValue(null)})

    fun godLogin() =
        instance.getByEmail("test@testing.com")
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe(
                {it.body?.let { userLiveData.postValue(it) }
                    ?:run {userLiveData.postValue(null)}},
                {userLiveData.postValue(null)})

    fun sessionLogin(email: String) =
        instance.getByEmail(email)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe(
                {it.body?.let { userLiveData.postValue(it) }
                    ?:run {userLiveData.postValue(null)}},
                {})
}