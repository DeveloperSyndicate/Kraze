package com.kraze

import kotlin.reflect.KClass

interface Serialization {
    fun <T : Any> decodeFromString(type: KClass<T>, string: String): T
    fun <T: Any> encodeToString(type: KClass<T>, value: T): String
}
