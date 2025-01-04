package com.kraze.websocket

import com.kraze.NetworkClient
import okhttp3.WebSocket

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


fun krazeWebSocket(
    path: String,
    client: NetworkClient,
    block: WebSocketDSL.() -> Unit
): WebSocket {
    val dsl = WebSocketDSL(client)
    dsl.block()
    return dsl.build(path)
}