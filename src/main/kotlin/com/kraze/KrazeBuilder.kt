package com.kraze

import kotlinx.serialization.json.Json
import okhttp3.Authenticator
import okhttp3.ConnectionPool
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

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

class KrazeBuilder {
    private val interceptors = mutableListOf<Interceptor>()
    private var connectTimeout: Long = 30
    private var readTimeout: Long = 30
    private var writeTimeout: Long = 30
    private var connectionPool: ConnectionPool = ConnectionPool()
    private var authenticator: Authenticator? = null
    private var baseUrl: String? = null
    private var logLevel: HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.NONE
    private var serialization: Serialization? = null

    fun baseUrl(url: String) {
        baseUrl = url
    }

    fun interceptor(interceptor: Interceptor) {
        interceptors.add(interceptor)
    }

    fun connectTimeout(timeout: Long, unit: TimeUnit = TimeUnit.SECONDS) {
        connectTimeout = unit.toMillis(timeout)
    }

    fun readTimeout(timeout: Long, unit: TimeUnit = TimeUnit.SECONDS) {
        readTimeout = unit.toMillis(timeout)
    }

    fun writeTimeout(timeout: Long, unit: TimeUnit = TimeUnit.SECONDS) {
        writeTimeout = unit.toMillis(timeout)
    }

    fun connectionPool(pool: ConnectionPool) {
        connectionPool = pool
    }

    fun authenticator(authenticator: Authenticator) {
        this.authenticator = authenticator
    }

    fun logLevel(logLevel: HttpLoggingInterceptor.Level) {
        this.logLevel = logLevel
    }

    fun serializer(serialization: Serialization) {
        this.serialization = serialization
    }

    fun build(): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .connectTimeout(connectTimeout, TimeUnit.SECONDS)
            .readTimeout(readTimeout, TimeUnit.SECONDS)
            .writeTimeout(writeTimeout, TimeUnit.SECONDS)
            .connectionPool(connectionPool)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = logLevel
            })

        interceptors.forEach { builder.addInterceptor(it) }
        authenticator?.let { builder.authenticator(it) }

        return builder.build()
    }

    fun getBaseUrl(): String? {
        return baseUrl
    }
    fun getSerialization(): Serialization? {
        return serialization
    }
}

internal fun network(block: KrazeBuilder.() -> Unit): OkHttpClient {
    val builder = KrazeBuilder()
    builder.block()
    return builder.build()
}