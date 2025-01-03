package com.kraze

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.squareup.moshi.JsonAdapter
import com.kraze.Serialization
import kotlin.reflect.KClass

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
