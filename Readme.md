# Kraze Documentation

## Overview
Kraze is a modern, Kotlin-first HTTP networking library built on top of OkHttp. It provides a flexible, type-safe API for making HTTP requests with built-in support for multiple serialization libraries and WebSocket connections.

## Core Components

### 1. Main Library (kraze)
The core library providing the base networking functionality.

### 2. Serialization Modules
- **kraze-gson**: Integration with Google's Gson
- **kraze-jackson**: Integration with Jackson
- **kraze-kotlinx-serialization**: Integration with Kotlin's official serialization library
- **kraze-moshi**: Integration with Square's Moshi

## Installation

Add the following to your `build.gradle.kts`:

```kotlin
// Core library
implementation("com.developersyndicate.kraze:kraze:1.0.0-alpha")

// Choose one or more serialization modules
implementation("com.developersyndicate.kraze:kraze-gson:1.0.0-alpha")
implementation("com.developersyndicate.kraze:kraze-jackson:1.0.0-alpha")
implementation("com.developersyndicate.kraze:kraze-kotlinx-serialization:1.0.0-alpha")
implementation("com.developersyndicate.kraze:kraze-moshi:1.0.0-alpha")
```

## Basic Usage

### Creating a Client

```kotlin
val client = krazeClient {
    baseUrl("https://api.example.com")
    logLevel(KrazeLoggingInterceptor.Level.BASIC)
    serializer(MoshiSerialization()) // Choose your preferred serializer
}
```

### Making Requests

#### GET Request
```kotlin
// Simple GET request
client.get<ResponseType>("endpoint").onSuccess { response ->
    println(response)
}.onFailure { error ->
    println(error)
}

// GET with query parameters
client.get<ResponseType>("endpoint") {
    queryParam("key", "value")
    header("Authorization", "Bearer token")
}
```

#### POST Request
```kotlin
client.post<ResponseType>("endpoint") {
    body(requestBody)
    header("Content-Type", "application/json")
}
```

## Serialization

### 1. Using Gson
```kotlin
val client = krazeClient {
    serializer(GsonSerialization())
}
```

### 2. Using Jackson
```kotlin
val client = krazeClient {
    serializer(JacksonSerialization())
}
```

### 3. Using Kotlinx Serialization
```kotlin
// Add @Serializable annotation to your data classes
@Serializable
data class User(val name: String, val age: Int)

val client = krazeClient {
    serializer(KotlinxSerialization())
}
```

### 4. Using Moshi
```kotlin
val client = krazeClient {
    serializer(MoshiSerialization())
}
```

## WebSocket Support

```kotlin
val webSocket = krazeWebSocket("/chat", client) {
    headers {
        "Authorization" to "Bearer token"
    }

    onOpen { webSocket, response ->
        println("Connection opened")
    }

    onMessage { webSocket, text ->
        println("Message received: $text")
    }

    onClosing { webSocket, code, reason ->
        println("Connection closing")
    }

    onFailure { webSocket, throwable, response ->
        println("Error: ${throwable.message}")
    }
}

// Send message
webSocket.send("Hello!")
```

## Advanced Configuration

### Logging Levels
```kotlin
krazeClient {
    logLevel(KrazeLoggingInterceptor.Level.NONE)    // No logging
    logLevel(KrazeLoggingInterceptor.Level.BASIC)   // Basic request/response info
    logLevel(KrazeLoggingInterceptor.Level.HEADERS) // Include headers
    logLevel(KrazeLoggingInterceptor.Level.BODY)    // Include request/response bodies
}
```

### Timeouts
```kotlin
krazeClient {
    connectTimeout(30) // seconds
    readTimeout(30)    // seconds
    writeTimeout(30)   // seconds
}
```

### Error Handling
```kotlin
client.get<ResponseType>("endpoint")
    .onSuccess { response ->
        // Handle success
    }
    .onFailure { error ->
        when (error) {
            is NetworkError -> // Handle network error
            is SerializationError -> // Handle serialization error
            else -> // Handle other errors
        }
    }
```

## Best Practices

1. **Serialization**: Choose a serialization library based on your needs:
  - Kotlinx Serialization: Best for Kotlin-first projects
  - Moshi: Good balance of features and performance
  - Gson: Widely used, good for Java interop
  - Jackson: Feature-rich, good for complex JSON handling

2. **Error Handling**: Always handle both success and failure cases using the Result API.

3. **Resource Management**: Close WebSocket connections when no longer needed.

4. **Logging**: Use appropriate log levels for different environments:
  - BODY for development
  - BASIC for staging
  - NONE for production

## Common Patterns

### Repository Pattern
```kotlin
class UserRepository(private val client: KrazeClient) {
    suspend fun getUser(id: String): Result<User> {
        return client.get("users/$id")
    }

    suspend fun createUser(user: User): Result<User> {
        return client.post("users") {
            body(user)
        }
    }
}
```

### Service Pattern
```kotlin
class AuthService(private val client: KrazeClient) {
    suspend fun login(credentials: Credentials): Result<Token> {
        return client.post("auth/login") {
            body(credentials)
        }
    }
}
```

## Contributing

1. Fork the repository
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE file](LICENSE) for details.


## Acknowledgments
This library is built on top of OkHttp by Square, and we extend our gratitude to their amazing work. We also thank the community for tools like Kotlinx Serialization, Gson, Jackson and Moshi that enable efficient data handling.