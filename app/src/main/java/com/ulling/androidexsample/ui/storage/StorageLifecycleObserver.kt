package com.ulling.androidexsample.ui.storage

import android.Manifest
import android.app.Activity
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.ulling.androidexsample.ui.home.HomeLifecycleObserver
import com.ulling.androidexsample.utils.PermissionUtils
import com.ulling.lib.core.utils.QcLog

class StorageLifecycleObserver(
//    val registry: ActivityResultRegistry,
    val activity: FragmentActivity,
    val viewModel: StorageViewModel
) :   DefaultLifecycleObserver {

    lateinit var registry: ActivityResultRegistry

    override fun onCreate(owner: LifecycleOwner) {
        QcLog.e("onCreate == ")

        registry = activity.activityResultRegistry

    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        QcLog.e("onStart == ")
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        QcLog.e("onResume == ")
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        QcLog.e("onPause == ")
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        QcLog.e("onStop == ")
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        QcLog.e("onDestroy == ")
    }
}