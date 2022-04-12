package com.ulling.androidexsample.ui.notifications

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ulling.androidexsample.R
import com.ulling.androidexsample.base.BaseFragment
import com.ulling.lib.core.utils.QcLog
import kotlinx.android.synthetic.main.fragment_notifications.*

class NotificationsFragment : BaseFragment(R.layout.fragment_notifications) {

    private lateinit var notificationsViewModel: NotificationsViewModel


    override fun init() {
        QcLog.e("init ======== ")
        notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)
        notificationsViewModel.text.observe(viewLifecycleOwner, Observer {
            text_notifications.text = it
        })
        text_notifications.text = "text_notifications"
    }

    override fun onBackPressed() {
    }

    override fun onBackStackChanged() {
    }
}