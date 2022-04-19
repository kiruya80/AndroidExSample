package com.ulling.androidexsample.ui.storage.share

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ulling.androidexsample.R
import com.ulling.androidexsample.base.BaseFragment
import com.ulling.androidexsample.component.clieckevent.setOnHasTermClickListener
import com.ulling.androidexsample.utils.ExtStorageUtils
import com.ulling.lib.core.utils.QcLog
import com.ulling.lib.core.utils.QcToast
import kotlinx.android.synthetic.main.fragment_storage_share.*
import java.io.File
import kotlin.random.Random

/**
 * 공유 저장소
 *
 *  https://developer.android.com/training/data-storage/shared/media?hl=ko
 *
 */
class StorageShareFragment : BaseFragment(R.layout.fragment_storage_share) {

    private lateinit var storageViewModel: StorageShareViewModel
    lateinit var observerInter: StorageShareLifecycleObserver

    var fileDir = "sampleDir"
    var fileName = "myfile"
    var fileContents = "Hello world!\n@world@ @world@ @world@"

    override fun init() {
        QcLog.e("init ======== ")

        storageViewModel =
            ViewModelProvider(this).get(StorageShareViewModel::class.java)

        observerInter = StorageShareLifecycleObserver(
            requireActivity(),
            storageViewModel
        )
        lifecycle.addObserver(observerInter)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun initView() {
        QcLog.e("initView ======== ")
        storageViewModel.text.observe(viewLifecycleOwner, Observer {
            text_title.text = it
        })

        btn_permission_share.setOnHasTermClickListener {
            QcLog.e("btn_permission_share === ")
            val isExternalStorageWritable = ExtStorageUtils(mCtx).isExternalStorageWritable()
            val isExternalStorageReadable = ExtStorageUtils(mCtx).isExternalStorageReadable()
            QcToast.getInstance()
                .show("쓰기 가능 : $isExternalStorageWritable, 읽기 가능 : $isExternalStorageReadable")
        }


        btn_share_makeFile.setOnHasTermClickListener {
            QcLog.e("btn_share_makeFile === ")
            fileName = edt_share_makeFile.text.toString() + Random.nextInt(1000).toString()
            if (fileName.isNullOrEmpty())
                fileName = "testFile_" + Random.nextInt(1000).toString()
            makeFile(fileName)
        }
        btn_share_makeFolder.setOnHasTermClickListener {
            QcLog.e("btn_share_makeFolder === ")
            fileDir = edt_share_makeFile.text.toString() + Random.nextInt(1000).toString()
            if (fileDir.isNullOrEmpty())
                fileDir = "testFolder_" + Random.nextInt(1000).toString()
            createInnerDir(fileDir)
        }


        btn_share_saveFile.setOnHasTermClickListener {
            QcLog.e("btn_share_saveFile === ")
            fileContents = fileName + "\n" + edt_share_saveFile.text.toString()
            saveFile(fileName, fileContents)
        }

        btn_share_getFile.setOnHasTermClickListener {
            QcLog.e("btn_share_getFile === ")
            getFile(fileName)
        }

        btn_share_deleteFile.setOnHasTermClickListener {
            QcLog.e("btn_share_deleteFile === ")
            getDeleteFile(fileName)
        }



        btn_share_getFileList.setOnHasTermClickListener {
            QcLog.e("btn_share_getFileList === ")
            getFileList()
        }

        btn_share_deleteFileList.setOnHasTermClickListener {
            QcLog.e("btn_share_deleteFileList === ")
            getDeleteFileList()
        }
    }

    // 내부 저장소

    /**
     * 중첩된 디렉터리 만들기
     */
    fun createInnerDir(dirname: String) {
        val result = mCtx.getDir(dirname, Context.MODE_PRIVATE)
        QcLog.e("createInnerDir ===== " + result)
    }

    private fun makeFile(fileName: String) {
        val file = File(mCtx.filesDir, fileName)
        QcLog.e("file ===== " + file.toString())
        text_share_makeFile.text = file.toString()
    }

    private fun saveFile(fileName: String, contents: String) {
        mCtx.openFileOutput(fileName, Context.MODE_PRIVATE).use {
//            it?.write(fileContents.toByteArray())
            it?.write(contents.toByteArray())
        }
    }

    // https://blog.naver.com/PostView.nhn?blogId=horajjan&logNo=221568409591&from=search&redirect=Log&widgetTypeCall=true&directAccess=false
    private fun getFile(fileName: String) {
        QcLog.e("getFile === $fileName ")
        val file = File(mCtx.filesDir, fileName)
        if (!file.exists()) {
            text_share_getFile.text = "파일이 없음"
            return
        }

        mCtx.openFileInput(fileName).bufferedReader().useLines { lines ->
            var contentText = lines.fold("") { some, text ->
                "$some\n$text"
            }
            QcLog.e("contentText ====== $contentText")
            text_share_getFile.text = contentText
        }

//        QcLog.e("라인별 가져오기 ======")
//        val lineList = mutableListOf<String>()
//        mCtx.openFileInput(fileName).bufferedReader().useLines { lines ->
//            lines.forEach {
//                lineList.add(it)
//            }
//            QcLog.e("contentText ====== $lineList")
//        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getFileList() {
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
        text_share_getFileList.text = lineList.toString()
    }


    private fun getDeleteFileList() {
        val files: Array<String> = mCtx.fileList()
        var result = true
        for (item in files) {
            QcLog.e("file ===== " + item)
            val deleteResult = getDeleteFile(item)
            result = deleteResult == true && result == true
        }
        text_share_deleteFileList.text = "앱 내부 파일 삭제 성공 $result"
    }

    private fun getDeleteFile(fileName: String): Boolean {
        val file = File(mCtx.filesDir, fileName)
        if (file.exists()) {
            val result = file.canonicalFile.delete()
            return result
        }
        return true
    }


    // 캐시 파일 만들기
    fun createCacheFile() {
        val filename = "temp.jpg"
        File.createTempFile(filename, null, mCtx.cacheDir)
    }

    // 캐시 파일 접근하기, 단 캐시 파일의 경우 안드로이드가 임의로 지워버릴 수 있음.
    fun accessCacheFile(): File {
        val filename = "temp.jpg"
        val cacheFile = File(mCtx.cacheDir, filename)
        return cacheFile
    }

    // 캐시 파일 제거하기
    // 안드로이드가 캐시 파일을 제거를 보장하지는 않음. 적절한 처리할 것
    fun removeCacheFile() {
        val cacheFile = accessCacheFile()
        // case 1
        cacheFile.delete()
        // case 2
        mCtx.deleteFile(cacheFile.name)
    }
}