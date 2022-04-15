package com.ulling.androidexsample.ui.storage.exter

import android.content.Context
import android.os.Environment
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ulling.androidexsample.R
import com.ulling.androidexsample.base.BaseFragment
import com.ulling.androidexsample.component.clieckevent.setOnHasTermClickListener
import com.ulling.lib.core.utils.QcLog
import kotlinx.android.synthetic.main.fragment_storage_exter.*
import java.io.File

/**
 * 외부, 내부 저장소
 *
 * https://developer.android.com/training/data-storage/app-specific?hl=ko
 *
 */
class StorageExterFragment : BaseFragment(R.layout.fragment_storage_exter) {

    private lateinit var storageViewModel: StorageExterViewModel
    lateinit var observerInter: StorageExterLifecycleObserver

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
        text_title.text = "내부 저장소 테스트"

//        btn_permission_read_write.setOnHasTermClickListener {
//            QcLog.e("btn_permission_read_write === ")
//            if (!PermissionUtils.isReadWritePermission(mCtx, permissionListRW)) {
//                PermissionUtils.requestReadWritePermission(
//                    mCtx,
//                    observerInter.actResultStartActivity,
//                    observerInter.actResultMultiPermissions
//                )
//            }
//        }

        btn_in_makeFile.setOnHasTermClickListener {
            QcLog.e("btn_in_makeFile === ")
            makeFile("testFile")
        }

        btn_in_saveFile.setOnHasTermClickListener {
            QcLog.e("btn_in_saveFile === ")
            saveFile()
        }

        btn_in_getFile.setOnHasTermClickListener {
            QcLog.e("btn_in_getFile === ")
            getFile(fileName)
        }

        btn_in_getFileList.setOnHasTermClickListener {
            QcLog.e("btn_in_getFileList === ")
            getFileList()
        }
    }

    val fileName = "myfile"
    val fileContents = "Hello world!\n@world@ @world@ @world@"

    // 내부 저장소
    private fun makeFile(fileName: String) {
        val file = File(mCtx.filesDir, fileName)
        QcLog.e("file ===== " + file.toString())
    }

    private fun saveFile() {
        mCtx.openFileOutput(fileName, Context.MODE_PRIVATE).use {
//            it?.write(fileContents.toByteArray())
            it?.write(edt_send_data.text.toString().toByteArray())
        }
    }

    // https://blog.naver.com/PostView.nhn?blogId=horajjan&logNo=221568409591&from=search&redirect=Log&widgetTypeCall=true&directAccess=false
    private fun getFile(fileName: String) {
        mCtx.openFileInput(fileName).bufferedReader().useLines { lines ->
            var contentText = lines.fold("") { some, text ->
                "$some\n$text"
            }
            QcLog.e("contentText ====== $contentText")
        }

        QcLog.e("라인별 가져오기 ======")
        val lineList = mutableListOf<String>()
        mCtx.openFileInput(fileName).bufferedReader().useLines { lines ->
            lines.forEach {
                lineList.add(it)
            }
            QcLog.e("contentText ====== $lineList")
        }
    }

    private fun getFileList() {
        var files: Array<String> = mCtx.fileList()
        for (item in files) {
            QcLog.e("file ===== " + item.toString())
        }
    }


    private fun getDeleteFileList() {
        var files: Array<String> = mCtx.fileList()
        for (item in files) {
            QcLog.e("file ===== " + item.toString())
        }
    }

    // 폴더 만들기
    fun createNestedDir() {
        val dirName = "sub"
        mCtx.getDir(dirName, Context.MODE_PRIVATE)
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

class InternalFileStorageUseCase {

    private lateinit var context: Context

    // 내부 저장소에 저장된 파일에 접근하기
    fun accessStoredFile(): File {
        val dir = context.filesDir
        val filename = "sample.txt"
        val file = File(dir, filename)
        return file
    }

    // Stream 사용해서 파일 저장하기
    fun storeFileUsingStream() {
        val filename = "sample.txt"
        val fileContent = "Hello World!"
        // API 24 이상에서, MODE_PRIVATE 사용 안하면, SecurityException 발생
        context.openFileOutput(filename, Context.MODE_PRIVATE).use {
            it.write(fileContent.toByteArray())
        }
    }

    // Stream 사용해서 파일 접근하기
    fun accessFileUsingStream() {
        val filename = "sample.txt"
        context.openFileInput(filename).bufferedReader().useLines { lines ->
            lines.fold("") { some, text ->
                "$some\n$text"
            }
        }
    }

    // 파일 리스트 받아오기
    fun viewListOfFiles() {
        var files: Array<String> = context.fileList()
    }

    // 폴더 만들기
    fun createNestedDir() {
        val dirName = "sub"
        context.getDir(dirName, Context.MODE_PRIVATE)
    }

    // 캐시 파일 만들기
    fun createCacheFile() {
        val cacheDir = context.cacheDir
        val filename = "temp.jpg"
        File.createTempFile(filename, null, cacheDir)
    }

    // 캐시 파일 접근하기, 단 캐시 파일의 경우 안드로이드가 임의로 지워버릴 수 있음.
    fun accessCacheFile(): File {
        val filename = "temp.jpg"
        val cacheFile = File(context.cacheDir, filename)
        return cacheFile
    }

    // 캐시 파일 제거하기
    // 안드로이드가 캐시 파일을 제거를 보장하지는 않음. 적절한 처리할 것
    fun removeCacheFile() {
        val cacheFile = accessCacheFile()
        // case 1
        cacheFile.delete()
        // case 2
        context.deleteFile(cacheFile.name)
    }
}

// 앱 특화된 external 저장소로, API 19 이상부턴 별다른 권한 없이 접근이 가능하다.
// 앱을 제거할 때 함께 제거된다.
// 항상 접근이 보장되는게 아니기 때문에, 앱의 메인 기능을 이 파일에 의존하면 안된다.

// API 28 이하에서는 적절한 권한만 있으면, 다른 앱의 external 영역에 접근할 수 있다.
// API 29 이상에서는 scoped storage로 관리하면 된다, scoped access를 허용하면, 다른 앱의 external 파일에 접근 못함.
class ExternalFileStorageUseCase {

    private lateinit var context: Context

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
    fun selectPhysicalStorageLocation(): File {
        val externalStorageVolumes: Array<out File> =
            ContextCompat.getExternalFilesDirs(context, null)
        val primaryExternalStorage = externalStorageVolumes[0]
        return primaryExternalStorage
    }

    // app-specific 외부 저장소에 파일 접근하기
    fun accessFile(): File {
        val externalDir = context.getExternalFilesDir(null)
        val filename = "sample.txt"
        val file = File(externalDir, filename)
        return file
    }

    // API 30 이상부터 앱에서 external storage에 본인 소유 directory 못만든다.

    fun createCacheFile(): File? {
        if (isExternalStorageWritable()) {
            val externalCacheDir = context.externalCacheDir
            val filename = "temp.jpg"
            val file = File(externalCacheDir, filename)
            return file
        } else {
            return null
        }
    }
}