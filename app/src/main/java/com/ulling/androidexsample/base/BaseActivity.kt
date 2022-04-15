package com.ulling.androidexsample.base

import android.content.Context
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.ulling.androidexsample.BuildConfig
import com.ulling.androidexsample.utils.Utils
import com.ulling.lib.core.utils.QcLog

abstract class BaseActivity(contentLayoutId: Int) : AppCompatActivity(contentLayoutId) {

    private lateinit var mCallbackBackPressed: OnBackPressedCallback
    protected lateinit var mContext: Context

    override fun onPause() {
        super.onPause()
        QcLog.e("onPause == ")
    }

    override fun onResume() {
        super.onResume()
        QcLog.e("onResume == ")
    }

    override fun onStart() {
        super.onStart()
        QcLog.e("onStart == ")

        mContext = this

//        /** onBackPressed() 이벤트 처리 */
//        mCallbackBackPressed = object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                onBackStackChanged()
//            }
//        }

        if (BuildConfig.DEBUG_MODE) Utils.printAllIntentParams(intent)
        init()
    }

//    override fun onDestroy() {
//        super.onDestroy()
//        mCallbackBackPressed.remove()
//    }


    /** 초기화 */
    abstract fun init()

    /** onBackPressed() 이벤트 */
//    abstract fun onBackPressed()

//    abstract fun onBackStackChanged()
}