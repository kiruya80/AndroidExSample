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
import com.ulling.androidexsample.ui.permission.PermissionLifecycleObserver
import com.ulling.androidexsample.utils.PermissionUtils
import com.ulling.androidexsample.utils.PermissionUtils.Companion.showDialogToGetPermission
import com.ulling.lib.core.utils.QcLog
import kotlinx.android.synthetic.main.fragment_storage.*
import kotlinx.android.synthetic.main.fragment_storage.btn_permission_read_write

class StorageFragment : BaseFragment(R.layout.fragment_storage) {

    private lateinit var storageViewModel: StorageViewModel
    lateinit var observer: StorageLifecycleObserver

    override fun init() {
        QcLog.e("init ======== ")

        storageViewModel =
            ViewModelProvider(this).get(StorageViewModel::class.java)


        observer = StorageLifecycleObserver(
            requireActivity(),
            storageViewModel
        )
        lifecycle.addObserver(observer)
    }

    override fun initView() {
        QcLog.e("initView ======== ")
        storageViewModel.text.observe(viewLifecycleOwner, Observer {
            text_storage.text = it
        })
        text_storage.text = "text_storage"

        btn_permission_read_write.setOnHasTermClickListener {
            QcLog.e("btn_permission_read_write === ")
            if (!PermissionUtils.isReadWritePermission(mCtx, permissionListRW)) {
                PermissionUtils.requestReadWritePermission(
                    mCtx,
                    startForResultReadWrite,
                    permissionMultiLauncher
                )
            }
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
        if (!map.isNullOrEmpty()) {
            val permissionShowList: ArrayList<String> = ArrayList<String>()

            for (entry in map.entries) {
                QcLog.e(
                    " ======= ${entry.key} = ${entry.value}     "
                            + " , isGranted : " + PermissionUtils.isCheckSelfPermission(
                        mCtx,
                        entry.key
                    )
                            + " , 권한 요청 : " + shouldShowRequestPermissionRationale(entry.key)
                )

                if (PermissionUtils.isCheckSelfPermission(mCtx, entry.key)) {
                    QcLog.e("허용된 권한 ${entry.key} ${entry.value}")

                } else {
                    if (shouldShowRequestPermissionRationale(entry.key)) {
                        // 이전 요청에 거부한 경우
                        // 사용자가 권한 요청을 명시적으로 거부한 경우 true를 반환한다.
                        QcLog.e("최초 거부 클릭시 : " + entry.key)
                    } else {
                        // 팝업 배경 선택 취소시
                        // 사용자가 권한 요청을 처음 보거나, 다시 묻지 않음 선택한 경우, 권한을 허용한 경우 false를 반환한다.
                        QcLog.e("이전 요청에 거부한 경우, 설정화면 보내기 팝업 노출 : " + entry.key)
                        // 권한 버튼 클릭을 해도 반응이 없을 수 있다 이럴때 설정화면으로 보내야한다
                        permissionShowList.add(entry.key)
                    }
                }

            }
            if (!permissionShowList.isNullOrEmpty()) {
                showDialogToGetPermission(
                    mCtx,
                    permissionShowList.toString(),
                    startForResultPermission
                )
            }
        }
    }
}