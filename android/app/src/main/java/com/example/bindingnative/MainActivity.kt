package com.example.bindingnative

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.nativelib.NativeLib
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var nativeLib: NativeLib? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonsConfigure()
        initNativeLib()
        initViewFields()
    }

    private fun buttonsConfigure() {
        val bApply: Button? = findViewById(R.id.bOpen)
        bApply?.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
//        val fBool = findViewById<View>(R.id.fieldBool) as SwitchMaterial
        val domain = findViewById<View>(R.id.domain) as TextInputEditText
        val appId = findViewById<View>(R.id.appId) as TextInputEditText

        changeConfiguration(domain.text.toString(), appId.text.toString())
        nativeLib?.start(this)
    }

    private fun changeConfiguration(domain: String, appId: String) {

        var configuration = NativeLib.getConfig()
        configuration = configuration?.apply {
            this.domain = domain
            this.appId = appId
        }

        if (configuration != null) {
            NativeLib.setConfig(configuration)
        }
    }

    private fun initNativeLib() {
        val configuration = com.example.nativelib.models.Configuration(domain = "https://api.shopstory.live", appId = "auchan-test")
        nativeLib = NativeLib.init(configuration)
        nativeLib?.onSentData {

            val fProductName = findViewById<View>(R.id.productName) as TextView
            fProductName.text = it.product?.name

            val fFeedProductId = findViewById<View>(R.id.feedProductId) as TextView
            fFeedProductId.text = it.product?.feedProductId

            val fFeedProductGroupId = findViewById<View>(R.id.feedProductGroupId) as TextView
            fFeedProductGroupId.text = it.product?.feedProductGroupId

            val fVendorCode = findViewById<View>(R.id.vendorCode) as TextView
            fVendorCode.text = it.product?.vendorCode
        }
        nativeLib?.onError {
            it.showSnackBar()
        }
    }

    private fun initViewFields() {
        val domain = findViewById<View>(R.id.domain) as TextInputEditText
        val appId = findViewById<View>(R.id.appId) as TextInputEditText
        domain.setText(NativeLib.getConfig()?.domain)
        appId.setText(NativeLib.getConfig()?.appId)
    }

    private fun String.showSnackBar() {
        val parentLayout = findViewById<View>(android.R.id.content)
        val snack = Snackbar.make(parentLayout, this, Snackbar.LENGTH_LONG)
        snack.show()
    }
}