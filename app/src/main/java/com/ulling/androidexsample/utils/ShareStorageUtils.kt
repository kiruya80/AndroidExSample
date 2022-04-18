package com.ulling.androidexsample.utils

import android.content.Context
import android.os.Environment
import androidx.core.content.ContextCompat
import com.ulling.lib.core.utils.QcLog
import java.io.File

// https://developer.android.com/training/data-storage/shared/media?hl=ko
/**
 *
 *
 * 이미지: 사진과 스크린샷을 포함하며, DCIM/ 및 Pictures/ 디렉터리에 저장됩니다. 시스템은 이러한 파일을 MediaStore.Images 테이블에 추가합니다.
 *
 * 동영상: DCIM/, Movies/, Pictures/ 디렉터리에 저장됩니다. 시스템은 이러한 파일을 MediaStore.Video 테이블에 추가합니다.
 *
 * 오디오 파일: Alarms/, Audiobooks/, Music/, Notifications/, Podcasts/, Ringtones/ 디렉터리에 저장됩니다. 또한 시스템은 Music/ 또는 Movies/ 디렉터리에 있는 오디오 재생목록과 Recordings/ 디렉터리에 있는 음성 녹음 파일을 인식합니다.
 * 시스템은 이러한 파일을 MediaStore.Audio 테이블에 추가합니다. 이 녹음 파일 디렉터리는 Android 11(API 수준 30) 이하에서는 사용할 수 없습니다.
 *
 * 다운로드한 파일: Download/ 디렉터리에 저장됩니다. Android 10(API 수준 29) 이상을 실행하는 기기에서는 이러한 파일이 MediaStore.Downloads 테이블에 저장됩니다. Android 9(API 수준 28) 이하에서는 이 테이블을 사용할 수 없습니다.
 *
 * val projection = arrayOf(media-database-columns-to-retrieve)
val selection = sql-where-clause-with-placeholder-variables
val selectionArgs = values-of-placeholder-variables
val sortOrder = sql-order-by-clause

applicationContext.contentResolver.query(
MediaStore.media-type.Media.EXTERNAL_CONTENT_URI,
projection,
selection,
selectionArgs,
sortOrder
)?.use { cursor ->
while (cursor.moveToNext()) {
// Use an ID column from the projection to get
// a URI representing the media item itself.
}
}

 */
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