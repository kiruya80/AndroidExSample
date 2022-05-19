package com.ulling.androidexsample.common

import android.Manifest
import com.ulling.androidexsample.BuildConfig

class Define {

    companion object {
        /**
         * 상용/ 테스트용
         */
        val DEBUG_FLAG = BuildConfig.DEBUG

        const val PAGE_SIZE = 20

        const val HTTP_READ_TIMEOUT = 5000
        const val HTTP_CONNECT_TIMEOUT = 4000

        /**
         * TimeOut
         */
        const val INTRO_TIMEOUT = 1500

        /**
         * time format
         */
        const val dateFormatFrom_sss = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        const val dateFormatFrom_ss = "yyyy-MM-dd'T'HH:mm:ss'Z'"
        const val displayDateFormatFrom = "MM.dd aa hh:mm:ss"
        const val yyyyMMddFormatFrom = "yyyy MM.dd"

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