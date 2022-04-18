package com.ulling.androidexsample.utils

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Environment
import android.os.storage.StorageManager
import android.os.storage.StorageManager.ACTION_MANAGE_STORAGE
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import com.ulling.lib.core.utils.QcLog
import java.io.File
import java.util.*

// 앱 특화된 external 저장소로, API 19 이상부턴 별다른 권한 없이 접근이 가능하다.
// 앱을 제거할 때 함께 제거된다.
// 항상 접근이 보장되는게 아니기 때문에, 앱의 메인 기능을 이 파일에 의존하면 안된다.

// API 28 이하에서는 적절한 권한만 있으면, 다른 앱의 external 영역에 접근할 수 있다.
// API 29 이상에서는 scoped storage로 관리하면 된다, scoped access를 허용하면, 다른 앱의 external 파일에 접근 못함.
// https://developer.android.com/training/data-storage/app-specific?hl=ko
class ExtStorageUtils (val mCtx: Context){

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

    /**
     * 여유 공간 쿼리
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun getAllocatableBytes(filesDir : File) {

        // App needs 10 MB within internal storage.
         val NUM_BYTES_NEEDED_FOR_MY_APP = 1024 * 1024 * 10L;

        val storageManager = mCtx.getSystemService<StorageManager>()!!
        val appSpecificInternalDirUuid: UUID = storageManager.getUuidForPath(filesDir)
        val availableBytes: Long =
            storageManager.getAllocatableBytes(appSpecificInternalDirUuid)
        if (availableBytes >= NUM_BYTES_NEEDED_FOR_MY_APP) {
            storageManager.allocateBytes(
                appSpecificInternalDirUuid, NUM_BYTES_NEEDED_FOR_MY_APP)
        } else {
            val storageIntent = Intent().apply {
                // To request that the user remove all app cache files instead, set
                // "action" to ACTION_CLEAR_APP_CACHE.
                action = ACTION_MANAGE_STORAGE
            }
        }
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

    /**
     * 앱 내에만 있는 사용자에게 가치를 제공하는 미디어 파일과 앱이 호환되면 다음 코드 스니펫과 같이
     * 외부 저장소 내 앱별 디렉터리에 미디어 파일을 저장하는 것이 가장 좋습니다.
     */
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