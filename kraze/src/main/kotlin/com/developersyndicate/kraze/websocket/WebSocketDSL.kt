package com.developersyndicate.kraze.websocket

import com.developersyndicate.kraze.NetworkClient
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

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


class WebSocketDSL(private val client: NetworkClient) {
    private val headers = mutableMapOf<String, String>()
    private var onOpen: ((WebSocket, Response) -> Unit)? = null
    private var onMessage: ((WebSocket, String) -> Unit)? = null
    private var onClosing: ((WebSocket, Int, String) -> Unit)? = null
    private var onClosed: ((WebSocket, Int, String) -> Unit)? = null
    private var onFailure: ((WebSocket, Throwable, Response?) -> Unit)? = null

    fun headers(block: MutableMap<String, String>.() -> Unit) {
        headers.apply(block)
    }

    fun onOpen(block: (WebSocket, Response) -> Unit) {
        onOpen = block
    }

    fun onMessage(block: (WebSocket, String) -> Unit) {
        onMessage = block
    }

    fun onClosing(block: (WebSocket, Int, String) -> Unit) {
        onClosing = block
    }

    fun onClosed(block: (WebSocket, Int, String) -> Unit) {
        onClosed = block
    }

    fun onFailure(block: (WebSocket, Throwable, Response?) -> Unit) {
        onFailure = block
    }

    fun build(path: String): WebSocket {
        val webSocketListener = object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                onOpen?.invoke(webSocket, response)
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                onMessage?.invoke(webSocket, text)
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                onClosing?.invoke(webSocket, code, reason)
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                onClosed?.invoke(webSocket, code, reason)
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                onFailure?.invoke(webSocket, t, response)
            }
        }

        return client.newWebSocket(path, {
            headers.forEach { (key, value) -> header(key, value) }
        }, webSocketListener)
    }
}
