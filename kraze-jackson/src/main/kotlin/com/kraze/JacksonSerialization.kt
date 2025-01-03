package com.kraze

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import kotlin.reflect.KClass

class JacksonSerialization : Serialization {
    private val objectMapper = ObjectMapper().registerModule(KotlinModule.Builder().build())

    override fun <T : Any> decodeFromString(type: KClass<T>, string: String): T {
        return objectMapper.readValue(string, type.java)
    }

    override fun <T : Any> encodeToString(type: KClass<T>, value: T): String {
        return objectMapper.writeValueAsString(value)
    }
}