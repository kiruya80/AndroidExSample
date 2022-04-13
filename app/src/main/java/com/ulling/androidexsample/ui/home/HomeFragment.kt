package com.ulling.androidexsample.ui.home

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ulling.androidexsample.R
import com.ulling.androidexsample.SubActivity
import com.ulling.androidexsample.base.BaseFragment
import com.ulling.androidexsample.component.clieckevent.setOnHasTermClickListener
import com.ulling.lib.core.utils.QcLog
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * activity 이동 : ActivityResultContracts.StartActivityForResult
 *
 */
class HomeFragment : BaseFragment(R.layout.fragment_home) {

    private lateinit var homeViewModel: HomeViewModel

    override fun init() {
        QcLog.e("init ======== ")
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            text_home.text = it
        })

        btn_move_sub.setOnHasTermClickListener {
            QcLog.e("btn_move_sub === ")
            startForResult.launch(
                Intent(activity, SubActivity::class.java).putExtra(
                    "sendData",
                    "send"
                )
            )
        }
    }

    override fun onBackPressed() {
    }

    override fun onBackStackChanged() {
    }

    val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            QcLog.e("registerForActivityResult == " + result.toString())
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                val data = intent?.getStringExtra("ResultData")
                QcLog.e("registerForActivityResult == " + data.toString())

                homeViewModel.text.value = "registerForActivityResult : " + data.toString()
            }
        }
}