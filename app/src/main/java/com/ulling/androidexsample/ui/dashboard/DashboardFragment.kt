package com.ulling.androidexsample.ui.dashboard

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ulling.androidexsample.R
import com.ulling.androidexsample.base.BaseFragment
import com.ulling.lib.core.utils.QcLog
import kotlinx.android.synthetic.main.fragment_dashboard.*

class DashboardFragment : BaseFragment(R.layout.fragment_dashboard) {

    private lateinit var dashboardViewModel: DashboardViewModel


    override fun init() {
        QcLog.e("init ======== ")
        dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)
        dashboardViewModel.text.observe(viewLifecycleOwner, Observer {
            text_dashboard.text = it
        })
        text_dashboard.text = "text_dashboard"

    }

    override fun onBackPressed() {
    }

    override fun onBackStackChanged() {
    }
}