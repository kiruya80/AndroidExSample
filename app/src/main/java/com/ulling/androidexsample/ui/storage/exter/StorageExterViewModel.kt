package com.ulling.androidexsample.ui.storage.exter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StorageExterViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "외부 저장소 테스트"
    }
    val text: LiveData<String> = _text

}