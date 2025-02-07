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


import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.kraze.Serialization
import kotlin.reflect.KClass

class JacksonSerialization : Serialization {
    private val objectMapper: ObjectMapper
    constructor() : super() {
        this.objectMapper = ObjectMapper().registerModule(KotlinModule.Builder().build())
    }
    constructor(objectMapper: ObjectMapper) : super() {
        this.objectMapper = objectMapper
    }

    override fun <T : Any> decodeFromString(type: KClass<T>, string: String): T {
        return objectMapper.readValue(string, type.java)
    }

    override fun <T : Any> encodeToString(type: KClass<T>, value: T): String {
        return objectMapper.writeValueAsString(value)
    }
}