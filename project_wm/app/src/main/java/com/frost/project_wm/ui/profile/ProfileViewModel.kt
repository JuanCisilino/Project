package com.frost.project_wm.ui.profile

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.frost.project_wm.UserPrefs
import com.frost.project_wm.model.User

class ProfileViewModel: ViewModel() {

   lateinit var userPrefs: UserPrefs
   lateinit var user: User

   fun setUserPrefs(context: Context) {
       userPrefs = UserPrefs(context)
   }

    fun getData(data: String) = userPrefs.getString(data)

}