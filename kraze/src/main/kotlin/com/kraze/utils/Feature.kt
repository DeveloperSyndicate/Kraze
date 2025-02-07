package com.kraze.utils
/*

import com.kraze.Methods
import com.kraze.NetworkClient.RequestBuilder
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

fun makeRequestAsync(
    method: String,
    path: String,
    block: RequestBuilder.() -> Unit,
    onSuccess: (Call, Response) -> Unit,
    onFailure: (Call, Throwable) -> Unit
) {
    val builder = RequestBuilder(url)
    builder.url(path)
    builder.method(method)
    builder.block()
    val requestBuilder = builder.build()
    authProvider?.addAuthenticationHeaders(requestBuilder)
    val request = requestBuilder.build()
    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            onFailure(call, e)
        }

        override fun onResponse(call: Call, response: Response) {
            onSuccess(call, response)
        }
    })
}

// Async Operations
fun getAsync(
    path: String,
    block: RequestBuilder.() -> Unit = {},
    onFailure: (Call, Throwable) -> Unit = { _, _ -> },
    onSuccess: (Call, Response) -> Unit = { _, _ -> },
) {
    makeRequestAsync(Methods.GET.name, path, block, onSuccess, onFailure)
}

fun postAsync(
    path: String,
    block: RequestBuilder.() -> Unit,
    onFailure: (Call, Throwable) -> Unit = { _, _ -> },
    onSuccess: (Call, Response) -> Unit = { _, _ -> },
) {
    makeRequestAsync(Methods.POST.name, path, block, onSuccess, onFailure)
}

fun putAsync(
    path: String,
    block: RequestBuilder.() -> Unit,
    onFailure: (Call, Throwable) -> Unit = { _, _ -> },
    onSuccess: (Call, Response) -> Unit = { _, _ -> },
) {
    makeRequestAsync(Methods.PUT.name, path, block, onSuccess, onFailure)
}

fun deleteAsync(
    path: String,
    block: RequestBuilder.() -> Unit,
    onFailure: (Call, Throwable) -> Unit = { _, _ -> },
    onSuccess: (Call, Response) -> Unit = { _, _ -> },
) {
    makeRequestAsync(Methods.DELETE.name, path, block, onSuccess, onFailure)
}

fun headAsync(
    path: String,
    block: RequestBuilder.() -> Unit = {},
    onFailure: (Call, Throwable) -> Unit = { _, _ -> },
    onSuccess: (Call, Response) -> Unit = { _, _ -> },
) {
    makeRequestAsync(Methods.HEAD.name, path, block, onSuccess, onFailure)
}

fun optionsAsync(
    path: String,
    block: RequestBuilder.() -> Unit = {},
    onFailure: (Call, Throwable) -> Unit = { _, _ -> },
    onSuccess: (Call, Response) -> Unit = { _, _ -> },
) {
    makeRequestAsync(Methods.OPTIONS.name, path, block, onSuccess, onFailure)
}

fun patchAsync(
    path: String,
    block: RequestBuilder.() -> Unit,
    onFailure: (Call, Throwable) -> Unit = { _, _ -> },
    onSuccess: (Call, Response) -> Unit = { _, _ -> },
) {
    makeRequestAsync(Methods.PATCH.name, path, block, onSuccess, onFailure)
}

fun traceAsync(
    path: String,
    block: RequestBuilder.() -> Unit = {},
    onFailure: (Call, Throwable) -> Unit = { _, _ -> },
    onSuccess: (Call, Response) -> Unit = { _, _ -> },
) {
    makeRequestAsync(Methods.TRACE.name, path, block, onSuccess, onFailure)
}

fun connectAsync(
    path: String,
    block: RequestBuilder.() -> Unit = {},
    onFailure: (Call, Throwable) -> Unit = { _, _ -> },
    onSuccess: (Call, Response) -> Unit = { _, _ -> },
) {
    makeRequestAsync(Methods.CONNECT.name, path, block, onSuccess, onFailure)
}*/
