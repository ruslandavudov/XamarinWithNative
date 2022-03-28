package com.example.nativelib.models

data class Configuration constructor (
    var domain: String? = "",
    var appId: String? = "",

    var fieldMap: Map<String, String>? = null,
    var fieldList: List<Int>? = null,
    var fieldBool: Boolean? = false,
    var fieldInt: Int? = -1,
)