package com.kraze

import com.kraze.auth.AuthenticationProvider
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

/*
 * Copyright 2024 Developer Syndicate
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Author: Sanjay
 * Organization: Developer Syndicate
 * Created: 19-11-2024
 */


/**
 * A robust HTTP client wrapper around OkHttp for making network requests.
 * This class provides methods for all standard HTTP methods, along with
 * support for serialization and authentication. It also allows for
 * configuration of requests using a Kotlin DSL.
 *
 * @property client The underlying OkHttpClient instance used to execute requests.
 * @property url The base URL for all requests made with this client.
 * @property serialization The serialization mechanism to use for decoding response bodies.
 * @property authProvider An optional authentication provider for adding authentication headers to requests.
 */
class NetworkClient(
    private val client: OkHttpClient,
    private val url: String?,
    val serialization: Serialization?,
    private val authProvider: AuthenticationProvider? = null
) {

    /**
     * Makes a network request using the specified HTTP method, path, and request configuration block.
     *
     * @param method The HTTP method (e.g., "GET", "POST").
     * @param path The endpoint path to append to the base URL.
     * @param block A DSL block for configuring the request using the `RequestBuilder`.
     * @return The `Response` returned by the OkHttp client.
     */
    fun makeRequest(method: String, path: String, block: RequestBuilder.() -> Unit): Response {
        val builder = RequestBuilder(url)
        builder.url(path)
        builder.method(method)
        builder.block()
        val requestBuilder = builder.build()
        authProvider?.addAuthenticationHeaders(requestBuilder)
        val request = requestBuilder.build()
        return client.newCall(request = request).execute()
    }

    // HTTP method-specific methods
    fun get(path: String, block: RequestBuilder.() -> Unit = {}): Response {
        return makeRequest(Methods.GET.name, path, block)
    }

    fun post(path: String, block: RequestBuilder.() -> Unit): Response {
        return makeRequest(Methods.POST.name, path, block)
    }

    fun put(path: String, block: RequestBuilder.() -> Unit): Response {
        return makeRequest(Methods.PUT.name, path, block)
    }

    fun delete(path: String, block: RequestBuilder.() -> Unit): Response {
        return makeRequest(Methods.DELETE.name, path, block)
    }

    fun head(path: String, block: RequestBuilder.() -> Unit = {}): Response {
        return makeRequest(Methods.HEAD.name, path, block)
    }

    fun options(path: String, block: RequestBuilder.() -> Unit = {}): Response {
        return makeRequest(Methods.OPTIONS.name, path, block)
    }

    fun patch(path: String, block: RequestBuilder.() -> Unit): Response {
        return makeRequest(Methods.PATCH.name, path, block)
    }

    fun trace(path: String, block: RequestBuilder.() -> Unit = {}): Response {
        return makeRequest(Methods.TRACE.name, path, block)
    }

    fun connect(path: String, block: RequestBuilder.() -> Unit = {}): Response {
        return makeRequest(Methods.CONNECT.name, path, block)
    }

    inline fun <reified T : Any> get(path: String, noinline block: RequestBuilder.() -> Unit = {}): Result<T> {
        return runCatching {
            val response = makeRequest(Methods.GET.name, path, block)
            serializationCommon<T>(response)
        }
    }

    inline fun <reified T : Any> post(path: String, noinline block: RequestBuilder.() -> Unit = {}): Result<T> {
        return runCatching {
            val response = makeRequest(Methods.POST.name, path, block)
            serializationCommon<T>(response)
        }
    }

    inline fun <reified T : Any> put(path: String, noinline block: RequestBuilder.() -> Unit = {}): Result<T> {
        return runCatching {
            val response = makeRequest(Methods.PUT.name, path, block)
            serializationCommon<T>(response)
        }
    }

    inline fun <reified T : Any> delete(path: String, noinline block: RequestBuilder.() -> Unit = {}): Result<T> {
        return runCatching {
            val response = makeRequest(Methods.DELETE.name, path, block)
            serializationCommon<T>(response)
        }
    }

    inline fun <reified T : Any> serializationCommon(response: Response): T {
        if (!response.isSuccessful) {
            throw IllegalStateException("HTTP error: ${response.code}")
        }

        val body = response.body?.string()
            ?: throw IllegalStateException("Response body is null")

        if (serialization != null) {
            return serialization.decodeFromString(T::class, body)
        } else {
            throw IllegalStateException("Serialization is not initialized")
        }
    }

    fun getWithResult(path: String, block: RequestBuilder.() -> Unit = {}): Result<Response> {
        return runCatching {
            makeRequest(Methods.GET.name, path, block)
        }
    }

    fun postWithResult(path: String, block: RequestBuilder.() -> Unit = {}): Result<Response> {
        return runCatching {
            makeRequest(Methods.POST.name, path, block)
        }
    }

    fun putWithResult(path: String, block: RequestBuilder.() -> Unit): Result<Response> {
        return runCatching {
            makeRequest(Methods.PUT.name, path, block)
        }
    }

    fun deleteWithResult(path: String, block: RequestBuilder.() -> Unit): Result<Response> {
        return runCatching {
            makeRequest(Methods.DELETE.name, path, block)
        }
    }

    internal fun newWebSocket(
        path: String,
        block: RequestBuilder.() -> Unit,
        listener: WebSocketListener
    ): WebSocket {
        val builder = RequestBuilder(url)
        builder.url(path)
        builder.block()
        val request = builder.build().build()

        return client.newWebSocket(request, listener)
    }



    // DSL for HTTP Requests
    class RequestBuilder(private val baseUrl: String?) {
        private val headers = mutableListOf<Pair<String, String>>()
        private val queryParams = mutableMapOf<String, String>()
        private val multipartBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)
        private var url: String? = null
        private var method: String = Methods.GET.name
        private var body: RequestBody? = null
        private var timeout: Long = 30L
        private var loggingEnabled = false
        private var authProvider: AuthenticationProvider? = null

        fun url(path: String) {
            this.url = baseUrl?.let { "$it$path" } ?: path
        }

        fun auth(provider: AuthenticationProvider) { this.authProvider = provider }

        fun queryParam(name: String, value: String) {
            queryParams[name] = value
        }

        fun multipartField(name: String, value: String) {
            multipartBuilder.addFormDataPart(name, value)
        }

        fun multipartFile(name: String, fileName: String, file: File, mediaType: MediaType) {
            multipartBuilder.addFormDataPart(name, fileName, file.asRequestBody(mediaType))
        }

        fun method(method: String) {
            this.method = method
        }

        fun body(body: RequestBody) {
            this.body = body
        }

        fun timeout(seconds: Long) {
            this.timeout = seconds
        }

        fun header(name: String, value: String) {
            headers.add(name to value)
        }

        fun logHeaders(enable: Boolean) {
            loggingEnabled = enable
        }

        fun build(): Request.Builder {
            if (method in listOf(Methods.POST.name, Methods.PUT.name, Methods.PATCH.name) && body == null) {
                body = multipartBuilder.build()
            }
            val finalUrl = urlWithQueryParams()
            val requestBuilder = Request.Builder()
            finalUrl?.let { requestBuilder.url(it) }
            requestBuilder.method(method, body)

            headers.forEach { (name, value) ->
                requestBuilder.addHeader(name, value)
            }
            authProvider?.addAuthenticationHeaders(requestBuilder)
            return requestBuilder
        }

        private fun urlWithQueryParams(): String? {
            if (url.isNullOrBlank()) return null
            val builder = url!!.toHttpUrlOrNull()?.newBuilder()
            queryParams.forEach { (key, value) ->
                builder?.addQueryParameter(key, value)
            }
            return builder?.build().toString()
        }
    }
}


/**
 * A DSL function for constructing a `NetworkClient` with customizable configuration.
 *
 * @param block A DSL block for configuring the `KrazeBuilder`, which sets up the `NetworkClient`.
 * @return A fully configured `NetworkClient` instance.
 */
fun krazeClient(block: KrazeBuilder.() -> Unit): NetworkClient {
    val builder = KrazeBuilder()
    builder.block()
    val baseUrl = builder.getBaseUrl()
    val serialization = builder.getSerialization()
    val authenticator = builder.getAuthProvider()
    return NetworkClient(network(block), baseUrl, serialization, authenticator)
}