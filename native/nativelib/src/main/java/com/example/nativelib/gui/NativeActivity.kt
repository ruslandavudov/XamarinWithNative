package com.example.nativelib.gui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import com.example.nativelib.NativeLib
import com.example.nativelib.OperationResponse
import com.example.nativelib.R
import com.google.android.material.textfield.TextInputEditText

class NativeActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configure()
    }

    override fun onClick(v: View?) {
        if (v!!.id == R.id.bBack) {
            onBackClick()
        }
        else if (v.id == R.id.bApply) {
            onApplyClick()
        }
        else {
            println("missing")
        }
    }

    private fun onBackClick() {
        finish()
    }

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private fun onApplyClick() {
        val lib = NativeLib.getInstance()

        val fString = findViewById<View>(R.id.fieldString) as TextInputEditText
        val fBool = findViewById<View>(R.id.fieldBool) as Switch

        val response = OperationResponse().apply {
            fieldString = fString.text?.toString()
            fieldBool = fBool.isChecked
        }

        lib?.setResponse(response)
//        finish()
    }

    private fun configure() {
        setContentView(R.layout.activity_native)
        buttonsConfigure()
        fieldsConfigure()
    }

    private fun buttonsConfigure() {
        val bApply: Button? = findViewById(R.id.bApply)
        bApply?.setOnClickListener(this)

        val bBack: Button? = findViewById(R.id.bBack)
        bBack?.setOnClickListener(this)
    }

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private fun fieldsConfigure() {
        val fieldBool = findViewById<View>(R.id.fieldBool) as Switch
        val fieldString = findViewById<View>(R.id.fieldString) as TextInputEditText

        val string = intent.getStringExtra("fieldString")
        val bool = intent.getBooleanExtra("fieldBool", false)

        fieldBool.isChecked = bool
        fieldString.setText(string, TextView.BufferType.EDITABLE)
    }
}