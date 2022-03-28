package com.example.nativelib.gui

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.nativelib.NativeLib
import com.example.nativelib.OperationResponse
import com.example.nativelib.R
import com.example.nativelib.models.StreamErrors
import com.example.nativelib.models.api.streaming.Product
import com.google.gson.Gson


class WebViewActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        buttonsConfigure()
        webViewConfigure()

    }

    @SuppressLint("SetJavaScriptEnabled", "JavascriptInterface")
    private fun webViewConfigure() {
        val webView: WebView = findViewById(R.id.webview)
        webView.settings.javaScriptEnabled = true

        val id = intent.getStringExtra("streamId")
        val appId = intent.getStringExtra("appId")
        val url = "https://app.shopstory.live/sdk/?x-force-appid=${appId}#/translation/${id}"

        webView.addJavascriptInterface(WebAppInterface(this), "shopstory")
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if (url != null) {
                    view?.loadUrl(url)
                }
                return true
            }
        }

        webView.loadUrl(url)
    }

    private fun buttonsConfigure() {
        val bBack: Button? = findViewById(R.id.bWebViewBack)
        bBack?.setOnClickListener(this)
    }

    private fun onBackClick() {
        finish()
    }

    override fun onClick(v: View?) {
        if (v!!.id == R.id.bWebViewBack) {
            onBackClick()
        }
    }
}

class WebAppInterface(private val webViewActivity: Activity) {
    private val gson by lazy { Gson() }

    @JavascriptInterface
    fun addProduct(jsonData: String?) {
        try {
            val product = gson.fromJson(jsonData, Product::class.java)
            val lib = NativeLib.getInstance()
            val response = OperationResponse().apply {
                this.product = product
            }

            lib?.setResponse(response)
            webViewActivity.finish()
            NativeActivity.instant?.finish()
        } catch (ex: Exception) {
            StreamErrors.Unknown(
                throwable = ex,
            )
        }
    }
}