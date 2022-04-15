package com.ulling.androidexsample.ui.storage.exter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StorageExterViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is storage Fragment"
    }
    val text: LiveData<String> = _text

}