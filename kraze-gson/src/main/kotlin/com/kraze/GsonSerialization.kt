package com.kraze

import com.google.gson.Gson
import com.kraze.Serialization
import kotlin.reflect.KClass

class GsonSerialization: Serialization {
    private val gson = Gson()
    override fun <T : Any> decodeFromString(type: KClass<T>, string: String): T {
        return gson.fromJson(string, type.java)
    }

    override fun <T : Any> encodeToString(type: KClass<T>, value: T): String {
        return gson.toJson(value)
    }
}