package com.frost.project_wm.ui.god

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GodViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is G0D Fragment"
    }
    val text: LiveData<String> = _text
}