package com.ulling.androidexsample.ui.permission

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ulling.androidexsample.R
import com.ulling.androidexsample.base.BaseFragment
import com.ulling.androidexsample.component.clieckevent.setOnHasTermClickListener
import com.ulling.lib.core.utils.QcLog
import kotlinx.android.synthetic.main.fragment_permission.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts

import android.os.Build.VERSION.SDK_INT
import com.ulling.androidexsample.common.permissionList
import com.ulling.androidexsample.common.permissionListAfterR
import com.ulling.androidexsample.common.permissionListRW
import com.ulling.androidexsample.utils.PermissionUtils.Companion.isAllPermissionGranted
import com.ulling.androidexsample.utils.PermissionUtils.Companion.isReadWritePermission
import com.ulling.androidexsample.utils.PermissionUtils.Companion.showDialogToGetPermission


/**
 * 권한 체크
 * ㄴ 안드로이드 Q , registerForActivityResult
 *
 */
class PermissionFragment : BaseFragment(R.layout.fragment_permission) {

    private lateinit var permissionViewModel: PermissionViewModel

    override fun onBackPressed() {
    }

    override fun onBackStackChanged() {
    }

    override fun init() {
        QcLog.e("init ======== ")
        permissionViewModel =
            ViewModelProvider(this).get(PermissionViewModel::class.java)
        permissionViewModel.text.observe(viewLifecycleOwner, Observer {
            text_permission.text = it
        })

        btn_permission_camera.setOnHasTermClickListener {
            QcLog.e("btn_permission_camera === ")
            //단일 권한을 요청하려면 RequestPermission을 사용합니다.
            //여러 권한을 동시에 요청하려면 RequestMultiplePermissions를 사용합니다.
            if (checkSelfPermission(
                    mCtx,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                permissionViewModel.text.value = Build.VERSION.SDK + " 카메라 권한 설정 완료"
            } else {
                // 권한 없는 경우, 권한 요청
                permissionLauncher.launch(Manifest.permission.CAMERA);
            }
        }

        btn_permission_multi.setOnHasTermClickListener {
            QcLog.e("btn_permission_multi === ")
            //단일 권한을 요청하려면 RequestPermission을 사용합니다.
            //여러 권한을 동시에 요청하려면 RequestMultiplePermissions를 사용합니다.
            var requestPermissions = permissionList
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                requestPermissions = permissionListAfterR;
            }

            if (isAllPermissionGranted(mCtx, requestPermissions)) {
                permissionViewModel.text.value = Build.VERSION.SDK + " 권한 설정 완료"
            }
            permissionMultiLauncher.launch(requestPermissions);

        }
        btn_permission_read_write.setOnHasTermClickListener {
            QcLog.e("btn_permission_read_write === ")
            if (!isReadWritePermission(mCtx, permissionListRW)) {
                requestReadWritePermission()
            }
        }
    }




    private fun requestReadWritePermission() {
        if (SDK_INT >= Build.VERSION_CODES.R) {
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
            if (isReadWritePermission(mCtx, permissionListRW)) {
                QcLog.e("모든 파일에 접근 권한 허용")
            }
        }

    val startForResultReadWrite =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            QcLog.e("ActivityResult ==== " + result.resultCode + " , " + result.toString())
//            if (result.resultCode == Activity.RESULT_OK) {
//            }
            if (isReadWritePermission(mCtx, permissionListRW)) {
                QcLog.e("모든 파일에 접근 권한 허용")
            }
        }

    /**
     * 단일 권한
     */
    val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        QcLog.e("RequestPermission === $isGranted ")
        if (isGranted) {
            permissionViewModel.text.value = Build.VERSION.SDK + "권한 설정 완료"
        } else {
            // shouldShowRequestPermissionRationale
            // 사용자가 권한 요청을 명시적으로 거부한 경우 true를 반환한다.
            // 사용자가 권한 요청을 처음 보거나, 다시 묻지 않음 선택한 경우, 권한을 허용한 경우 false를 반환한다.
            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                QcLog.e("거부 한 번 했을경우 재요청 가능")
            } else {
                QcLog.e("거부 두 번 했을경우 설정 화면으로 보내기")
                showDialogToGetPermission(mCtx, startForResultPermission)
            }
//                when {
//                    ContextCompat.checkSelfPermission(
//                        CONTEXT,
//                        Manifest.permission.REQUESTED_PERMISSION
//                    ) == PackageManager.PERMISSION_GRANTED -> {
//                        // You can use the API that requires the permission.
//                    }
//                    shouldShowRequestPermissionRationale(...) -> {
//                    // In an educational UI, explain to the user why your app requires this
//                    // permission for a specific feature to behave as expected. In this UI,
//                    // include a "cancel" or "no thanks" button that allows the user to
//                    // continue using your app without granting the permission.
//                    showInContextUI(...)
//                }
//                    else -> {
//                        // You can directly ask for the permission.
//                        // The registered ActivityResultCallback gets the result of this request.
//                        requestPermissionLauncher.launch(
//                            Manifest.permission.REQUESTED_PERMISSION)
//                    }
//                }

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