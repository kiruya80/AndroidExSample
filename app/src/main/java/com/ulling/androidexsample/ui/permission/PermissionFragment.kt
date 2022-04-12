package com.ulling.androidexsample.ui.permission

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ulling.androidexsample.R
import com.ulling.androidexsample.base.BaseFragment
import com.ulling.androidexsample.component.clieckevent.setOnHasTermClickListener
import com.ulling.lib.core.utils.QcLog
import kotlinx.android.synthetic.main.fragment_permission.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

import android.os.Build.VERSION.SDK_INT


/**
 * 권한 체크
 * ㄴ 안드로이드 Q , registerForActivityResult
 *
 */
class PermissionFragment : BaseFragment(R.layout.fragment_permission) {

    private lateinit var permissionViewModel: PermissionViewModel

    // 단일 권한
    private lateinit var permissionLauncher: ActivityResultLauncher<String>

    //  멀티 권한
    private lateinit var permissionMultiLauncher: ActivityResultLauncher<Array<String>>

    var permissionList: Array<String> = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
//        Manifest.permission.ACCESS_NETWORK_STATE,
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO
    )

    var permissionListAfterR: Array<String> = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
//        Manifest.permission.ACCESS_NETWORK_STATE,
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO
    )

    var permissionListRW: Array<String> = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
    )

    override fun onBackPressed() {
    }

    override fun onBackStackChanged() {
    }

    private fun showDialogToGetPermission() {
        val builder = AlertDialog.Builder(mCtx)
        builder.setTitle("동의하지 않은 권한이 있습니다. ")
            .setMessage("모든 권한 동의 후 사용가능합니다.")

        builder.setPositiveButton("설정") { dialogInterface, i ->
//            val intent = Intent(
//                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
//                Uri.fromParts("package", mCtx.packageName, null)
//            )
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//            startActivity(intent)

//            startForResult.launch(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
//                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                data = Uri.parse(String.format("package:%s", mCtx.packageName))
//            }, )


            startForResultPermission.launch(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                data = Uri.parse(String.format("package:%s", mCtx.packageName))
            }, )
        }
        builder.setNegativeButton("종료") { dialogInterface, i ->

        }
        val dialog = builder.create()
        dialog.show()
    }

    val startForResultPermission =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            QcLog.e("ActivityResult ==== " + result.resultCode + " , " + result.toString())
