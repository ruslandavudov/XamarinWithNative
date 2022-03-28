package com.example.nativelib.managers

import android.content.Context
import com.android.volley.DefaultRetryPolicy
import com.android.volley.VolleyError
import com.example.nativelib.logger.NativeLibLogger
import com.example.nativelib.models.StreamErrors
import com.example.nativelib.models.api.streaming.ErrorResponse
import com.example.nativelib.models.api.streaming.ResponseBase
import com.example.nativelib.network.ServiceGenerator
import com.example.nativelib.network.Requests
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject
import kotlinx.coroutines.*

internal object GatewayManager {
    private const val TIMEOUT_DELAY = 15000
    private const val MAX_RETRIES = 0

    private val gson by lazy { Gson() }
    private val gatewayScope by lazy { CoroutineScope(Dispatchers.Main + Job()) }

    private fun getUrl(endPoint: String): String = "https://api.shopstory.live${endPoint}"

    fun <T : ResponseBase> sendRequest(
        context: Context,
        jsonBody: String?,
        url: String,
        requestType: Int,
        classOfT: Class<T>,
        onSuccess: (T) -> Unit,
        onError: (StreamErrors) -> Unit
    ) = sendRequest(
        context = context,
        jsonBody = jsonBody,
        url = url,
        requestType = requestType,
        onSuccess = { body -> handleSuccessResponse(body, onSuccess, onError, classOfT) },
        onError = onError
    )

    fun sendRequest(
        context: Context,
        jsonBody: String?,
        url: String,
        requestType: Int,
        onSuccess: (String) -> Unit,
        onError: (StreamErrors) -> Unit
    ) {
        try {
//            val url: String = getUrl(endpoint)
            val jsonRequest: JSONObject? = convertBodyToJson(jsonBody)

            val request = Requests(
                methodType = requestType,
                fullUrl = url,
                jsonRequest = jsonRequest,
                listener = {
                    NativeLibLogger.d(this, "Request successful sent")
                    onSuccess.invoke(it.toString())
                },
                errorsListener = { volleyError -> handleError(volleyError, onError) }
            ).apply {
                setShouldCache(false)
                retryPolicy = DefaultRetryPolicy(
                    TIMEOUT_DELAY,
                    MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
            }

            ServiceGenerator.getInstance(context)?.addToRequestQueue(request)
        } catch (e: Exception) {
            NativeLibLogger.e(this, "Sending request was failure with exception", e)
            onError.invoke(StreamErrors.Unknown(e))
        }
    }


    private fun <T : ResponseBase> handleSuccessResponse(
        data: String,
        onSuccess: (T) -> Unit,
        onError: (StreamErrors) -> Unit,
        classOfT: Class<T>
    ) = gatewayScope.launch {
        try {
            handleSuccess(data, onSuccess, onError, classOfT)
        } catch (e: Exception) {
            onError.invoke(StreamErrors.Unknown(e))
        }
    }

    private fun<T : ResponseBase> handleSuccess(
        data: String,
        onSuccess: (T) -> Unit,
        onError: (StreamErrors) -> Unit,
        classOfT: Class<T>
    ) = gatewayScope.launch{
        val dataObject = convertJsonToBody(data, classOfT)
        val status = dataObject.status

        if (status == 200) {
            onSuccess.invoke(dataObject)
        } else {
            val errorResponse = convertJsonToBody(data, ErrorResponse::class.java)
            onError.invoke(
                StreamErrors.Validation(
                    code = 200,
                    status = errorResponse.status,
                    errorMessage = errorResponse.body?.message,
                )
            )
        }
    }

    private fun handleError(
        volleyError: VolleyError,
        onError: (StreamErrors) -> Unit
    ) = gatewayScope.launch {
        try {
            val error = volleyError.networkResponse
            if (error == null) {
                onError.invoke(StreamErrors.Unknown())
                return@launch
            }
            val code = error.statusCode
            val errorData = error.data
            val errorMessage = String(errorData)

            when (code) {
                400, 401, 403, 404, 429 -> onError.invoke(
                    StreamErrors.Protocol(
                        code = code,
                        status = null,
                        errorMessage = errorMessage,
                    )
                )
                500, 503 -> onError.invoke(
                    StreamErrors.InternalServer(
                        code = code,
                        errorMessage = "Internal server error",
                    )
                )
                else -> onError.invoke(
                    StreamErrors.UnknownServer(
                        code = code,
                        errorMessage = "Unknown server error",
                    )
                )
            }
        } catch (e: Exception) {
            NativeLibLogger.e(this, "Parsing server response was failure", e)
            onError.invoke(StreamErrors.Unknown(e))
        }
    }

    private fun convertBodyToJson(body: String?): JSONObject? {
        return if (body == null) {
            null
        } else try {
            JSONObject(body)
        } catch (e: JSONException) {
            null
        }
    }

    private suspend fun <T> convertJsonToBody(
        data: String,
        classOfT: Class<T>
    ) = withContext(Dispatchers.Default) { gson.fromJson(data, classOfT) }
}