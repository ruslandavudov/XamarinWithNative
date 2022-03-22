package com.example.bindingnative

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.example.nativelib.NativeLib
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var nativeLib: NativeLib? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonsConfigure()
        initNativeLib()
    }

    private fun buttonsConfigure() {
        val bApply: Button? = findViewById(R.id.bOpen)
        bApply?.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        val fBool = findViewById<View>(com.example.nativelib.R.id.fieldBool) as SwitchMaterial
        val fString = findViewById<View>(com.example.nativelib.R.id.fieldString) as TextInputEditText

        changeConfiguration(fBool.isChecked, fString.text.toString())

        nativeLib?.start(this)
    }

    private fun changeConfiguration(fBool: Boolean, fString: String) {

        var configuration = NativeLib.getConfig()
        configuration = configuration?.apply {
            fieldBool = fBool
            fieldString = fString
        }

        if (configuration != null) {
            NativeLib.setConfig(configuration)
        }
    }

    private fun initNativeLib() {
        val configuration = com.example.nativelib.models.Configuration()
        nativeLib = NativeLib.init(configuration)
        nativeLib?.onApply {
            changeConfiguration(it.fieldBool ?: false, it.fieldString ?: "")

            val fBool = findViewById<View>(com.example.nativelib.R.id.fieldBool) as SwitchMaterial
            val fString = findViewById<View>(com.example.nativelib.R.id.fieldString) as TextInputEditText

            fBool.isChecked = it.fieldBool ?: false
            fString.setText(it.fieldString)
        }
    }
}