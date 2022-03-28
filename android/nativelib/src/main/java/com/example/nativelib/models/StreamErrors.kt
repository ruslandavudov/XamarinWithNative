package com.example.nativelib.models

import com.google.gson.Gson


sealed class StreamErrors(open val code: Int?) {

    companion object {

        private val gson by lazy { Gson() }

    }

    fun toJson(): String = gson.toJson(this)

    data class Validation(
        override val code: Int,
        val status: Int?,
        val errorMessage: String?,
    ) : StreamErrors(code) {
        override fun toString(): String = errorMessage ?: "null"
    }

    data class Protocol(
        override val code: Int,
        val status: Int?,
        val errorMessage: String?,
    ) : StreamErrors(code) {
        override fun toString(): String = errorMessage ?: "null"
    }

    data class InternalServer(
        override val code: Int,
        val errorMessage: String?,
    ) : StreamErrors(code) {
        override fun toString(): String = errorMessage ?: "null"
    }

    data class UnknownServer(
        override val code: Int? = null,
        val errorMessage: String? = null,
    ) : StreamErrors(code) {

        override fun toString(): String = errorMessage ?: "null"
        constructor() : this(errorMessage = "Cannot reach server")

    }

    data class Unknown(
        val throwable: Throwable? = null
    ) : StreamErrors(null)

}