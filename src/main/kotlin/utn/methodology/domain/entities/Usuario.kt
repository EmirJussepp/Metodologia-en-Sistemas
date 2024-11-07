package utn.methodology.domain.entities

import kotlinx.serialization.Serializable
import java.util.UUID
import utn.methodology.domain.valueObjets.*

@Serializable
  data class User (
  val uuid: String,
  val username: String,
  val surname: String,
  val email: String,
  val password: String,
  var seguidos: List<String> = listOf(),
  var seguidores: List<String> = listOf()
) {
  private var status: Status?
    get() = null
    set(value) = TODO()

  companion object {
    fun fromPrimitives(primitives: Map<String, Any>): User {
      /*filterIsInstance<String>()este método filtra solo los elementos de tipo String en la lista. Así, evitamos un unchecked cast porque nos aseguramos de que todos los elementos sean String.*/
      val seguidos = (primitives["seguidos"] as? List<*>)?.filterIsInstance<String>() ?: listOf()
      val seguidores = (primitives["seguidores"] as? List<*>)?.filterIsInstance<String>() ?: listOf()

      val user = User(
        primitives["uuid"] as? String ?: "",
        primitives["username"] as? String ?: "",
        primitives["surname"] as? String ?: "",
        primitives["email"] as? String ?: "",
        primitives["password"] as? String ?: "",
        seguidos,
        seguidores
      )

      (primitives["status"] as? Map<*, *>)?.let { statusMap ->
        if (statusMap.keys.all { it is String } && statusMap.values.all { it is String }) {
          @Suppress("UNCHECKED_CAST")
          user.status = Status.fromPrimitives(statusMap as Map<String, String>)
        }
      }

      return user
    }


  fun create(
      username: String,
      surname: String,
      email: String,
      password: String,
    ) : User {
      val user = User(UUID.randomUUID().toString(),
        username,
        surname,
        email,
        password)

      return user
    }


  }
  fun toPrimitives(): Map<String, Any?> {
    return mapOf(
      "uuid" to this.uuid,
      "username" to this.username,
      "surname" to this.surname,
      "email" to this.email,
      "password" to this.password,
      "seguidores" to this.seguidores,
      "seguidos" to this.seguidos
    )
  }

  fun getUserId(): String {
    return this.uuid;
  }

}

