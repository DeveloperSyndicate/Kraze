import com.kraze.krazeClient
import com.kraze.logging.KrazeLoggingInterceptor
import com.kraze.serialization.GsonSerialization
import com.kraze.serialization.JacksonSerialization
import com.kraze.serialization.KotlinxSerialization
import com.kraze.serialization.MoshiSerialization
import kotlinx.serialization.Serializable

fun main() {


    val client = krazeClient {
        baseUrl("https://catfact.ninja/")
        logLevel(KrazeLoggingInterceptor.Level.BASIC)
        serializer(MoshiSerialization())
    }

//    val webSocket = krazeWebSocket("/chat", client) {
//        headers {
//            "Authorization" to "Bearer my-token"
//        }
//
//        onOpen { webSocket, response ->
//            println("WebSocket connection opened: $response")
//        }
//
//        onMessage { webSocket, text ->
//            println("Message received: $text")
//        }
//
//        onClosing { webSocket, code, reason ->
//            println("WebSocket is closing: $code / $reason")
//        }
//
//        onFailure { webSocket, throwable, response ->
//            println("WebSocket error: ${throwable.message}")
//        }
//    }
//
//    webSocket.send("")

//    client.get<Model>("fact").onSuccess {
//        println(it)
//    }.onFailure {
//        println(it)
//    }
//
//    println("hello")


//
//    val request2 = client.get("fact")
//    request2.body?.string()

}

@Serializable
data class Model(val fact: String, val length: Int)