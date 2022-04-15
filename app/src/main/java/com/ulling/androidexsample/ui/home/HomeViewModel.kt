package com.ulling.androidexsample.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel(param: String) : ViewModel() {

    val text: MutableLiveData<String> = MutableLiveData()



//    private val _text = MutableLiveData<String>().apply {
//        value = "This is home Fragment"
//    }
//    val text: LiveData<String> = _text


}