package com.example.nativelib.network

import android.os.Build
import com.android.volley.BuildConfig
import com.android.volley.NetworkResponse
import com.android.volley.ParseError
import com.android.volley.Response
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.JsonObjectRequest
import com.example.nativelib.extensions.logOnException
import com.example.nativelib.extensions.returnOnException
import com.example.nativelib.logger.NativeLibLogger
import com.google.gson.JsonSyntaxException
import org.json.JSONObject
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset

internal data class Requests(
    val methodType: Int = Method.POST,
    val fullUrl: String = "",
    val jsonRequest: JSONObject? = null,
    val listener: Response.Listener<JSONObject>? = null,
    val errorsListener: Response.ErrorListener? = null
): JsonObjectRequest(methodType, fullUrl, jsonRequest, listener, errorsListener) {

    companion object {
        private const val HEADER_CONTENT_TYPE = "Content-Type"
        private const val HEADER_USER_AGENT = "User-Agent"
        private const val HEADER_ACCEPT = "Accept"

        private const val VALUE_CONTENT_TYPE = "application/json; charset=utf-8"
        private const val VALUE_ACCEPT = "application/json"

        private const val DEFAULT_RESPONSE_CHARSET = "UTF-8"
    }

    //building headers
    override fun getHeaders(): MutableMap<String, String> {
        val params: MutableMap<String, String> = HashMap()

        runCatching {
            params[HEADER_CONTENT_TYPE] = VALUE_CONTENT_TYPE
            params[HEADER_USER_AGENT] = String.format(
                Build.VERSION.RELEASE,
                Build.MANUFACTURER,
                Build.MODEL,
            )
            params[HEADER_ACCEPT] = VALUE_ACCEPT
        }.logOnException()

        return params
    }

    //Logging responses
    override fun parseNetworkResponse(response: NetworkResponse?): Response<JSONObject> {
        return runCatching {
            logResponse(response)

            try {

                val body = String(
                    response?.data ?: ByteArray(0),
                    Charset.forName(
                        HttpHeaderParser.parseCharset(
                            response?.headers,
                            DEFAULT_RESPONSE_CHARSET
                        )
                    )
                )

                logBodyResponse(body)

                val bodyJson = when {
                    body.isEmpty() -> "{data: null}"
                    !isJsonObject(body) -> "{data: $body}"
                    else -> body
                }

                val cacheEntry = if (response != null) {
                    HttpHeaderParser.parseCacheHeaders(response)
                } else null

                Response.success(JSONObject(bodyJson), cacheEntry)
            } catch (e: UnsupportedEncodingException) {
                Response.error(ParseError(e))
            } catch (e: JsonSyntaxException) {
                Response.error(ParseError(e))
            } finally {
                logEndResponse()
            }
        }.returnOnException { e -> Response.error(ParseError(e)) }
    }

    private fun isJsonObject(body: String) = body.startsWith("{") && body.endsWith("}")

    private fun logResponse(response: NetworkResponse?) {
        if (BuildConfig.DEBUG) {
            runCatching {
                NativeLibLogger.d(this, "<--- ${response?.statusCode} $fullUrl")

                response?.allHeaders?.asIterable()?.forEach { header ->
                    NativeLibLogger.d(this, "${header.name}: ${header.value}")
                }
            }.returnOnException { }
        }
    }

    private fun logEndResponse() {
        if (BuildConfig.DEBUG) {
            runCatching {
                NativeLibLogger.d(this, "<--- End of response")
            }.logOnException()
        }
    }

    private fun logBodyResponse(json: String?) {
        if (BuildConfig.DEBUG) {
            runCatching {
                NativeLibLogger.d(this, "$json")
            }.logOnException()
        }
    }
}

