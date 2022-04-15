package com.ulling.androidexsample.base

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.ulling.lib.core.utils.QcLog

abstract class BaseFragment(contentLayoutId: Int) : Fragment(contentLayoutId) {

    private lateinit var mCallbackBackPressed: OnBackPressedCallback
    protected lateinit var mCtx: Context
    override fun onPause() {
        super.onPause()
        QcLog.e("onPause == ")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        QcLog.e("onDestroyView == ")
    }

    override fun onDestroy() {
        super.onDestroy()
        QcLog.e("onDestroy == ")
    }

    override fun onStart() {
        super.onStart()
        QcLog.e("onStart == ")
    }

    override fun onResume() {
        super.onResume()
        QcLog.e("onResume == ")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        QcLog.e("onViewCreated == ")
        initView()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        QcLog.e("onAttach == ")
        mCtx = context
        init()

//        /** onBackPressed() 이벤트 처리 */
//        mCallbackBackPressed = object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                QcLog.e("handleOnBackPressed ======== ")
//                onBackStackChanged()
//            }
//        }
//
//        // 프레그먼트에서 onBackPressed() 이벤트에 대해 핸들링하기 위해 등록
//        requireActivity().onBackPressedDispatcher.addCallback(this, mCallbackBackPressed)
    }

//    override fun onDetach() {
//        super.onDetach()
//        mCallbackBackPressed.remove()
//    }

    /** 초기화 */
    abstract fun init()
    abstract fun initView()

    /** onBackPressed() 이벤트 */
//    abstract fun onBackPressed()

//    abstract fun onBackStackChanged()
}