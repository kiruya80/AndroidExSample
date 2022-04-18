package com.ulling.androidexsample.ui.storage.share

import android.Manifest
import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.ulling.androidexsample.common.permissionListRW
import com.ulling.androidexsample.utils.PermissionUtils
import com.ulling.lib.core.utils.QcLog
import com.ulling.lib.core.utils.QcToast

class StorageShareLifecycleObserver(
//    val registry: ActivityResultRegistry,
    val activity: FragmentActivity,
    val viewModel: StorageShareViewModel
) :   DefaultLifecycleObserver {

    lateinit var registry: ActivityResultRegistry

    lateinit var actResultPermission: ActivityResultLauncher<String>
    lateinit var actResultMultiPermissions: ActivityResultLauncher<Array<String>>
    lateinit var actResultStartActivity: ActivityResultLauncher<Intent>

    override fun onCreate(owner: LifecycleOwner) {
        QcLog.e("onCreate == ")

        registry = activity.activityResultRegistry

        actResultPermission =
            registry.register(
                "actResultPermission", owner,
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                QcLog.e("RequestPermission === $isGranted ")
                var permission = Manifest.permission.CAMERA

                if (isGranted) {
//                    viewModel.text.value = Build.VERSION.SDK + "권한 설정 완료"
                } else {
                    QcLog.e(
                        " ======= Manifest.permission.CAMERA " + shouldShowRequestPermissionRationale(
                            activity, permission
                        )
                    )
                    if (shouldShowRequestPermissionRationale(activity, permission)) {
                        QcLog.e("최초 거부 클릭시 : " + permission)
                    } else {
                        PermissionUtils.showDialogToGetPermission(
                            activity,
                            permission,
                            actResultStartActivity
                        )
                    }
                }
            }
        actResultMultiPermissions =
            registry.register(
                "actResultMultiPermissions", owner,
                ActivityResultContracts.RequestMultiplePermissions()
            ) { map ->
                if (!map.isNullOrEmpty()) {
                    val permissionShowList: ArrayList<String> = ArrayList<String>()

                    for (entry in map.entries) {
                        QcLog.e(
                            " ======= ${entry.key} = ${entry.value}     "
                                    + " , isGranted : " + PermissionUtils.isCheckSelfPermission(
                                activity,
                                entry.key
                            )
                                    + " , 권한 요청 : " + shouldShowRequestPermissionRationale(
                                activity,
                                entry.key
                            )
                        )

                        if (PermissionUtils.isCheckSelfPermission(activity, entry.key)) {
                            QcLog.e("허용된 권한 ${entry.key} ${entry.value}")

                        } else {
                            if (shouldShowRequestPermissionRationale(activity, entry.key)) {
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
                        PermissionUtils.showDialogToGetPermission(
                            activity,
                            permissionShowList.toString(),
                            actResultStartActivity
                        )
                    } else {
                        QcToast.getInstance().show("모든 권한 허용 완료")
                    }
                }
            }

        actResultStartActivity =
            registry.register(
                "actResultStartActivity", owner,
                ActivityResultContracts.StartActivityForResult()
            ) { result: ActivityResult ->
                QcLog.e("ActivityResult ==== " + result.resultCode + " , " + result.toString())
                if (result.resultCode == Activity.RESULT_OK) {

                } else {

                }

                if (PermissionUtils.isReadWritePermission(activity, permissionListRW)) {
                    QcLog.e("모든 파일에 접근 권한 허용")
                }
            }

    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        QcLog.e("onStart == ")
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        QcLog.e("onResume == ")
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        QcLog.e("onPause == ")
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        QcLog.e("onStop == ")
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        QcLog.e("onDestroy == ")
    }
}