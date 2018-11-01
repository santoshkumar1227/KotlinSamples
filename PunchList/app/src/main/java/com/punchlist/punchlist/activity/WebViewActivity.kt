package com.punchlist.punchlist.activity

import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.webkit.WebView
import android.webkit.WebViewClient
import com.punchlist.punchlist.R
import kotlinx.android.synthetic.main.activity_web_view.*

class WebViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "About"
        if (intent.hasExtra("url")) {
            val url = intent.getStringExtra("url")
            webView.webViewClient = MyWebViewClient()
            webView.settings.javaScriptEnabled = true
            webView.loadUrl(url)
        } else if (intent.hasExtra("htmldata")) {
            webView.loadData(intent.getStringExtra("htmldata"), "text/html", "UTF-8")
        }
    }

    inner class MyWebViewClient : WebViewClient() {
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
        }

        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            view?.loadUrl(url)
            return true
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return true
    }
}
