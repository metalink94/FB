package ru.lopav.kzn.fb

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.web.*

class WebViewActivity: AppCompatActivity() {

    var currentUrl: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.web)
        currentUrl = intent.getStringExtra(KEY_URL)
//        currentUrl = "https://track.alltraffer.ru/click?pid=3098&offer_id=130&sub1=andr"
        //https://track.alltraffer.ru/click?pid=3098&offer_id=206&sub1=android
        initWebView()
        swipeRefresh.setOnRefreshListener {
            swipeRefresh.isRefreshing = true
            load()
        }
    }

    private fun initWebView() {
        webView.webChromeClient = WebChromeClient()
        webView.webViewClient = object : WebViewClient() {

            @TargetApi(Build.VERSION_CODES.N)
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                val uri = request?.url
                return shouldOverrideUrlLoading(uri.toString())
            }

            @Deprecated("shouldOverrideUrlLoading")
            override fun shouldOverrideUrlLoading(view: WebView, url: String?): Boolean {
                return shouldOverrideUrlLoading(url ?: "")
            }

            private fun shouldOverrideUrlLoading(url: String): Boolean {
                if (url.startsWith("market")) {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                    finish()
                    return true
                }
                currentUrl = url
                Log.d("WebView", "get Url $currentUrl")
//                load()
                return false
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                swipeRefresh.isRefreshing = false
            }
        }
        webView.clearCache(true)
        webView.clearHistory()
        webView.setInitialScale(1)
        webView.settings.javaScriptEnabled = true
        webView.settings.loadWithOverviewMode = true
        webView.settings.useWideViewPort = true
        webView.settings.javaScriptCanOpenWindowsAutomatically = true
        webView.scrollBarStyle = WebView.SCROLLBARS_OUTSIDE_OVERLAY
        webView.isScrollbarFadingEnabled = false
        webView.settings.builtInZoomControls = true
        webView.settings.displayZoomControls = false
        load()
    }

    private fun load() {
        webView.loadUrl(currentUrl)
    }

    companion object {
        const val KEY_URL = "url"

        fun getInstance(context: Context, url: String): Intent =
            Intent(context, WebViewActivity::class.java)
                .putExtra(KEY_URL, url)
    }
}
