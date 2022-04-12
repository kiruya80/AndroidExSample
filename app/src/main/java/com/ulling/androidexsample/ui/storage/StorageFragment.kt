package com.ulling.androidexsample.ui.storage

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ulling.androidexsample.R
import com.ulling.androidexsample.base.BaseFragment
import com.ulling.lib.core.utils.QcLog
import kotlinx.android.synthetic.main.fragment_storage.*

class StorageFragment : BaseFragment(R.layout.fragment_storage) {

    private lateinit var storageViewModel: StorageViewModel


    override fun init() {
        QcLog.e("init ======== ")

        storageViewModel =
            ViewModelProvider(this).get(StorageViewModel::class.java)
        storageViewModel.text.observe(viewLifecycleOwner, Observer {
            text_storage.text = it
        })
        text_storage.text = "text_storage"

    }

    override fun onBackPressed() {
    }

    override fun onBackStackChanged() {
    }
}