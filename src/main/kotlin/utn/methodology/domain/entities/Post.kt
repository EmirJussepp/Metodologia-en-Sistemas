package utn.methodology.domain.entities


import kotlinx.serialization.Serializable
import java.util.UUID
import java.time.LocalDateTime

@Serializable
data class Post(
    val uuid: String,
    val userId: String,
    val message: String,
    val createdAt: String
) {

    // Método que convierte el Post a un Map de primitivos
    fun toPrimitives(): Map<String, Any> {
        return mapOf(
            "_id" to uuid,
            "userId" to userId,
            "message" to message,
            "createdAt" to createdAt.toString() // Almacena como string en MongoDB
        )
    }

    // Método que crea una instancia de Post desde un Map de primitivos
    companion object {
        fun fromPrimitives(data: Map<String, Any>): Post {
            return Post(
                uuid = data["_id"] as String,
                userId = data["userId"] as String,
                message = data["message"] as String,
                createdAt = data["createdAt"] as String
            )
        }

        // Método para crear un nuevo Post con los datos iniciales
        fun create(userId: String, message: String): Post {
            return Post(
                uuid = UUID.randomUUID().toString(),
                userId = userId,
                message = message,
                createdAt = LocalDateTime.now().toString()
            )

        }
    }
    fun getOwnerId(): String {
        return this.userId
    }
    fun getMensaje(): String {
        return this.message
    }

}
