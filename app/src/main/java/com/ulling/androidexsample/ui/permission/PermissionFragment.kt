package com.ulling.androidexsample.ui.permission

import android.Manifest
import android.app.Activity
import android.os.Build
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ulling.androidexsample.R
import com.ulling.androidexsample.base.BaseFragment
import com.ulling.androidexsample.component.clieckevent.setOnHasTermClickListener
import com.ulling.lib.core.utils.QcLog
import kotlinx.android.synthetic.main.fragment_permission.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.ulling.androidexsample.QUllingApplication

import com.ulling.androidexsample.common.permissionList
import com.ulling.androidexsample.common.permissionListAfterR
import com.ulling.androidexsample.common.permissionListRW
import com.ulling.androidexsample.utils.PermissionUtils
import com.ulling.androidexsample.utils.PermissionUtils.Companion.isAllCheckSelfPermission
import com.ulling.androidexsample.utils.PermissionUtils.Companion.isCheckSelfPermission
import com.ulling.androidexsample.utils.PermissionUtils.Companion.isReadWritePermission
import com.ulling.androidexsample.utils.PermissionUtils.Companion.showDialogToGetPermission
import com.ulling.lib.core.utils.QcDialogUtils
import com.ulling.lib.core.utils.QcToast
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_permission.fab


/**
 * 권한 체크
 * ㄴ 안드로이드 Q , registerForActivityResult
 *
 */
class PermissionFragment : BaseFragment(R.layout.fragment_permission) {

    private lateinit var permissionViewModel: PermissionViewModel
    lateinit var observer: PermissionLifecycleObserver

    override fun init() {
        QcLog.e("init ======== ")
//        permissionViewModel =
//            ViewModelProvider(this).get(PermissionViewModel::class.java)

//        permissionViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(
//            QUllingApplication.getInstance()
//        )).get(PermissionViewModel::class.java)

        val sampleParam = "Add Param"
        permissionViewModel = ViewModelProvider(
            this, PermissionParamViewModelFactory(
                QUllingApplication.getInstance(), sampleParam
            )
        ).get(PermissionViewModel::class.java)

        observer = PermissionLifecycleObserver(
            requireActivity(),
            permissionViewModel
        )
        lifecycle.addObserver(observer)
    }

