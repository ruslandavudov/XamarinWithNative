package com.example.nativelib.models

data class Configuration constructor (
    var fieldString: String? = "",
    var fieldMap: Map<String, String>? = null,
    var fieldList: List<Int>? = null,
    var fieldBool: Boolean? = false,
    var fieldInt: Int? = -1,
)