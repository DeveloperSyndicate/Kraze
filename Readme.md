# Kraze Network Library

`Kraze` is a Kotlin-based, lightweight, and extensible HTTP networking library built on top of OkHttp. It provides a fluent API for making HTTP requests with support for serialization, authentication, and custom configuration.

## Table of Contents

- [Installation](#installation)
- [Features](#features)
- [Quick Start](#quick-start)
- [Usage](#usage)
    - [Creating a Network Client](#creating-a-network-client)
    - [Making HTTP Requests](#making-http-requests)
    - [Serialization](#serialization)
    - [Authentication](#authentication)
    - [Custom Configuration](#custom-configuration)
- [RequestBuilder DSL](#requestbuilder-dsl)
- [Error Handling](#error-handling)
- [Full Example](#full-example)
- [Contribution](#contribution)
- [License](#license)
- [Acknowledgments](#acknowledgments)

---

## Installation

Add the following dependency to your `build.gradle.kts`:

```kotlin
implementation("com.example:kraze-network:1.0.0")
```

## Features
- **HTTP Methods:** Supports all common HTTP methods like GET, POST, PUT, DELETE, etc.
- **Serialization Support:** Seamlessly decode responses into Kotlin objects.
- **Authentication:** Built-in support for adding authentication headers.
- **RequestBuilder DSL:** A flexible DSL for configuring requests.
- **Timeouts and Connection Pooling:** Customize timeouts and connection pooling for optimized performance.
- **Logging:** Configurable logging for debugging HTTP requests and responses.

## Quick Start
Here's a quick example of how to use Kraze:
```kotlin
val client = krazeClient {
    baseUrl("https://api.example.com")
    logLevel(KrazeLoggingInterceptor.Level.BODY)
    connectTimeout(15)
}
val response = client.get("/endpoint") {
  queryParam("key", "value")
  header("Authorization", "Bearer token")
}

println(response.body?.string())

// Get with Result method
val response2 = client.getWithResult("/endpoint") {
    queryParam("key", "value")
    header("Authorization", "Bearer token")
}
response2.onSuccess {
    println("Success: $it")
}.onFailure {
    println("Error: ${it.message}")
}
```

## Usage
### Creating a Network Client
Create a NetworkClient using the krazeClient function:
```kotlin
val client = krazeClient {
    baseUrl("https://api.example.com")
    connectTimeout(30) // Timeout in seconds
    readTimeout(30)
    writeTimeout(30)
    logLevel(KrazeLoggingInterceptor.Level.BODY)
    serializer(GsonSerialization()) // Optional: for JSON parsing
}
```
## Making HTTP Requests
You can use methods like get, post, put, delete, etc., for making requests.
### GET Request
```kotlin
val response: Response = client.get("/users") {
    queryParam("id", "123")
}
```
### POST Request
```kotlin
val response: Response = client.post("/users") {
    body(RequestBody.create(MediaType.get("application/json"), "{ \"name\": \"John\" }"))
}
```
### Serialization
You can parse HTTP responses into Kotlin objects if a serialization adapter is configured:
```kotlin
val user: Result<User> = client.get("/users/123")
user.onSuccess {
    println("User name: ${it.name}")
}
```
### Authentication
Add an AuthenticationProvider to include authentication headers:
```kotlin
val client = krazeClient {
    baseUrl("https://api.example.com")
    authenticator(TokenAuthenticationProvider("bearer-token-here"))
}
```
### Custom Configuration
You can customize logging, timeouts, connection pooling, and caching:
```kotlin
val client = krazeClient {
    baseUrl("https://api.example.com")
    logLevel(KrazeLoggingInterceptor.Level.HEADERS)
    connectTimeout(10)
    readTimeout(10)
    writeTimeout(10)
    connectionPool(ConnectionPool(5, 5, TimeUnit.MINUTES))
    cache(File("cache_dir"), 10L * 1024 * 1024) // 10 MB cache
}
```

## RequestBuilder DSL
The RequestBuilder DSL allows you to configure requests flexibly:
```kotlin
client.get("/users") {
    queryParam("id", "123")
    header("Accept", "application/json")
    timeout(60) // Override default timeout
    multipartField("field_name", "value")
    multipartFile("file", "filename.txt", file, MediaType.parse("text/plain")!!)
}
```

## Error Handling
Handle errors using Kotlin's Result API:
```kotlin
val result: Result<Response> = client.getWithResult("/users")
result.onSuccess { response ->
    println("Success: ${response.body()?.string()}")
}.onFailure { error ->
    println("Error: ${error.message}")
}
```

## Full Example
```kotlin
val client = krazeClient {
    baseUrl("https://api.example.com")
    logLevel(KrazeLoggingInterceptor.Level.BODY)
    serializer(JacksonSerialization()) // Gson/Kotlinx serialization/Moshi/Jackson
    connectTimeout(30)
    readTimeout(30)
    writeTimeout(30)
}

val response: Result<MyResponse> = client.get("/endpoint") {
    queryParam("key", "value")
    header("Authorization", "Bearer token")
}

response.onSuccess {
    println("Success: $it")
}.onFailure {
    println("Error: ${it.message}")
}
```
## Contribution
We welcome contributions! To contribute:

1. Fork the repository.
2. Create a new branch for your feature or bugfix: git checkout -b feature-name.
3. Make your changes and test them.
4. Commit your changes: git commit-m "Add feature-name".
5. Push to your branch: git push origin feature-name.
6. Open a pull request on GitHub.

Please ensure all contributions adhere to the following:
- Write clear and concise commit messages.
- Include appropriate unit tests.
- Follow the existing coding style.

## License
This library is licensed under the Apache 2.0 License. See the LICENSE file for more details.

## Acknowledgments
This library is built on top of OkHttp by Square, and we extend our gratitude to their amazing work. We also thank the community for tools like Kotlinx Serialization, Gson, Jackson and Moshi that enable efficient data handling.