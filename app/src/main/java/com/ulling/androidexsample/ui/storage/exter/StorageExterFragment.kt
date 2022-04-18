package com.ulling.androidexsample.ui.storage.exter

import android.content.Context
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ulling.androidexsample.R
import com.ulling.androidexsample.base.BaseFragment
import com.ulling.androidexsample.component.clieckevent.setOnHasTermClickListener
import com.ulling.androidexsample.utils.ExtStorageUtils
import com.ulling.lib.core.utils.QcLog
import com.ulling.lib.core.utils.QcToast
import kotlinx.android.synthetic.main.fragment_storage_exter.*
import kotlinx.android.synthetic.main.fragment_storage_exter.text_title
import java.io.File
import kotlin.random.Random

/**
 * 외부 저장소
 *
 * https://developer.android.com/training/data-storage/app-specific?hl=ko
 *
 */
class StorageExterFragment : BaseFragment(R.layout.fragment_storage_exter) {

    private lateinit var storageViewModel: StorageExterViewModel
    lateinit var observerInter: StorageExterLifecycleObserver

    var fileDir = "sampleDir"
    var fileName = "myfile"
    var fileContents = "Hello world!\n@world@ @world@ @world@"

    override fun init() {
        QcLog.e("init ======== ")

        storageViewModel =
            ViewModelProvider(this).get(StorageExterViewModel::class.java)

        observerInter = StorageExterLifecycleObserver(
            requireActivity(),
            storageViewModel
        )
        lifecycle.addObserver(observerInter)
    }

    override fun initView() {
        QcLog.e("initView ======== ")
        storageViewModel.text.observe(viewLifecycleOwner, Observer {
            text_title.text = it
        })
        text_title.text = "외부 저장소 테스트"



        btn_permission.setOnHasTermClickListener {
            QcLog.e("btn_permission === ")
           val isExternalStorageWritable = ExtStorageUtils(mCtx).isExternalStorageWritable()
            QcLog.e("isExternalStorageWritable : $isExternalStorageWritable ")
            val isExternalStorageReadable = ExtStorageUtils(mCtx).isExternalStorageReadable()
            QcLog.e("isExternalStorageReadable : $isExternalStorageReadable ")
            QcToast.getInstance().show("쓰기 가능 : $isExternalStorageWritable, 읽기 가능 : $isExternalStorageReadable")
        }

        btn_ext_makeFile.setOnHasTermClickListener {
            QcLog.e("btn_ext_makeFile === ")
            fileName = edt_ext_makeFile.text.toString() + Random.nextInt(1000).toString()
            if (fileName.isNullOrEmpty())
                fileName = "testFile_" + Random.nextInt(1000).toString()


            ExtStorageUtils(mCtx).selectPhysicalStorageLocation()
        }

        btn_ext_makeFolder.setOnHasTermClickListener {
            QcLog.e("btn_ext_makeFolder === ")
            fileDir = edt_ext_makeFile.text.toString() + Random.nextInt(1000).toString()
            if (fileDir.isNullOrEmpty())
                fileDir = "testFolder_" + Random.nextInt(1000).toString()
//            createInnerDir(fileDir)

            ExtStorageUtils(mCtx).accessFile(fileDir)
        }

        btn_ext_saveFile.setOnHasTermClickListener {
            QcLog.e("btn_ext_saveFile === ")
            fileContents = fileName +"\n" +edt_ext_saveFile.text.toString()
//            saveFile(fileName, fileContents)

            var cacheFile = "cacheFile_" + Random.nextInt(1000).toString()
            val result =ExtStorageUtils(mCtx).createCacheFile(cacheFile)
            QcLog.e("createCacheFile ===  $result")
        }







        btn_ext_getFile.setOnHasTermClickListener {
            QcLog.e("btn_ext_getFile === ")
//            getFile(fileName)
        }

        btn_ext_deleteFile.setOnHasTermClickListener {
            QcLog.e("btn_ext_deleteFile === ")
//            getDeleteFile(fileName)
        }

        btn_ext_getFileList.setOnHasTermClickListener {
            QcLog.e("btn_ext_getFileList === ")
//            getFileList()
        }

        btn_ext_deleteFileList.setOnHasTermClickListener {
            QcLog.e("btn_ext_deleteFileList === ")
//            getDeleteFileList()
        }
    }

    fun createInnerDir(dirname:String) {
        val result = mCtx.getDir(dirname, Context.MODE_PRIVATE)
        QcLog.e("createInnerDir ===== " + result)
    }

    // 내부 저장소
    private fun makeFile(fileName: String) {
        val file = File(mCtx.filesDir, fileName)
        QcLog.e("file ===== " + file.toString())
        text_ext_makeFile.text = file.toString()
    }

    private fun saveFile(fileName :String, contents:String) {
        mCtx.openFileOutput(fileName, Context.MODE_PRIVATE).use {
            it?.write(contents.toByteArray())
        }
    }

    private fun getFile(fileName: String) {
        QcLog.e("getFile === $fileName ")
        val file = File(mCtx.filesDir, fileName)
        if (!file.exists()) {
            text_ext_getFile.text = "파일이 없음"
            return
        }

        mCtx.openFileInput(fileName).bufferedReader().useLines { lines ->
            var contentText = lines.fold("") { some, text ->
                "$some\n$text"
            }
            QcLog.e("contentText ====== $contentText")
            text_ext_getFile.text = contentText
        }
    }

    private fun getFileList() {
        QcLog.e("file externalCacheDir ===== " + mCtx.externalCacheDir.toString())
        QcLog.e("file getExternalFilesDir ===== " + mCtx.getExternalFilesDir(null).toString())

        val lineList = mutableListOf<String>()
        val files: Array<String> = mCtx.fileList()
        for (item in files) {
            lineList.add(item)
            QcLog.e("file ===== " + item.toString())
        }
        text_ext_getFileList.text = lineList.toString()


        val fileList = mutableListOf<File>()
        val file: Array<File> = mCtx.getExternalMediaDirs()
        for (item in file) {
            QcLog.e("file ===== " + item.toString())
        }
    }


    private fun getDeleteFileList() {
        val files: Array<String> = mCtx.fileList()
        var result = true
        for (item in files) {
            QcLog.e("file ===== " + item)
            val deleteResult = getDeleteFile(item)
            result = deleteResult == true && result == true
        }
        text_ext_deleteFileList.text = "앱 외부 파일 삭제 성공 $result"
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
        val cacheDir = mCtx.cacheDir
        val filename = "temp.jpg"
        File.createTempFile(filename, null, cacheDir)
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