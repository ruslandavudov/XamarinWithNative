package com.example.nativelib.extensions

import com.example.nativelib.NativeLib
import com.example.nativelib.logger.NativeLibLogger


fun <T> Result<T>.returnOnException(block: (exception: Throwable) -> T): T {
    return this.getOrElse { exception ->
        exception.handle()
        return block.invoke(exception)
    }
}
fun Result<Unit>.logOnException() {
    this.exceptionOrNull()?.handle()
}

private fun Throwable.handle() {
    try {
        NativeLibLogger.e(NativeLib, "NativeLib caught unhandled error", this)
        // todo log crash
    } catch (e: Throwable) {
    }
}