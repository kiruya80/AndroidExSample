package com.ulling.androidexsample

import android.app.Activity
import android.content.Intent
import com.ulling.androidexsample.base.BaseActivity
import com.ulling.androidexsample.component.clieckevent.setOnHasTermClickListener
import com.ulling.lib.core.utils.QcLog
import kotlinx.android.synthetic.main.activity_sub.*

class MainSubActivity : BaseActivity(R.layout.activity_main_sub) {

    override fun init() {
        QcLog.e("init ======== ")


        btn_finish.setOnHasTermClickListener {
            QcLog.e("btn_finish === ")
            val intent = Intent()
            intent.putExtra("ResultData", edt_send_data.text.toString())
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    //    override fun onBackStackChanged() {
//        setResult(Activity.RESULT_CANCELED)
//        finish()
//    }
}