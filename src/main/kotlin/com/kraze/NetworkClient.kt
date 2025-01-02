package com.kraze

import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response

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

class NetworkClient(private val client: OkHttpClient, private val url: String?, val serialization: Serialization?) {

    fun makeRequest(method: String, path: String, block: RequestBuilder.() -> Unit): Response {
        val builder = RequestBuilder(url)
        builder.url(path)
        builder.method(method)
        builder.block()
        return client.newCall(builder.build()).execute()
    }

    // HTTP method-specific methods
    fun get(path: String, block: RequestBuilder.() -> Unit): Response {
        return makeRequest("GET", path, block)
    }

    fun post(path: String, block: RequestBuilder.() -> Unit): Response {
        return makeRequest("POST", path, block)
    }

    fun put(path: String, block: RequestBuilder.() -> Unit): Response {
        return makeRequest("PUT", path, block)
    }

    fun delete(path: String, block: RequestBuilder.() -> Unit): Response {
        return makeRequest("DELETE", path, block)
    }

    fun head(path: String, block: RequestBuilder.() -> Unit = {}): Response {
        return makeRequest("HEAD", path, block)
    }

    fun options(path: String, block: RequestBuilder.() -> Unit = {}): Response {
        return makeRequest("OPTIONS", path, block)
    }

    fun patch(path: String, block: RequestBuilder.() -> Unit): Response {
        return makeRequest("PATCH", path, block)
    }

    fun trace(path: String, block: RequestBuilder.() -> Unit = {}): Response {
        return makeRequest("TRACE", path, block)
    }

    fun connect(path: String, block: RequestBuilder.() -> Unit = {}): Response {
        return makeRequest("CONNECT", path, block)
    }

    inline fun <reified T : Any> get(path: String, noinline block: RequestBuilder.() -> Unit = {}): Result<T> {
        return runCatching {
            val response = makeRequest("GET", path, block)
            serializationCommon<T>(response)
        }
    }

    inline fun <reified T : Any> post(path: String, noinline block: RequestBuilder.() -> Unit = {}): Result<T> {
        return runCatching {
            val response = makeRequest("POST", path, block)
            serializationCommon<T>(response)
        }
    }

    inline fun <reified T : Any> put(path: String, noinline block: RequestBuilder.() -> Unit = {}): Result<T> {
        return runCatching {
            val response = makeRequest("PUT", path, block)
            serializationCommon<T>(response)
        }
    }

    inline fun <reified T : Any> delete(path: String, noinline block: RequestBuilder.() -> Unit = {}): Result<T> {
        return runCatching {
            val response = makeRequest("DELETE", path, block)
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
            makeRequest("GET", path, block)
        }
    }

    fun postWithResult(path: String, block: RequestBuilder.() -> Unit = {}): Result<Response> {
        return runCatching {
            makeRequest("POST", path, block)
        }
    }

    fun putWithResult(path: String, block: RequestBuilder.() -> Unit): Result<Response> {
        return runCatching {
            makeRequest("PUT", path, block)
        }
    }

    fun deleteWithResult(path: String, block: RequestBuilder.() -> Unit): Result<Response> {
        return runCatching {
            makeRequest("DELETE", path, block)
        }
    }



    // DSL for HTTP Requests
    class RequestBuilder(private val baseUrl: String?) {
        private val headers = mutableListOf<Pair<String, String>>()
        private var url: String? = null
        private var method: String = "GET"
        private var body: RequestBody? = null
        private var timeout: Long = 30L
        private var loggingEnabled = false

        fun url(path: String) {
            this.url = baseUrl?.let { "$it$path" } ?: path
        }

        fun method(method: String) { this.method = method }
        fun body(body: RequestBody) { this.body = body }
        fun timeout(seconds: Long) { this.timeout = seconds }
        fun header(name: String, value: String) { headers.add(name to value) }
        fun logHeaders(enable: Boolean) { loggingEnabled = enable }

        fun build(): Request {
            val requestBuilder = Request.Builder()
            url?.let { requestBuilder.url(it) }
            requestBuilder.method(method, body)

            headers.forEach { (name, value) ->
                requestBuilder.addHeader(name, value)
            }

            return requestBuilder.build()
        }
    }
}

fun krazeClient(block: KrazeBuilder.() -> Unit): NetworkClient {
    val builder = KrazeBuilder()
    builder.block()
    val baseUrl = builder.getBaseUrl()
    val serialization = builder.getSerialization()
    return NetworkClient(network(block), baseUrl, serialization)
}

