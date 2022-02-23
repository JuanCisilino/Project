package com.frost.project_wm.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DetailViewModel: ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Detail Fragment"
    }
    val text: LiveData<String> = _text
}