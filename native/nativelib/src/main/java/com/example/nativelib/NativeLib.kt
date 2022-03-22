package com.example.nativelib

import android.content.Context
import android.content.Intent
import com.example.nativelib.gui.NativeActivity
import com.example.nativelib.models.Configuration
import com.google.gson.annotations.SerializedName


class OperationResponse(
    @SerializedName("fieldString") var fieldString: String? = null,
    @SerializedName("fieldBool") var fieldBool: Boolean? = null,
)

class NativeLib private constructor() {
    private var onSuccess: ((OperationResponse) -> Unit)? = null

    companion object {
        private var instance: NativeLib? = null
        var configuration: Configuration? = null

        @JvmName("init")
        @JvmStatic
        fun init(configuration: Configuration): NativeLib? {
            this.instance = NativeLib()
            this.configuration = configuration

            return this.instance
        }

        @JvmName("getInstance")
        @JvmStatic
        fun getInstance(): NativeLib? {
            return this.instance
        }

        @JvmName("getConfig")
        @JvmStatic
        fun getConfig(): Configuration? {
            return this.configuration
        }

        @JvmName("setConfig")
        @JvmStatic
        fun setConfig(configuration: Configuration): Configuration? {
            this.configuration = configuration
            return this.configuration
        }
    }

    internal fun setResponse(response: OperationResponse) {
        configuration = configuration?.apply {
            fieldBool = response.fieldBool
            fieldString = response.fieldString
        }

        this.onSuccess?.invoke(response)
    }

    @JvmName("start")
    fun start(context: Context) {
        val intent = Intent(context, NativeActivity::class.java)
        intent.putExtra("fieldString", configuration?.fieldString ?: "null")
        intent.putExtra("fieldBool", configuration?.fieldBool ?: false)

        context.startActivity(intent)
    }

    @JvmName("onApply")
    fun onApply(onSuccess: (OperationResponse) -> Unit) {
        this.onSuccess = onSuccess
    }
}
