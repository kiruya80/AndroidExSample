package com.ulling.androidexsample.ui.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.ulling.androidexsample.SubActivity
import com.ulling.lib.core.utils.QcLog

class HomeLifecycleObserver(
//    val registry: ActivityResultRegistry,
    val activity: FragmentActivity,
    val homeViewModel: HomeViewModel
) :   DefaultLifecycleObserver {

    lateinit var registry: ActivityResultRegistry

    lateinit var startAct: ActivityResultLauncher<Intent>

    lateinit var sampleLauncher: ActivityResultLauncher<Int>

    override fun onCreate(owner: LifecycleOwner) {
        QcLog.e("onCreate == ")
        registry = activity.activityResultRegistry

//        getContent = registry.register("getContent", owner, ActivityResultContracts.GetContent()) { uri ->
//            // Handle the returned Uri
//        }

        startAct =
            registry.register("startAct", owner, ActivityResultContracts.StartActivityForResult()) {
                QcLog.e("registerForActivityResult == " + it.toString())
                if (it.resultCode == Activity.RESULT_OK) {
                    val intent = it.data
                    val data = intent?.getStringExtra("ResultData")
                    QcLog.e("activity response data == " + data.toString())

                homeViewModel.text.value = "activity response data : " + data.toString()
                }
            }

        // sample 1
        sampleLauncher =
            registry.register("launcher", owner, SampleActivityContract()) { result: String? ->
                result?.let {
                    QcLog.e("SampleActivityContract = $it")
                }
            }

    }


//    fun selectImage() {
//        getContent.launch("image/*")
//    }

//    GetContent : 사용자가 선택한 콘텐트의 Uri를 반환한다.
//    GetMultipleContent : 사용자가 선택한 1개 이상의 콘텐츠들이 List<Uri>형태로 반환된다.
//    TakePicturePreview : 사진을 찍고 Bitmap을 반환한다.
//    TakePicture : 촬영한 사진을 지정한 경로에 저장하고 Bitmap을 반환한다.
//    TakeVideo : 촬영한 비디오를 지정한 경로에 저장하고 썸네일을 Bitmap으로 반환한다.
//    CreateDocument : 새로운 문서 작성하고 해당 경로를 Uri형태로 반환한다.
//    OpenDocument : 사용자가 선택한 문서의 Uri를 반환한다.
//    OpenMultipleDocuments : 사용자가 선택한 1개 이상의 문서들이 List<Uri> 형태로 반환된다.
//    OpenDocumentTree : 사용자가 선택한 디렉토리의 Uri를 반환한다.
//    PickContact : 사용자가 선택한 연락처의 Uri를 반환한다.
//    RequestPermission : 단일 권한을 요청하고, 승인 여부를 반환한다.
//    RequestMultiplePermissions : 다중 권한을 요청하고, 승인 여부를 Map<String,Boolean>형태로 반환한다.
//    StartActivityForResult : 요청한 인텐트를 통해 액티비티를 실행하고, 액티비티 결과를 ActivityResult로 래핑하여 반환한다.
// sample 1
    class SampleActivityContract : ActivityResultContract<Int, String?>() {
        override fun createIntent(context: Context, input: Int): Intent {
            return   Intent(context, SubActivity::class.java).putExtra(
                "mainSendData",
                "send"
            )
        }

        override fun parseResult(resultCode: Int, intent: Intent?): String? {
            return when (resultCode) {
                Activity.RESULT_OK -> intent?.getStringExtra("result")
                else -> null
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