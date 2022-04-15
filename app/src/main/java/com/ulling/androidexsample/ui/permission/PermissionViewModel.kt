package com.ulling.androidexsample.ui.permission

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

/**
 *  ViewModel 클래스에서 Context 객체를 소유하거나 접근하는 것에 있어서 권장하지 않고 있습니다.
 *  하지만 정말 불가피하게 필요한 경우가 있을 수 있는데요.
 *  ViewModel 에서 Context 를 사용해야할 필요성이 있을 때는 AndroidViewModel 클래스를 사용하면 됩니다.
 */
class PermissionViewModel(application: Application, param: String) : AndroidViewModel(application) {

    val text: MutableLiveData<String> = MutableLiveData()

}