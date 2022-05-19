package com.ulling.androidexsample.ui.webview

import androidx.lifecycle.ViewModelProvider
import com.ulling.androidexsample.base.BaseFragment
import com.ulling.lib.core.utils.QcLog
import kotlinx.android.synthetic.main.fragment_webview.*
import android.os.Environment

import android.print.PrintAttributes

import android.print.PrintAttributes.Resolution

import android.webkit.WebView
import com.ulling.androidexsample.R
import com.ulling.androidexsample.utils.PdfPrint
import java.io.File


class WebViewFragment : BaseFragment(R.layout.fragment_webview) {

    private lateinit var viewModel: WebViewModel


    override fun init() {
        QcLog.e("init ======== ")
        viewModel =
            ViewModelProvider(this).get(WebViewModel::class.java)
    }

    override fun initView() {
        QcLog.e("initView ======== ")




    }


    private fun createWebPrintJob(webView: WebView) {
        val jobName = getString(R.string.app_name) + " Document"

        val custom = PrintAttributes.MediaSize("VISIT_K", "VISIT_K", 1600, 2560)
        custom.asLandscape() // 용지방향?

        val attributes = PrintAttributes.Builder()
//            .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
//            .setResolution(Resolution("pdf", "pdf", 600, 600))
//            .setMinMargins(PrintAttributes.Margins.NO_MARGINS)
            .setMediaSize(custom)
            .build()
        val path: File =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM + "/PDFTest/")

        val pdfPrint = PdfPrint(attributes)
        pdfPrint.print(
            webView.createPrintDocumentAdapter(jobName),
            path,
            "output_" + System.currentTimeMillis() + ".pdf"
        )
    }

}