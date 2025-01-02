package com.kraze

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import kotlin.reflect.KClass

class KotlinxSerialization : Serialization {
    private val json = Json

    @OptIn(InternalSerializationApi::class)
    override fun <T : Any> decodeFromString(type: KClass<T>, string: String): T {
        return json.decodeFromString(type.serializer(), string)
    }

    @OptIn(InternalSerializationApi::class)
    override fun <T : Any> encodeToString(type: KClass<T>, value: T): String {
        return json.encodeToString(type.serializer(), value)
    }
}
