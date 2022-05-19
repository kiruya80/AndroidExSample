package com.ulling.androidexsample.ui.storage.inter

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ulling.androidexsample.R
import com.ulling.androidexsample.base.BaseFragment
import com.ulling.androidexsample.component.clieckevent.setOnHasTermClickListener
import com.ulling.androidexsample.utils.InterStorageUtils
import com.ulling.lib.core.utils.QcLog
import kotlinx.android.synthetic.main.fragment_permission.*
import kotlinx.android.synthetic.main.fragment_storage_inter.*
import kotlinx.android.synthetic.main.fragment_storage_inter.fab
import kotlin.random.Random

/**
 * 내부 앱 저장소
 *
 * https://developer.android.com/training/data-storage/app-specific?hl=ko
 *
 */
class StorageInterFragment : BaseFragment(R.layout.fragment_storage_inter) {

    private lateinit var storageViewModel: StorageInterViewModel
    lateinit var observerInter: StorageInterLifecycleObserver

    var fileDir = "sampleDir"
    var fileName = "myfile"
    var fileContents = "Hello world!\n@world@ @world@ @world@"

    override fun init() {
        QcLog.e("init ======== ")

        storageViewModel =
            ViewModelProvider(this).get(StorageInterViewModel::class.java)

        observerInter = StorageInterLifecycleObserver(
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

        fab.setOnClickListener {
            findNavController().navigate(R.id.action_move_storage_exter)
        }
        btn_in_make_dir.setOnHasTermClickListener {
            QcLog.e("btn_in_make_dir === ")
            fileDir =
                "testFolder_" + edt_in_makeFile.text.toString() + Random.nextInt(1000).toString()
            InterStorageUtils(mCtx).createDir(fileDir)
        }

        btn_in_makeFile.setOnHasTermClickListener {
            QcLog.e("btn_in_makeFile === ")
            fileName =
                "testFile_" + edt_in_makeFile.text.toString() + Random.nextInt(1000).toString()
            InterStorageUtils(mCtx).accessFile(fileName)
        }

        btn_in_saveFile.setOnHasTermClickListener {
            QcLog.e("btn_in_saveFile === ")
            fileContents = fileName + "\n" + edt_in_saveFile.text.toString()
            InterStorageUtils(mCtx).saveFileUsingStream(fileName, fileContents)
        }

        btn_in_get_file.setOnHasTermClickListener {
            QcLog.e("btn_in_get_file === ")
            if (InterStorageUtils(mCtx).accessFile(fileName).exists()
                && InterStorageUtils(mCtx).accessFile(fileName).isFile
            ) {
                var content = InterStorageUtils(mCtx).getFileUsingStream(fileName)
                text_in_get_file.text = content
            } else {
                text_in_get_file.text = "파일이 없음"
            }
        }

        btn_in_deleteFile.setOnHasTermClickListener {
            QcLog.e("btn_in_deleteFile === ")
            InterStorageUtils(mCtx).deleteFile(fileName)
        }

        btn_in_getFileList.setOnHasTermClickListener {
            QcLog.e("btn_in_getFileList === ")
            val lineList = InterStorageUtils(mCtx).getFileList()
            text_in_getFileList.text = lineList.toString()
        }

        btn_in_deleteFileList.setOnHasTermClickListener {
            QcLog.e("btn_in_deleteFileList === ")
            InterStorageUtils(mCtx).deleteFileList()
        }
    }
}