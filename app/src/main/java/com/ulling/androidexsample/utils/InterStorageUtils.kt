package com.ulling.androidexsample.utils

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.storage.StorageManager
import androidx.annotation.RequiresApi
import androidx.core.content.getSystemService
import com.ulling.lib.core.utils.QcLog
import kotlinx.android.synthetic.main.fragment_storage_inter.*
import java.io.File
import java.util.*

// https://blog.naver.com/PostView.nhn?blogId=horajjan&logNo=221568409591&from=search&redirect=Log&widgetTypeCall=true&directAccess=false

/**
 * Android OS 11 이상
 * 권한 필요없음 / Context.getFilesDir() / 앱삭제시 제거
 *
 * 경로는 /data/data/com.example.appname 이고 캐시의경우 /data/data/com.example.appname/cache가 됩니다.
 */
class InterStorageUtils(val mCtx: Context) {

    // 폴더 만들기
    /**
     * 중첩된 디렉터리 만들기
     */
    fun createDir(dirName: String) {
        mCtx.getDir(dirName, Context.MODE_PRIVATE)
    }

    // 내부 저장소에 저장된 파일에 접근하기
    fun accessFile(fileName: String): File {
        val file = File(mCtx.filesDir, fileName)
        QcLog.e("file ===== " + file.toString())
        return file
    }

    // Stream 사용해서 파일 저장하기
    fun saveFileUsingStream(fileName: String, contents: String) {
        // API 24 이상에서, MODE_PRIVATE 사용 안하면, SecurityException 발생
        mCtx.openFileOutput(fileName, Context.MODE_PRIVATE).use {
            it.write(contents.toByteArray())
        }
    }

    // Stream 사용해서 파일 접근하기
    fun getFileUsingStream(fileName: String): String {
        var content = ""
//        mCtx.openFileInput(fileName).bufferedReader().useLines { lines ->
//            lines.fold("") { some, text ->
//                "$some\n$text"
//            }
//        }
        mCtx.openFileInput(fileName).bufferedReader().useLines { lines ->
            content = lines.fold("") { some, text ->
                "$some\n$text"
            }
        }

//        QcLog.e("라인별 가져오기 ======")
//        val lineList = mutableListOf<String>()
//        mCtx.openFileInput(fileName).bufferedReader().useLines { lines ->
//            lines.forEach {
//                lineList.add(it)
//            }
//            QcLog.e("contentText ====== $lineList")
//        }

        QcLog.e("content ====== $content")
        return content
    }

    fun deleteFileList(): Boolean {
        val files: Array<String> = mCtx.fileList()
        var result = true
        for (item in files) {
            QcLog.e("file ===== " + item)
            val deleteResult = deleteFile(item)
            result = deleteResult == true && result == true
        }
        return result
    }

    fun deleteFile(fileName: String): Boolean {
        val file = File(mCtx.filesDir, fileName)
        if (file.exists() && file.isFile) {
            val result = file.canonicalFile.delete()
            return result
        }
        return true
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun getFileList(): Array<String> {
        val lineList = mutableListOf<String>()

        QcLog.e("file cacheDir ===== " + mCtx.cacheDir.toString())
        QcLog.e("file codeCacheDir ===== " + mCtx.codeCacheDir.toString())
        QcLog.e("file dataDir ===== " + mCtx.dataDir.toString())
        QcLog.e("file filesDir ===== " + mCtx.filesDir.toString())
        QcLog.e("file noBackupFilesDir ===== " + mCtx.noBackupFilesDir.toString())


        val files: Array<String> = mCtx.fileList()
        for (item in files) {
            lineList.add(item)
            QcLog.e("file ===== " + item.toString())
        }

        return files
    }


    // 캐시 파일 만들기
    fun createCacheFile(fileName: String) {
        File.createTempFile(fileName, null, mCtx.cacheDir)
    }

    // 캐시 파일 접근하기, 단 캐시 파일의 경우 안드로이드가 임의로 지워버릴 수 있음.
    fun accessCacheFile(fileName: String): File {
        val cacheFile = File(mCtx.cacheDir, fileName)
        return cacheFile
    }

    // 캐시 파일 제거하기
    // 안드로이드가 캐시 파일을 제거를 보장하지는 않음. 적절한 처리할 것
    fun removeCacheFile(fileName: String) {
        val cacheFile = accessCacheFile(fileName)
        // case 1
        cacheFile.delete()
        // case 2
        mCtx.deleteFile(cacheFile.name)
    }
}