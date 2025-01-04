package com.kraze.logging

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okio.Buffer

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
 * Created: 04-01-2025
 */


class KrazeLoggingInterceptor(private val logLevel: Level = Level.BASIC) : Interceptor {
    enum class Level {
        NONE,       // No logging
        BASIC,      // Log method, URL, and status code
        HEADERS,    // Log headers along with BASIC details
        BODY        // Log full request and response bodies
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        if (logLevel == Level.NONE) return chain.proceed(chain.request())

        val request = chain.request()
        val startTime = System.nanoTime()
        logRequest(request)

        val response: Response
        try {
            response = chain.proceed(request)
        } catch (e: Exception) {
            KrazeLogger.log("HTTP Request failed: ${e.message}")
            throw e
        }

        val endTime = System.nanoTime()
        val duration = (endTime - startTime) / 1e6
        logResponse(response, duration)

        return response
    }

    private fun logRequest(request: Request) {
        KrazeLogger.log("===== HTTP Request =====")
        KrazeLogger.log("${request.method} ${request.url}")

        if (logLevel >= Level.HEADERS) {
            KrazeLogger.log("Headers:")
            request.headers.forEach { header ->
                KrazeLogger.log("${header.first}: ${header.second}")
            }
        }

        if (logLevel >= Level.BODY && request.body != null) {
            val buffer = Buffer()
            request.body?.writeTo(buffer)
            KrazeLogger.log("Body:")
            KrazeLogger.log(buffer.readUtf8())
        }
    }

    private fun logResponse(response: Response, duration: Double) {
        KrazeLogger.log("===== HTTP Response =====")
        KrazeLogger.log("URL: ${response.request.url}")
        KrazeLogger.log("Status Code: ${response.code}")
        KrazeLogger.log("Time: ${"%.2f".format(duration)} ms")

        if (logLevel >= Level.HEADERS) {
            KrazeLogger.log("Headers:")
            response.headers.forEach { header ->
                KrazeLogger.log("${header.first}: ${header.second}")
            }
        }

        if (logLevel >= Level.BODY) {
            val responseBody = response.peekBody(Long.MAX_VALUE)
            KrazeLogger.log("Body: ${responseBody.string()}")
        }
    }
}
