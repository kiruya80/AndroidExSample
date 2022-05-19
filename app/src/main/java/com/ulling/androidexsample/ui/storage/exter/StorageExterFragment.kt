package com.ulling.androidexsample.ui.storage.exter

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ulling.androidexsample.R
import com.ulling.androidexsample.base.BaseFragment
import com.ulling.androidexsample.component.clieckevent.setOnHasTermClickListener
import com.ulling.androidexsample.utils.ExtStorageUtils
import com.ulling.lib.core.utils.QcLog
import com.ulling.lib.core.utils.QcToast
import kotlinx.android.synthetic.main.fragment_storage_exter.*
import kotlinx.android.synthetic.main.fragment_storage_exter.fab
import kotlinx.android.synthetic.main.fragment_storage_exter.text_title
import kotlinx.android.synthetic.main.fragment_storage_inter.*
import kotlin.random.Random

/**
 * 외부 앱 저장소
 *
 * https://developer.android.com/training/data-storage/app-specific?hl=ko
 *
 */
class StorageExterFragment : BaseFragment(R.layout.fragment_storage_exter) {

    private lateinit var storageViewModel: StorageExterViewModel
    lateinit var observerInter: StorageExterLifecycleObserver

    var fileDir = "sampleDir"
    var fileName = "myfile.txt"
    var cacheFile = "cacheFileText.txt"
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

        fab.setOnClickListener {
            findNavController().navigate(R.id.action_move_storage_share)
        }
        btn_permission_exter.setOnHasTermClickListener {
            QcLog.e("btn_permission_exter === ")
            val isExternalStorageWritable = ExtStorageUtils(mCtx).isExternalStorageWritable()
            val isExternalStorageReadable = ExtStorageUtils(mCtx).isExternalStorageReadable()
            QcToast.getInstance()
                .show("쓰기 가능 : $isExternalStorageWritable, 읽기 가능 : $isExternalStorageReadable")
        }

        btn_ext_selected.setOnHasTermClickListener {
            QcLog.e("btn_ext_selected === ")
            val slectedStore = ExtStorageUtils(mCtx).selectPhysicalStorageLocation()
            text_ext_selected.text = slectedStore.toString()
        }

        btn_ext_make_file.setOnHasTermClickListener {
            QcLog.e("btn_ext_make_file === ")
            fileName = "testFile_" + edt_ext_make_file.text.toString() + Random.nextInt(1000)
                .toString() + ".txt"
            val file = ExtStorageUtils(mCtx).accessFile(filename = fileName)
            QcLog.e("accessFile ===  $file")
        }

        btn_ext_make_dir.setOnHasTermClickListener {
            QcLog.e("btn_ext_make_dir === ")
            val dirName =
                "testFolder_" + edt_ext_make_dir.text.toString() + Random.nextInt(1000).toString()

            ExtStorageUtils(mCtx).createDir(dirName)
        }

        btn_ext_save_file.setOnHasTermClickListener {
            QcLog.e("btn_ext_save_file === ")

            fileName = "testFile_" + Random.nextInt(1000).toString() + ".txt"
            fileContents = fileName + "\n" + edt_ext_save_file.text.toString()

            val file = ExtStorageUtils(mCtx).accessFile(filename = fileName)
            val result = ExtStorageUtils(mCtx).writeTextToFile(file.path, fileContents)
            QcLog.e("writeTextToFile ===  $result")
        }

        btn_ext_get_file.setOnHasTermClickListener {
            QcLog.e("btn_ext_get_file === ")
            val file = ExtStorageUtils(mCtx).accessFile(filename = fileName)
            val result = ExtStorageUtils(mCtx).readTextFromFile(file.path)
            QcLog.e("readTextFromFile ===  $result")
            text_ext_get_file.text = result
        }

        btn_ext_get_files.setOnHasTermClickListener {
            QcLog.e("btn_ext_get_files === ")
            val result = ExtStorageUtils(mCtx).getFileListFromDir()
            QcLog.e("getDir ===  $result")
            text_ext_get_files.text = result.toString()
        }

        btn_ext_delete_file.setOnHasTermClickListener {
            QcLog.e("btn_ext_delete_file === ")
            val result = ExtStorageUtils(mCtx).deleteFileList()
            QcLog.e("deleteFileList ===  $result")
            text_ext_delete_file.text = result.toString()
        }

        btn_ext_cache.setOnHasTermClickListener {
            QcLog.e("btn_ext__cache === ")
            val cache = ExtStorageUtils(mCtx).createCacheFile(cacheFile)
            QcLog.e("cache create === $cache")
            text_ext_cache.text = cache.toString()
        }
        btn_ext_cache_delete.setOnHasTermClickListener {
            QcLog.e("btn_ext__cache_delete === ")
            val cache = ExtStorageUtils(mCtx).deleteCacheFile(cacheFile)
            QcLog.e("cache delete === $cache")
            text_ext_cache_delete.text = cache.toString()
        }
    }
}