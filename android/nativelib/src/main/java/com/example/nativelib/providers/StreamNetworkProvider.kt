package com.example.nativelib.providers

import android.content.Context
import com.example.nativelib.extensions.logOnException
import com.example.nativelib.managers.GatewayManager
import com.example.nativelib.models.StreamErrors
import com.example.nativelib.models.api.streaming.ResponseBase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

object StreamNetworkProvider {
    private val nativeLibJob = Job()
    val nativeLibScope = CoroutineScope(Dispatchers.Default + nativeLibJob)

    private fun syncOperation(
        context: Context,
        jsonBody: String,
        url: String,
        requestType: Int,
        onSuccess: (String) -> Unit,
        onError: (StreamErrors) -> Unit
    ) = runCatching {
        GatewayManager.sendRequest(
            context = context,
            jsonBody = jsonBody,
            url = url,
            requestType = requestType,
            onSuccess = onSuccess,
            onError = onError,
        )
    }.logOnException()

    fun <T : ResponseBase> syncOperation(
        context: Context,
        jsonBody: String = "{}",
        url: String,
        requestType: Int,
        classOfV: Class<T>,
        onSuccess: (T) -> Unit,
        onError: (StreamErrors) -> Unit
    ) = runCatching {
        GatewayManager.sendRequest(
            context = context,
            jsonBody = jsonBody,
            url = url,
            requestType = requestType,
            classOfT = classOfV,
            onSuccess = onSuccess,
            onError = onError,
        )
    }.logOnException()

    inline fun <reified T: ResponseBase> syncRequest(
        context: Context,
        jsonBody: String = "{}",
        url: String,
        requestType: Int,
        noinline onSuccess: (T) -> Unit,
        noinline onError: (StreamErrors) -> Unit
    ) {
        nativeLibScope.launch {
            syncOperation(
                context = context,
                jsonBody = jsonBody,
                url = url,
                requestType = requestType,
                classOfV = T::class.java,
                onSuccess = onSuccess,
                onError = onError,
            )
        }
    }
}