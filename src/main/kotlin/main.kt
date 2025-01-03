import com.kraze.*
import kotlinx.serialization.Serializable

fun main() {
    val client = krazeClient {
        baseUrl("https://catfact.ninja/")
        serializer(KotlinxSerialization())
    }

    client.get<Model>("fact") {

    }.onSuccess {
        println(it)
    }
}

@Serializable
data class Model(val fact: String, val length: Int)