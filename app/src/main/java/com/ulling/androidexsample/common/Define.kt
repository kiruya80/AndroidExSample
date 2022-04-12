package com.ulling.androidexsample.common

import android.Manifest

class Define {

    companion object {

    }
}

val  permissionList: Array<String> = arrayOf(
    Manifest.permission.WRITE_EXTERNAL_STORAGE,
//        Manifest.permission.ACCESS_NETWORK_STATE,
    Manifest.permission.CAMERA,
    Manifest.permission.RECORD_AUDIO
)

 val  permissionListAfterR: Array<String> = arrayOf(
    Manifest.permission.READ_EXTERNAL_STORAGE,
//        Manifest.permission.ACCESS_NETWORK_STATE,
    Manifest.permission.CAMERA,
    Manifest.permission.RECORD_AUDIO
)

 val  permissionListRW: Array<String> = arrayOf(
    Manifest.permission.WRITE_EXTERNAL_STORAGE,
    Manifest.permission.READ_EXTERNAL_STORAGE,
)