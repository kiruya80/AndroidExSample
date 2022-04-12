package com.ulling.androidexsample.base

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment

abstract class BaseFragment(contentLayoutId: Int) : Fragment(contentLayoutId) {

    private lateinit var mCallbackBackPressed: OnBackPressedCallback
    protected lateinit var mCtx: Context

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mCtx = context

        /** onBackPressed() 이벤트 처리 */
        mCallbackBackPressed = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPressed()
            }
        }
        // 프레그먼트에서 onBackPressed() 이벤트에 대해 핸들링하기 위해 등록
        requireActivity().onBackPressedDispatcher.addCallback(this, mCallbackBackPressed)
    }

    override fun onDetach() {

        super.onDetach()
        mCallbackBackPressed.remove()
    }

    /** 초기화 */
    abstract fun init()

    /** onBackPressed() 이벤트 */
    abstract fun onBackPressed()

    abstract fun onBackStackChanged()
}