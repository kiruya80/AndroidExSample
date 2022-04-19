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
import java.io.*
import java.util.*

// 앱 특화된 external 저장소로, API 19 이상부턴 별다른 권한 없이 접근이 가능하다.
// 앱을 제거할 때 함께 제거된다.
// 항상 접근이 보장되는게 아니기 때문에, 앱의 메인 기능을 이 파일에 의존하면 안된다.

// API 28 이하에서는 적절한 권한만 있으면, 다른 앱의 external 영역에 접근할 수 있다.
// API 29 이상에서는 scoped storage로 관리하면 된다, scoped access를 허용하면, 다른 앱의 external 파일에 접근 못함.
// https://developer.android.com/training/data-storage/app-specific?hl=ko
/**
 *
 * Android OS 11 이상
 * 권한 필요없음 / Context.getExternalFilesDir() / 앱삭제시 제거
 *
 */
class ExtStorageUtils(val mCtx: Context) {

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
     * 저장소 선택 실제 저장소 위치 선택
     *  기기에 외부 저장소가 포함될 수 있는 여러 실제 볼륨이 있으므로 앱별 저장소에 사용할 볼륨을 선택해야 합니다
     *  여러 볼륨 중에 고르기 API 18 이하에서는 하나만 있다.
     */
    fun selectPhysicalStorageLocation(): File {
        val externalStorageVolumes: Array<out File> =
            ContextCompat.getExternalFilesDirs(mCtx, null)
        for (item in externalStorageVolumes) {
            QcLog.e("item === $item")
        }
        // 0 기본 외부 저장소
        return externalStorageVolumes[0]
    }

    fun createDir(dirName: String, type: String? = null) {
        val externalDir = mCtx.getExternalFilesDir(type)
        val dir = File(externalDir, dirName)
        QcLog.e("dir === $dir")
        if (!dir.exists())
            dir.mkdir()
    }

    fun getFileListFromDir(type: String? = null): ArrayList<File>? {
//        val fileList  = mCtx.getExternalFilesDirs(type)
        val externalDir = mCtx.getExternalFilesDir(type)
        QcLog.e("externalDir === $externalDir")

        val files: ArrayList<File> = arrayListOf()
        var fileList = externalDir?.listFiles()

        if (fileList != null) {
            for (file in fileList) {
                if (file.isFile) {
                    files.add(file)
                    QcLog.i("isFile $file")
                } else if (file.isDirectory) {
                    QcLog.i("isDirectory $file")
                }
            }
        }

//        var filesName = externalDir?.list()
//
//        if (filesName != null) {
//            for (item in filesName) {
//                QcLog.e("files === $item ")
//            }
//        }

//        val files: Array<File> = externalDir?.listFiles(FileFilter { pathname ->
//            pathname.name.toLowerCase(Locale.US).endsWith(".jpg") //확장자
//        }) as Array<File>

        return files
    }

    /**
     *
     * 개별 앱 공간 Context.getExternalFilesDir()접근
     *
     * app-specific 외부 저장소에 파일 접근하기
     *
     *  파라미터가 null 이라면 루트
     * type :
     * Environment.DIRECTORY_MUSIC 음악 파일 저장
    Environment.DIRECTORY_PODCASTS 팟캐스트 파일 저장
    Environment.DIRECTORY_DOWNLOADS 다운로드한 파일 저장
    Environment.DIRECTORY_ALARMS 알람으로 사용할 오디오 저장
    Environment.DIRECTORY_NOTIFICATIONS 알림음 오디오 저장
    Environment.DIRECTORY_PICTURES 그림 파일 저장
    Environment.DIRECTORY_MOVIES 영상 파일 저장
    Environment.DIRECTORY_DCIM 사진 파일 저장

    /storage/emulated/0/Android/data/com.ulling.androidexsample/files/myfile
     */
    fun accessFile(filename: String, type: String? = null): File {
        val externalDir = mCtx.getExternalFilesDir(type)
        val file = File(externalDir, filename)
        QcLog.e("file === $file")
        return file
    }

    fun writeTextToFile(path: String, content: String) {
        QcLog.e("writeTextToFile : $path , $content")
        val file = File(path)
        val fileWriter = FileWriter(file, false)
        val bufferedWriter = BufferedWriter(fileWriter)
        bufferedWriter.write(content)
        bufferedWriter.close()
    }


    fun readTextFromFile(path: String): String {
        QcLog.e("readTextFromFile : $path")
        var content = ""

        val file = File(path)
        if (file.exists() && file.isFile) {
            val fileReader = FileReader(file)
            val bufferedReader = BufferedReader(fileReader)

            bufferedReader.useLines {
                content = it.fold("") { some, text ->
                    "$some\n$text"
                }
            }
        }
        return content
    }


    fun deleteFileList(type: String? = null): Boolean {
        val externalDir = mCtx.getExternalFilesDir(type)
        QcLog.e("deleteFileList === $externalDir")

        var fileList = externalDir?.listFiles()

        var result = true
        if (fileList != null) {
            for (file in fileList) {
                if (file.isFile) {
                    val deleteResult = deleteFile(file.path)
                    QcLog.i("isFile $file")
                    result = deleteResult == true && result == true
                } else if (file.isDirectory) {
                    QcLog.i("isDirectory $file")
                }
            }
        }
        return result
    }


    fun deleteFile(filePath: String): Boolean {
        val file = File(filePath)
        if (file.exists() && file.isFile) {
            val result = file.canonicalFile.delete()
            return result
        }
        return true
    }


    // API 30 이상부터 앱에서 external storage에 본인 소유 directory 못만든다.
    fun createCacheFile(cacheFile: String): File? {
        if (isExternalStorageWritable()) {
            return File(mCtx.externalCacheDir, cacheFile)
        } else {
            return null
        }
    }

    fun deleteCacheFile(cacheFile: String): Boolean {
        if (isExternalStorageWritable()) {
            val file = File(mCtx.externalCacheDir, cacheFile)
            if (file.exists()) {
                return file.delete()
            } else {
                return true
            }
        } else {
            return true
        }
    }


    /**
     * 앱 내에만 있는 사용자에게 가치를 제공하는 미디어 파일과 앱이 호환되면 다음 코드 스니펫과 같이
     * 외부 저장소 내 앱별 디렉터리에 미디어 파일을 저장하는 것이 가장 좋습니다.
     */
    fun getAppSpecificAlbumStorageDir(context: Context, albumName: String): File? {
        val file = File(
            context.getExternalFilesDir(
                Environment.DIRECTORY_PICTURES
            ), albumName
        )
        if (!file?.mkdirs()) {
            QcLog.e("Directory not created")
        }
        return file
    }

    /**
     * 여유 공간 쿼리
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun getAllocatableBytes(filesDir: File) {

        // App needs 10 MB within internal storage.
        val NUM_BYTES_NEEDED_FOR_MY_APP = 1024 * 1024 * 10L;

        val storageManager = mCtx.getSystemService<StorageManager>()!!
        val appSpecificInternalDirUuid: UUID = storageManager.getUuidForPath(filesDir)
        val availableBytes: Long =
            storageManager.getAllocatableBytes(appSpecificInternalDirUuid)
        if (availableBytes >= NUM_BYTES_NEEDED_FOR_MY_APP) {
            storageManager.allocateBytes(
                appSpecificInternalDirUuid, NUM_BYTES_NEEDED_FOR_MY_APP
            )
        } else {
            val storageIntent = Intent().apply {
                // To request that the user remove all app cache files instead, set
                // "action" to ACTION_CLEAR_APP_CACHE.
                action = ACTION_MANAGE_STORAGE
            }
        }
    }
}