package com.ulling.androidexsample

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Environment
import android.print.PrintAttributes
import android.print.PrintManager
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.print.PrintHelper
import com.ulling.androidexsample.base.BaseActivity
import com.ulling.androidexsample.component.clieckevent.setOnHasTermClickListener
import com.ulling.androidexsample.utils.PdfPrint
import com.ulling.lib.core.utils.QcLog
import kotlinx.android.synthetic.main.activity_print.*
import kotlinx.android.synthetic.main.activity_sub.*
import kotlinx.android.synthetic.main.activity_sub.btn_finish
import kotlinx.android.synthetic.main.fragment_webview.*
import java.io.File
import android.webkit.ValueCallback
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpEntity
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.ClientProtocolException
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.ResponseHandler
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.methods.HttpGet
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.util.EntityUtils
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException


class PrintActivity : BaseActivity(R.layout.activity_print) {

    override fun init() {
        QcLog.e("init ======== ")


        btn_print.setOnHasTermClickListener {
            QcLog.e("btn_print === ")
            createWebPrintJob(webView)
        }
        btn_print_par.setOnHasTermClickListener {
            QcLog.e("btn_print_par === ")
            urlStr = "https://smartall-mid-web-test.wjthinkbig.com/"
            printJtml()
        }
        btn_finish.setOnHasTermClickListener {
            QcLog.e("btn_finish === ")
            val intent = Intent()
            intent.putExtra("ResultData", edt_send_data.text.toString())
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        doWebViewPrint()
        urlStr = "https://www.wjthinkbig.com/"
        webView.loadUrl(urlStr)
    }


    /**
     * https://smartall-mid-web-test.wjthinkbig.com/
     * https://www.wjthinkbig.com/
     *
     *
     * https://www.naver.com/
     * https://www.daum.net/
     *
     */
    var urlStr = "https://www.naver.com/"
    var doc: Document? = null

    override fun onBackPressed() {
        super.onBackPressed()
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

//    private var mWebView: WebView? = null

    @SuppressLint("SetJavaScriptEnabled")
    private fun doWebViewPrint() {
        // Create a WebView object specifically for printing
//        val webView = WebView(this)
        webView.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest) = false

            override fun onPageFinished(view: WebView, url: String) {
                QcLog.i("page finished loading $url")

//                createWebPrintJob(view)
//                mWebView = null
            }
        }


        var  websettings = webView.settings
        websettings.javaScriptEnabled = true
//        websettings.setJavaScriptCanOpenWindowsAutomatically(true)
//        websettings.setAllowFileAccess(true)
//        websettings.setLoadWithOverviewMode(true)
//        websettings.setUseWideViewPort(true)
//        websettings.setCacheMode(WebSettings.LOAD_DEFAULT)
//        websettings.setDefaultFixedFontSize(14)

//        webView.loadUrl("javascript:alerthello()")
//        val doc: Document = Jsoup.connect("http://en.wikipedia.org/").get()


        // Generate an HTML document on the fly:
//        val htmlDocument =            "<html><body><h1>Test Content</h1><p>Testing, testing, testing...</p></body></html>"
//        webView.loadDataWithBaseURL(null, htmlDocument, "text/HTML", "UTF-8", null)

//        loadData(url, "text/html; charset=utf-8", "UTF-8") loadDataWithBaseURL(null, url, "text/html; charset=utf-8", "UTF-8", null)

//        webView.loadDataWithBaseURL("https://www.naver.com/", "", "text/HTML", "UTF-8", null)

//        webView.loadUrl(urlStr)
        // Keep a reference to WebView object until you pass the PrintDocumentAdapter
        // to the PrintManager
//        mWebView = webView
    }

    fun printJtml() {
        object : Thread() {
            override fun run() {

                try {
                    doc = Jsoup.connect(urlStr).get()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                QcLog.e("doc ==== $doc")
                runOnUiThread {
                    webView.loadDataWithBaseURL(null, doc.toString(), "text/HTML", "UTF-8", null)
                }
            }
        }.start()
    }



    @SuppressLint("ServiceCast")
    private fun createWebPrintJob(webView: WebView) {

        // Get a PrintManager instance
        (getSystemService(Context.PRINT_SERVICE) as? PrintManager)?.let { printManager ->

            val jobName = "${getString(R.string.app_name)} Document"

            // Get a print adapter instance
            val printAdapter = webView.createPrintDocumentAdapter(jobName)

            // Create a print job with name and adapter instance
            printManager.print(
                jobName,
                printAdapter,
                PrintAttributes.Builder().build()
            ).also { printJob ->

                // Save the job object for later status checking
//                printJobs += printJob
            }
        }
    }

    // http://daplus.net/android-webview%EC%97%90%EC%84%9C-html-%EC%BD%98%ED%85%90%EC%B8%A0%EB%A5%BC-%EC%96%BB%EB%8A%94-%EB%B0%A9%EB%B2%95/
//    private fun getHtml(url: String): String? {
//        val pageGet = HttpGet(url)
//        val handler: ResponseHandler<String> = object : ResponseHandler<String?>() {
//            @Throws(ClientProtocolException::class, IOException::class)
//            override fun handleResponse(response: java.net.http.HttpResponse): String? {
//                val entity: HttpEntity = response.getEntity()
//                val html: String
//                return if (entity != null) {
//                    html = EntityUtils.toString(entity)
//                    html
//                } else {
//                    null
//                }
//            }
//        }
//        pageHTML = null
//        try {
//            while (pageHTML == null) {
//                pageHTML = client.execute(pageGet, handler)
//            }
//        } catch (e: ClientProtocolException) {
//            // TODO Auto-generated catch block
//            e.printStackTrace()
//        } catch (e: IOException) {
//            // TODO Auto-generated catch block
//            e.printStackTrace()
//        }
//        return pageHTML
//    }

//    @Override
//    public void customizeWebView(final ServiceCommunicableActivity activity, final WebView webview, final SearchResult mRom) {
//        mRom.setFileSize(getFileSize(mRom.getURLSuffix()));
//        webview.getSettings().setJavaScriptEnabled(true);
//        WebViewClient anchorWebViewClient = new WebViewClient()
//        {
//
//            @Override
//            public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                super.onPageStarted(view, url, favicon);
//
//                //Do what you want to with the html
//                String html = getHTML(url);
//
//                if( html!=null && !url.equals(lastLoadedURL)){
//                    lastLoadedURL = url;
//                    webview.loadDataWithBaseURL(url, html, null, "utf-8", url);
//                }
//            }
}