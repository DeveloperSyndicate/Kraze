package com.developersyndicate.kraze

import com.developersyndicate.kraze.auth.AuthenticationProvider
import com.developersyndicate.kraze.logging.KrazeLoggingInterceptor
import okhttp3.*
import java.io.File
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
    private var authProvider: AuthenticationProvider? = null
    private var baseUrl: String? = null
    private var logLevel: KrazeLoggingInterceptor? = null
    private var serialization: Serialization? = null
    private var cache: Cache? = null

    /**
     * Sets the base URL for the HTTP client.
     *
     * @param url The base URL as a string. This will be used as the prefix for all relative API endpoints.
     */
    fun baseUrl(url: String) {
        baseUrl = url
    }

    /**
     * Configures the logging level for HTTP requests and responses.
     *
     * @param level The desired log level using `KrazeLoggingInterceptor.Level`.
     *              Example: `KrazeLoggingInterceptor.Level.BODY`.
     */
    fun logLevel(level: KrazeLoggingInterceptor.Level) {
        this.logLevel = KrazeLoggingInterceptor(level)
    }

    /**
     * Adds a custom OkHttp interceptor to the HTTP client.
     *
     * @param interceptor An instance of `Interceptor` to customize request/response handling.
     */
    fun interceptor(interceptor: Interceptor) {
        interceptors.add(interceptor)
    }

    /**
     * Configures the timeout for establishing a connection.
     *
     * @param timeout The timeout duration.
     * @param unit The time unit for the timeout (default: `TimeUnit.SECONDS`).
     */
    fun connectTimeout(timeout: Long, unit: TimeUnit = TimeUnit.SECONDS) {
        connectTimeout = unit.toMillis(timeout)
    }

    /**
     * Configures the timeout for reading data from the server.
     *
     * @param timeout The timeout duration.
     * @param unit The time unit for the timeout (default: `TimeUnit.SECONDS`).
     */
    fun readTimeout(timeout: Long, unit: TimeUnit = TimeUnit.SECONDS) {
        readTimeout = unit.toMillis(timeout)
    }

    /**
     * Configures the timeout for writing data to the server.
     *
     * @param timeout The timeout duration.
     * @param unit The time unit for the timeout (default: `TimeUnit.SECONDS`).
     */
    fun writeTimeout(timeout: Long, unit: TimeUnit = TimeUnit.SECONDS) {
        writeTimeout = unit.toMillis(timeout)
    }

    /**
     * Sets the connection pool configuration for managing HTTP connections.
     *
     * @param pool An instance of `ConnectionPool` for controlling connection reuse and management.
     */
    fun connectionPool(pool: ConnectionPool) {
        connectionPool = pool
    }

    /**
     * Sets an OkHttp authenticator for handling authentication challenges.
     *
     * @param authenticator The `Authenticator` instance to use.
     */
    fun authenticator(authenticator: Authenticator) {
        this.authenticator = authenticator
    }

    /**
     * Sets a kraze authentication provider for adding authentication headers.
     *
     * @param authenticator The `AuthenticationProvider` instance to use.
     */
    fun authenticator(authenticator: AuthenticationProvider) {
        this.authProvider = authenticator
    }

    /**
     * Configures the serialization mechanism for encoding and decoding JSON.
     *
     * @param serialization The `Serialization` instance to handle serialization and deserialization.
     */
    fun serializer(serialization: Serialization) {
        this.serialization = serialization
    }

    /**
     * Configures HTTP caching for the client.
     *
     * @param cacheDir The directory where the cache will be stored.
     * @param cacheSize The maximum size of the cache (default: 10 MB).
     */
    fun cache(cacheDir: File, cacheSize: Long = 10L * 1024 * 1024) {
        this.cache = Cache(cacheDir, cacheSize)
    }

    /**
     * Builds and returns a configured OkHttpClient instance.
     *
     * @return A fully configured instance of `OkHttpClient`.
     */
    fun build(): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .connectTimeout(connectTimeout, TimeUnit.SECONDS)
            .readTimeout(readTimeout, TimeUnit.SECONDS)
            .writeTimeout(writeTimeout, TimeUnit.SECONDS)
            .connectionPool(connectionPool)

        cache?.let { builder.cache(it) }
        interceptors.forEach { builder.addInterceptor(it) }
        logLevel?.let { builder.addInterceptor(it) }
        authProvider?.let { provider ->
            builder.addInterceptor { chain ->
                val originalRequest = chain.request()
                val requestBuilder = originalRequest.newBuilder()
                provider.addAuthenticationHeaders(requestBuilder)
                chain.proceed(requestBuilder.build())
            }
        }
        authenticator?.let { builder.authenticator(it) }


        return builder.build()
    }

    fun getBaseUrl(): String? {
        return baseUrl
    }
    fun getSerialization(): Serialization? {
        return serialization
    }

    fun getAuthProvider(): AuthenticationProvider? {
        return authProvider
    }
}

internal fun network(block: KrazeBuilder.() -> Unit): OkHttpClient {
    val builder = KrazeBuilder()
    builder.block()
    return builder.build()
}