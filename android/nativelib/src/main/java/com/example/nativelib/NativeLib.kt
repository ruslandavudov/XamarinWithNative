package com.example.nativelib

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.android.volley.Request
import com.example.nativelib.gui.NativeActivity
import com.example.nativelib.models.Configuration
import com.example.nativelib.models.api.streaming.Product
import com.example.nativelib.models.api.streaming.StreamResponse
import com.example.nativelib.models.api.streaming.StreamsResponse
import com.example.nativelib.providers.StreamNetworkProvider
import com.google.gson.annotations.SerializedName


class OperationResponse(
    @SerializedName("product") var product: Product? = null,
)

class NativeLib private constructor() {
    private var onSuccess: ((OperationResponse) -> Unit)? = null
    private var onError: ((String) -> Unit)? = null

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
        this.onSuccess?.invoke(response)
    }

    @JvmName("start")
    fun start(context: Context) {
        getStreams(context)
    }

    // example endpoint: "/v2/mini-player/stream?application=shopstory-prod&productCode=jf114440"
    @JvmName("getStream")
    fun getStream(context: Context, productCode: String) {
        configuration?.let { cfg ->
            StreamNetworkProvider.syncRequest<StreamResponse>(
                context = context,
                url = "${cfg.domain}/v2/mini-player/stream?application=${cfg.appId}&productCode=${productCode}",
                requestType = Request.Method.GET,
                onSuccess = {
                    val body = it.body
                    println(body?.playbackLink)
                },
                onError = {
                    this.onError?.invoke(it.toString())
                    println(it)
                }
            )
        }

    }

    // example endpoint: "/v3/streams?applicationId=shopstory-prod"
    @JvmName("getStreams")
    fun getStreams(context: Context) {
        configuration?.let { cfg ->
            StreamNetworkProvider.syncRequest<StreamsResponse>(
                context = context,
                url = "${cfg.domain}/v3/streams?applicationId=${cfg.appId}",
                requestType = Request.Method.GET,
                onSuccess = {
                    val intent = Intent(context, NativeActivity::class.java)
                    val bundle = Bundle()

                    bundle.putParcelable("streamsResponse", it)
                    intent.putExtra("Bundle", bundle)

                    context.startActivity(intent)
                },
                onError = {
                    this.onError?.invoke(it.toString())
                    println(it)
                }
            )
        }

    }

    @JvmName("onSentData")
    fun onSentData(onSuccess: (OperationResponse) -> Unit) {
        this.onSuccess = onSuccess
    }

    @JvmName("onError")
    fun onError(onError: (String) -> Unit) {
        this.onError = onError
    }
}
