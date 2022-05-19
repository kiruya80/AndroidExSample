package com.ulling.androidexsample.utils

import android.os.CancellationSignal
import android.os.ParcelFileDescriptor

import android.print.PageRange

import android.print.PrintDocumentAdapter

import android.print.PrintDocumentInfo

import android.print.PrintAttributes
import android.print.PrintDocumentAdapter.LayoutResultCallback
import android.print.PrintDocumentAdapter.WriteResultCallback
import com.ulling.lib.core.utils.QcLog
import java.io.File
import java.lang.Exception

// https://stackoverflow.com/questions/26841501/save-pdf-from-webview-on-android
// http://www.annalytics.co.uk/android/pdf/2017/04/06/Save-PDF-From-An-Android-WebView/
class PdfPrint(private val printAttributes: PrintAttributes) {
    fun print(printAdapter: PrintDocumentAdapter, path: File, fileName: String) {
//        printAdapter.onLayout(null, printAttributes, null, object : LayoutResultCallback() {
//            override fun onLayoutFinished(info: PrintDocumentInfo, changed: Boolean) {
//                printAdapter.onWrite(
//                    arrayOf(PageRange.ALL_PAGES),
//                    getOutputFile(path, fileName),
//                    CancellationSignal(),
//                    object : WriteResultCallback() {
//                        override fun onWriteFinished(pages: Array<PageRange>) {
//                            super.onWriteFinished(pages)
//                        }
//                    })
//            }
//        }, null)
    }

    private fun getOutputFile(path: File, fileName: String): ParcelFileDescriptor? {
        if (!path.exists()) {
            path.mkdirs()
        }
        val file = File(path, fileName)
        try {
            file.createNewFile()
            return ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_WRITE)
        } catch (e: Exception) {
            QcLog.e( "Failed to open ParcelFileDescriptor", e)
        }
        return null
    }

    companion object {
        private val TAG = PdfPrint::class.java.simpleName
    }
}