    override fun initView() {
        QcLog.e("initView ======== ")
        permissionViewModel.text.observe(viewLifecycleOwner, Observer {
            text_permission.text = it
        })

        fab.setOnClickListener {
            findNavController().navigate(R.id.action_move_storage_inter)
        }
        btn_permission_camera.setOnHasTermClickListener {
            QcLog.e("btn_permission_camera === ")
            //단일 권한을 요청하려면 RequestPermission을 사용합니다.
            //여러 권한을 동시에 요청하려면 RequestMultiplePermissions를 사용합니다.
            if (isCheckSelfPermission(mCtx, Manifest.permission.CAMERA)) {
                permissionViewModel.text.value = Build.VERSION.SDK + " 카메라 권한 설정 완료"
            } else {
                // 권한 없는 경우, 권한 요청
                observer.actResultPermission.launch(Manifest.permission.CAMERA);
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

            if (isAllCheckSelfPermission(mCtx, requestPermissions)) {
                permissionViewModel.text.value = Build.VERSION.SDK + " 권한 설정 완료"
            }
            observer.actResultMultiPermissions.launch(requestPermissions);

        }
        btn_permission_read_write.setOnHasTermClickListener {
            QcLog.e("btn_permission_read_write === ")
            if (!isReadWritePermission(mCtx, permissionListRW)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    QcDialogUtils.getInstance().showDefaultDialog(mCtx,
                        "알림",
                        "모든 파일에 접근 허용 권한 허용",
                        false,
                        "설정",
                        "종료",
                        QcDialogUtils.DialogListener {
                            if (it) {
                                PermissionUtils.requestReadWritePermission(
                                    mCtx,
                                    observer.actResultStartActivity,
                                    observer.actResultMultiPermissions
                                )
                            }
                        }
                    )
                } else {
                    PermissionUtils.requestReadWritePermission(
                        mCtx,
                        observer.actResultStartActivity,
                        observer.actResultMultiPermissions
                    )
                }
            } else {
                QcToast.getInstance().show("접근 가능")
            }
        }
    }

//    /**
//     * 단일 권한
//     */
//    val actResultPermission = registerForActivityResult(
//        ActivityResultContracts.RequestPermission()
//    ) { isGranted: Boolean ->
//        QcLog.e("RequestPermission === $isGranted ")
//        var permission = Manifest.permission.CAMERA
//
//        if (isGranted) {
//            permissionViewModel.text.value = Build.VERSION.SDK + "권한 설정 완료"
//        } else {
//            QcLog.e(
//                " ======= Manifest.permission.CAMERA " + shouldShowRequestPermissionRationale(
//                    permission
//                )
//            )
//            if (shouldShowRequestPermissionRationale(permission)) {
//                QcLog.e("최초 거부 클릭시 : " + permission)
//            } else {
//                showDialogToGetPermission(mCtx, permission, actResultStartActivity)
//            }
//        }
//    }
//
//    /**
//     * 멀티 권한
//     *
//     * launch 실행시
//     * ㄴ 거부 * 한번은 선택창이 나오지만, 두번하는 경우 바로 들어온다
//     *
//     */
//    val actResultMultiPermissions = registerForActivityResult(
//        ActivityResultContracts.RequestMultiplePermissions()
//    ) { map ->
//        if (!map.isNullOrEmpty()) {
//            val permissionShowList: ArrayList<String> = ArrayList<String>()
//
//            for (entry in map.entries) {
//                QcLog.e(
//                    " ======= ${entry.key} = ${entry.value}     "
//                            + " , isGranted : " + isCheckSelfPermission(mCtx, entry.key)
//                            + " , 권한 요청 : " + shouldShowRequestPermissionRationale(entry.key)
//                )
//
//                if (isCheckSelfPermission(mCtx, entry.key)) {
//                    QcLog.e("허용된 권한 ${entry.key} ${entry.value}")
//
//                } else {
//                    if (shouldShowRequestPermissionRationale(entry.key)) {
//                        // 이전 요청에 거부한 경우
//                        // 사용자가 권한 요청을 명시적으로 거부한 경우 true를 반환한다.
//                        QcLog.e("최초 거부 클릭시 : " + entry.key)
//                    } else {
//                        // 팝업 배경 선택 취소시
//                        // 사용자가 권한 요청을 처음 보거나, 다시 묻지 않음 선택한 경우, 권한을 허용한 경우 false를 반환한다.
//                        QcLog.e("이전 요청에 거부한 경우, 설정화면 보내기 팝업 노출 : " + entry.key)
//                        // 권한 버튼 클릭을 해도 반응이 없을 수 있다 이럴때 설정화면으로 보내야한다
//                        permissionShowList.add(entry.key)
//                    }
//                }
//
//            }
//            if (!permissionShowList.isNullOrEmpty()) {
//                showDialogToGetPermission(
//                    mCtx,
//                    permissionShowList.toString(),
//                    actResultStartActivity
//                )
//            } else {
//                QcToast.getInstance().show("모든 권한 허용 완료")
//            }
//        }
//    }
//
//
//    val actResultStartActivity = registerForActivityResult(
//        ActivityResultContracts.StartActivityForResult()
//    ) { result: ActivityResult ->
//        QcLog.e("ActivityResult ==== " + result.resultCode + " , " + result.toString())
//        if (result.resultCode == Activity.RESULT_OK) {
//
//        } else {
//
//        }
//
//        if (isReadWritePermission(mCtx, permissionListRW)) {
//            QcLog.e("모든 파일에 접근 권한 허용")
//        }
//    }
}