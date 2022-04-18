package com.ulling.androidexsample.utils

import android.content.Context
import java.io.File

class InterStorageUtils {

    private lateinit var mCtx: Context

    // 내부 저장소에 저장된 파일에 접근하기
    fun accessStoredFile(): File {
        val dir = mCtx.filesDir
        val filename = "sample.txt"
        val file = File(dir, filename)
        return file
    }

    // Stream 사용해서 파일 저장하기
    fun storeFileUsingStream(fileName:String, contents:String) {
//        val fileName = "sample.txt"
//        val contents = "Hello World!"
        // API 24 이상에서, MODE_PRIVATE 사용 안하면, SecurityException 발생
        mCtx.openFileOutput(fileName, Context.MODE_PRIVATE).use {
            it.write(contents.toByteArray())
        }
    }

    // Stream 사용해서 파일 접근하기
    fun accessFileUsingStream(fileName:String) {
//        val fileName = "sample.txt"
        mCtx.openFileInput(fileName).bufferedReader().useLines { lines ->
            lines.fold("") { some, text ->
                "$some\n$text"
            }
        }
    }

    // 파일 리스트 받아오기
    fun viewListOfFiles() {
        var files: Array<String> = mCtx.fileList()
    }

    // 폴더 만들기
    fun createNestedDir() {
        val dirName = "sub"
        mCtx.getDir(dirName, Context.MODE_PRIVATE)
    }

    // 캐시 파일 만들기
    fun createCacheFile(fileName:String) {
        val cacheDir = mCtx.cacheDir
//        val fileName = "temp.jpg"
        File.createTempFile(fileName, null, cacheDir)
    }

    // 캐시 파일 접근하기, 단 캐시 파일의 경우 안드로이드가 임의로 지워버릴 수 있음.
    fun accessCacheFile(fileName:String): File {
//        val filename = "temp.jpg"
        val cacheFile = File(mCtx.cacheDir, fileName)
        return cacheFile
    }

    // 캐시 파일 제거하기
    // 안드로이드가 캐시 파일을 제거를 보장하지는 않음. 적절한 처리할 것
    fun removeCacheFile(fileName:String) {
        val cacheFile = accessCacheFile(fileName)
        // case 1
        cacheFile.delete()
        // case 2
        mCtx.deleteFile(cacheFile.name)
    }
}