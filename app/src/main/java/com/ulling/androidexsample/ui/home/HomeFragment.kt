package com.ulling.androidexsample.ui.home

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ulling.androidexsample.MainSubActivity
import androidx.navigation.fragment.findNavController
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
    lateinit var observer: HomeLifecycleObserver

    override fun init() {
        QcLog.e("init ======== ")
//        homeViewModel =
//            ViewModelProvider(this).get(HomeViewModel::class.java)
        val sampleParam = "Add Param"
        homeViewModel = ViewModelProvider(this, HomeParamViewModelFactory(sampleParam))
            .get(HomeViewModel::class.java)

        observer = HomeLifecycleObserver(
            requireActivity(),
            homeViewModel
        )
        lifecycle.addObserver(observer)
    }

    override fun initView() {
        QcLog.e("initView ======== ")
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            text_home.text = it
        })

        fab.setOnClickListener {
            findNavController().navigate(R.id.action_move_permission)
        }

        btn_move_main_sub.setOnHasTermClickListener {
            QcLog.e("btn_move_main_sub === ")
            actResultStartActivity.launch(
                Intent(activity, MainSubActivity::class.java).putExtra(
                    "mainSendData",
                    "send"
                )
            )
        }

        btn_move_sub.setOnHasTermClickListener {
            QcLog.e("btn_move_sub === ")
            observer.startAct.launch(
                Intent(context, SubActivity::class.java).putExtra(
                    "mainSendData",
                    "send"
                )
            )
        }
    }

    /**
     * 참고 자료
     * https://developer.android.com/training/basics/intents/result?hl=ko#java
     *
     *
    //        ActivityResultLauncher<String> registerForActivityResult = registerForActivityResult(
    //                new ActivityResultContracts.RequestPermission(),
    //                new ActivityResultCallback<Boolean>() {
    //                    @Override
    //                    public void onActivityResult(Boolean result) {
    //
    //                    }
    //                }
    //        );
     */
    val actResultStartActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            QcLog.e("registerForActivityResult == " + result.toString())
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                val data = intent?.getStringExtra("ResultData")
                QcLog.e("activity response data == " + data.toString())

                homeViewModel.text.value = "activity response data : " + data.toString()
            }
        }
}