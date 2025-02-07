package com.kraze.serialization

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


import com.kraze.Serialization
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import kotlin.reflect.KClass

class KotlinxSerialization : Serialization {
    private val json: Json
    constructor(json: Json) : super() {
        this.json = json
    }
    constructor(): super() {
        this.json = Json.Default
    }

    @OptIn(InternalSerializationApi::class)
    override fun <T : Any> decodeFromString(type: KClass<T>, string: String): T {
        return json.decodeFromString(type.serializer(), string)
    }

    @OptIn(InternalSerializationApi::class)
    override fun <T : Any> encodeToString(type: KClass<T>, value: T): String {
        return json.encodeToString(type.serializer(), value)
    }
}