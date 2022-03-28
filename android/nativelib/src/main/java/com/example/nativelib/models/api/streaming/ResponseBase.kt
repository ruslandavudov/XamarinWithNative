package com.example.nativelib.models.api.streaming

interface ResponseBase{
    val status: Int?
    val serverTime: String?

    companion object {

        internal const val STATUS_SUCCESS = 200
        internal const val STATUS_INTERNAL_SERVER_ERROR = 500
        internal const val STATUS_PROTOCOL_ERROR = 400

    }
}