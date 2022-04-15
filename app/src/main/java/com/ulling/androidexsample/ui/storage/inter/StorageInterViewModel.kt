package com.ulling.androidexsample.ui.storage.inter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StorageInterViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is storage Fragment"
    }
    val text: LiveData<String> = _text

}