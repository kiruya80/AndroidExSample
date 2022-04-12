package com.ulling.androidexsample.ui.storage

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ulling.androidexsample.R
import com.ulling.androidexsample.base.BaseFragment
import com.ulling.androidexsample.common.permissionListRW
import com.ulling.androidexsample.component.clieckevent.setOnHasTermClickListener
import com.ulling.androidexsample.utils.PermissionUtils
import com.ulling.androidexsample.utils.PermissionUtils.Companion.showDialogToGetPermission
import com.ulling.lib.core.utils.QcLog
import kotlinx.android.synthetic.main.fragment_storage.*
import kotlinx.android.synthetic.main.fragment_storage.btn_permission_read_write

class StorageFragment : BaseFragment(R.layout.fragment_storage) {

    private lateinit var storageViewModel: StorageViewModel

    override fun onBackPressed() {
    }

    override fun onBackStackChanged() {
    }


    override fun init() {
        QcLog.e("init ======== ")

        storageViewModel =
            ViewModelProvider(this).get(StorageViewModel::class.java)
        storageViewModel.text.observe(viewLifecycleOwner, Observer {
            text_storage.text = it
        })
        text_storage.text = "text_storage"

        btn_permission_read_write.setOnHasTermClickListener {
            QcLog.e("btn_permission_read_write === " )
            if (!PermissionUtils.isReadWritePermission(mCtx, permissionListRW)) {
//                if (!isAllPermissionGranted(permissionListAfterR)) {
                requestReadWritePermission()
            }
        }
    }



    private fun requestReadWritePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                startForResultReadWrite.launch(Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION).apply {
                    addCategory("android.intent.category.DEFAULT")
                    data = Uri.parse(String.format("package:%s", mCtx.packageName))
                })
            } catch (e: Exception) {
                startForResultReadWrite.launch(
                    Intent().apply {
                        action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                    },
                )
            }
        } else {
            //below android 11
            permissionMultiLauncher.launch(permissionListRW);
        }
    }

    val startForResultPermission =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            QcLog.e("ActivityResult ==== " + result.resultCode + " , " + result.toString())
//            if (result.resultCode == Activity.RESULT_OK) {
//            }
            if (PermissionUtils.isReadWritePermission(mCtx, permissionListRW)) {
                QcLog.e("모든 파일에 접근 권한 허용")
            }
        }

    val startForResultReadWrite =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            QcLog.e("ActivityResult ==== " + result.resultCode + " , " + result.toString())
//            if (result.resultCode == Activity.RESULT_OK) {
//            }
            if (PermissionUtils.isReadWritePermission(mCtx, permissionListRW)) {
                QcLog.e("모든 파일에 접근 권한 허용")
            }
        }


    /**
     * 멀티 권한
     *
     * launch 실행시
     * ㄴ 거부 * 한번은 선택창이 나오지만, 두번하는 경우 바로 들어온다
     *
     */
    val permissionMultiLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { map ->
        QcLog.e("RequestMultiplePermissions ===  " + map.toString())

        if (!map.isNullOrEmpty()) {
            var isMoveSetting = false
            for (entry in map.entries) {
                // android.permission.READ_EXTERNAL_STORAGE = true
                QcLog.e("${entry.key} = ${entry.value}")

                // shouldShowRequestPermissionRationale
                // 사용자가 권한 요청을 명시적으로 거부한 경우 true를 반환한다.
                // 사용자가 권한 요청을 처음 보거나, 다시 묻지 않음 선택한 경우, 권한을 허용한 경우 false를 반환한다.
                if (shouldShowRequestPermissionRationale(entry.key)) {
                    QcLog.e("거부 한 번 했을경우 재요청 가능 : " + entry.key)
                } else {
                    QcLog.e("거부 두 번 했을경우 설정 화면으로 보내기 : " + entry.key)
                    isMoveSetting = true
                    break
                }
            }
            if (isMoveSetting) {
                showDialogToGetPermission(mCtx, startForResultPermission)
            }
        }
    }
}