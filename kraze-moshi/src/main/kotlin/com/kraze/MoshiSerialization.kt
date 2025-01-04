package com.kraze

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlin.reflect.KClass

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

class MoshiSerialization : Serialization {
    private val moshi: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    override fun <T : Any> decodeFromString(type: KClass<T>, string: String): T {
        val jsonAdapter: JsonAdapter<T> = moshi.adapter(type.java)
        return jsonAdapter.fromJson(string) ?: throw IllegalStateException("Error deserializing")
    }

    override fun <T : Any> encodeToString(type: KClass<T>, value: T): String {
        val jsonAdapter: JsonAdapter<T> = moshi.adapter(type.java)
        return jsonAdapter.toJson(value)
    }
}
