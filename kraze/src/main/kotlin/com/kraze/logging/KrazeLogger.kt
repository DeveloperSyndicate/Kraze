package com.kraze.logging

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
 * Created: 20-11-2024
 */

internal object KrazeLogger {
    private const val ANDROID_LOG_TAG = "KrazeLogging"

    fun log(message: String) {
        if (isAndroid()) {
            logAndroid(ANDROID_LOG_TAG, message)
        } else {
            println(message)
        }
    }

    fun log(tag: String, message: String) {
        if (isAndroid()) {
            logAndroid(tag, message)
        } else {
            println("[$tag] $message")
        }
    }

    private fun isAndroid(): Boolean {
        return try {
            Class.forName("android.util.Log")
            true
        } catch (e: ClassNotFoundException) {
            false
        }
    }

    private fun logAndroid(tag: String, message: String) {
        try {
            val logClass = Class.forName("android.util.Log")
            val method = logClass.getMethod("d", String::class.java, String::class.java)
            method.invoke(null, tag, message)
        } catch (e: Exception) {
            println("Fallback: [$tag] $message")
        }
    }
}
