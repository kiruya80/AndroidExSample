package com.ulling.androidexsample.ui.storage.inter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StorageInterViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "내부 저장소 테스트 (Android OS 11 R 30)"
    }
    val text: LiveData<String> = _text

}