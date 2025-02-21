package com.developersyndicate.kraze.auth

import com.developersyndicate.kraze.utils.toBase64
import okhttp3.Request

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


class BasicAuthenticationProvider(private val username: String, private val password: String) : AuthenticationProvider {
    override fun addAuthenticationHeaders(builder: Request.Builder) {
        val credentials = "$username:$password".toBase64()
        builder.addHeader("Authorization", "Basic $credentials")
    }
}
