package com.example.nativelib.network

import android.content.Context
import com.android.volley.BuildConfig
import com.android.volley.RequestQueue
import com.android.volley.VolleyLog
import com.android.volley.toolbox.Volley
import com.example.nativelib.extensions.logOnException
import com.example.nativelib.extensions.returnOnException


internal class ServiceGenerator constructor(context: Context) {
    companion object {
        @Volatile
        private var INSTANCE: ServiceGenerator? = null
        internal fun getInstance(context: Context): ServiceGenerator? = runCatching {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: ServiceGenerator(context).also {
                    INSTANCE = it
                }
            }
        }.returnOnException { null }
    }

    init {
        runCatching {
            VolleyLog.DEBUG = BuildConfig.DEBUG
        }.logOnException()
    }

    private val requestQueue: RequestQueue? by lazy {
        Volley.newRequestQueue(context.applicationContext)
    }

    internal fun addToRequestQueue(request: Requests) {
        runCatching {
            requestQueue?.add(request)
        }.returnOnException {}
    }
}