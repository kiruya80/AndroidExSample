package com.ulling.androidexsample.utils

import android.content.Context
import android.os.Environment
import androidx.core.content.ContextCompat
import com.ulling.lib.core.utils.QcLog
import java.io.File

// https://developer.android.com/training/data-storage/shared/media?hl=ko
class ShareStorageUtils (val mCtx: Context){

//    private lateinit var mCtx: Context


    // external은 항상 접근가능하지 않다. (sd card, usb를 생각해봐라 사용자가 마음대로 뽑아버릴 수 있다.)

    // 쓰기 가능한 상태인지 체크
    fun isExternalStorageWritable(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }

    // 읽기 가능한 상태인지 체크
    fun isExternalStorageReadable(): Boolean {
        return Environment.getExternalStorageState() in
                setOf(Environment.MEDIA_MOUNTED, Environment.MEDIA_MOUNTED_READ_ONLY)
    }

    // 여러 볼륨 중에 고르기
    // API 18 이하에서는 하나만 있다.
    // 실제 저장소 위치 선택
    fun selectPhysicalStorageLocation(): File {
        val externalStorageVolumes: Array<out File> =
            ContextCompat.getExternalFilesDirs(mCtx, null)
        for (item in externalStorageVolumes) {
            QcLog.e("item === $item")
        }
        val primaryExternalStorage = externalStorageVolumes[0]
        return primaryExternalStorage
    }

    // app-specific 외부 저장소에 파일 접근하기
    fun accessFile(filename: String): File {
        val externalDir = mCtx.getExternalFilesDir(null)
//        val filename = "sample.txt"
        val file = File(externalDir, filename)
        QcLog.e("file === $file")
        return file
    }

    // API 30 이상부터 앱에서 external storage에 본인 소유 directory 못만든다.

    fun createCacheFile(cacheFile: String): File? {
        if (isExternalStorageWritable()) {
            val externalCacheDir = mCtx.externalCacheDir
//            val filename = "temp.jpg"
            val file = File(externalCacheDir, cacheFile)
            return file
        } else {
            return null
        }
    }

    fun deleteCacheFile(cacheFile: String): Boolean {
        if (isExternalStorageWritable()) {
            val externalCacheDir = mCtx.externalCacheDir
            val file = File(externalCacheDir, cacheFile)
            return file.delete()
        } else {
            return true
        }
    }

    fun getAppSpecificAlbumStorageDir(context: Context, albumName: String): File? {
        // Get the pictures directory that's inside the app-specific directory on
        // external storage.
        val file = File(context.getExternalFilesDir(
            Environment.DIRECTORY_PICTURES), albumName)
        if (!file?.mkdirs()) {
            QcLog.e("Directory not created")
        }
        return file
    }
}