//            if (result.resultCode == Activity.RESULT_OK) {
//            }
            if (checkReadWritePermission()) {
                QcLog.e("모든 파일에 접근 권한 허용")
            }
        }


    override fun init() {
        QcLog.e("init ======== ")
        permissionViewModel =
            ViewModelProvider(this).get(PermissionViewModel::class.java)
        permissionViewModel.text.observe(viewLifecycleOwner, Observer {
            text_permission.text = it
        })

        permissionLauncher = registerForActivityResult(
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
                    showDialogToGetPermission()
//                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
//                    intent.data = Uri.parse("package:" + activity?.packageName)
//                    activity?.startActivity(intent)
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
         * launch 실행시
         * ㄴ 거부 * 한번은 선택창이 나오지만, 두번하는 경우 바로 들어온다
         *
         */
        permissionMultiLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { map ->
            QcLog.e("RequestMultiplePermissions ===  " + map.toString())

            if (!map.isNullOrEmpty()) {
                var isMoveSetting = false
                for (entry in map.entries) {
                    // android.permission.READ_EXTERNAL_STORAGE = true
                    QcLog.e("${entry.key} = ${entry.value}")
//                    QcLog.e("shouldShowRequestPermissionRationale = " + shouldShowRequestPermissionRationale(entry.key))

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
                    showDialogToGetPermission()
                }
            }
        }

        btn_permission_camera.setOnHasTermClickListener {
            QcLog.e("btn_permission_camera === ")
            //단일 권한을 요청하려면 RequestPermission을 사용합니다.
            //여러 권한을 동시에 요청하려면 RequestMultiplePermissions를 사용합니다.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
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

            } else {
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
        }

        btn_permission_multi.setOnHasTermClickListener {
            QcLog.e("btn_permission_multi === ")
            //단일 권한을 요청하려면 RequestPermission을 사용합니다.
            //여러 권한을 동시에 요청하려면 RequestMultiplePermissions를 사용합니다.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (isAllPermissionGranted(permissionListAfterR)) {
                    permissionViewModel.text.value = Build.VERSION.SDK + " 권한 설정 완료"
                } else {
//                    requestPermissions(permissionListAfterR, PERMISSION_CHECK)
                    // 권한 없는 경우, 권한 요청
                    permissionMultiLauncher.launch(permissionListAfterR);
                }

            } else {
                if (isAllPermissionGranted(permissionList)) {
                    permissionViewModel.text.value = Build.VERSION.SDK + " 권한 설정 완료"
                } else {
                    // RequestMultiplePermissions, RequestPermission
//                    requestPermissions(permissionList, PERMISSION_CHECK)
                    permissionMultiLauncher.launch(permissionList);
                }
            }
        }
        btn_permission_read_write.setOnHasTermClickListener {
            QcLog.e("btn_permission_read_write === " + checkReadWritePermission())
            if (!checkReadWritePermission()) {
//                if (!isAllPermissionGranted(permissionListAfterR)) {
                requestReadWritePermission()
            }
        }

    }


    fun isAllPermissionGranted(requestPermissions: Array<out String>): Boolean {
        for (permission in requestPermissions) {
            // checkSelfPermission
            // 특정 권한이 PackageManager.PERMISSION_DENIED 인지, PackageManager.PERMISSION_GRANTED 인지 반환 한다.
            if (checkSelfPermission(mCtx, permission) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    private fun checkReadWritePermission(): Boolean {
        return if (SDK_INT >= Build.VERSION_CODES.R) {
            Environment.isExternalStorageManager()
        } else {
            isAllPermissionGranted(permissionListRW)
        }
    }

    private fun requestReadWritePermission() {
        if (SDK_INT >= Build.VERSION_CODES.R) {
            try {
                startForResult.launch(Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION).apply {
                    addCategory("android.intent.category.DEFAULT")
                    data = Uri.parse(String.format("package:%s", mCtx.packageName))
                })
//                            startActivityForResult(Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION).apply {
//                                addCategory("android.intent.category.DEFAULT")
//                                data = Uri.parse(String.format("package:%s", mCtx.packageName))
//                            }, 300)
            } catch (e: Exception) {
                startForResult.launch(
                    Intent().apply {
                        action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                    },
                )
//                            startActivityForResult(Intent().apply {
//                                action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
//                            }, 300)
            }
        } else {
            //below android 11
            permissionMultiLauncher.launch(permissionListRW);
        }

    }


    val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            QcLog.e("ActivityResult ==== " + result.resultCode + " , " + result.toString())
//            if (result.resultCode == Activity.RESULT_OK) {
//            }
            if (checkReadWritePermission()) {
                QcLog.e("모든 파일에 접근 권한 허용")
            }
        }




//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        QcLog.e("onRequestPermissionsResult == ")
//        when (requestCode) {
//            PERMISSION_CHECK -> {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//                    if (!Environment.isExternalStorageManager()) {
//                        moveReadPermission()
//
//                    }
//                } else {
//                    if ((grantResults.isNotEmpty() &&
//                                grantResults[0] == PackageManager.PERMISSION_GRANTED)
//                    ) {
//                        QcToast.getInstance().show("권한 추가 완료")
//                        permissionViewModel.text.value = Build.VERSION.SDK + "권한 설정 완료"
//                    } else {
//                        // Explain to the user that the feature is unavailable because
//                        // the features requires a permission that the user has denied.
//                        // At the same time, respect the user's decision. Don't link to
//                        // system settings in an effort to convince the user to change
//                        // their decision.
//                    }
//                }
//            }
//        }
//    }
}