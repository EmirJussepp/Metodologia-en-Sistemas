package utn.methodology.infrastructure.persistence

import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters
import com.mongodb.client.model.UpdateOptions
import com.mongodb.client.model.Updates
import org.bson.Document
import utn.methodology.domain.contracts.IUserRepository
import utn.methodology.domain.entities.User



// Poner nombre correcto de la colección en MongoDB
const val collectionName: String = "user"


class UserRepository(private val database: MongoDatabase) : IUserRepository {

  private val collection : MongoCollection<Document>

  init {

    collection = this.database.getCollection(collectionName) as MongoCollection<Document>
  }

    override fun save(user: User) {
    println("UserRepository - Saving user: $user")
    val options = UpdateOptions().upsert(true)

    val filter = Document("_id", user.getUserId()) // Usa el campo uuid como filtro
    val update = Document("\$set", user.toPrimitives())

    collection.updateOne(filter, update, options)
  }

  override fun existsById(userId: String): Boolean {
    val filter = Document("_id", userId)
    val count = collection.countDocuments(filter)
    return count > 0
  }
    override fun findByField(field: String, value: String): List<User> {
        val filter = Document(field, value)
        val documents = collection.find(filter)

        return documents.mapNotNull {
            User.fromPrimitives(it as Map<String, Any>)
        }
    }

    override fun actualizarUsuario(user: User): Boolean {
        // Verificar si el usuario existe
        val filtro = Document("_id", user.uuid)
        val documentoExistente = collection.find(filtro).firstOrNull()
        if (documentoExistente == null) {
            return false // Usuario no encontrado
        }

        // Actualizar el usuario
        val update = Document("\$set", user.toPrimitives())
        collection.updateOne(filtro, update)

        return true // Actualización exitosa
    }



    override fun agregarALista(userId: String, campo: String, valor: String): Boolean {
        // Filtrar el documento por el userId
        val filtro = Filters.eq("_id", userId)

        // Preparar la actualización, agregando userIdAAgregar a la lista indicada (puede ser "seguidos" o "seguidores")
        val update = Updates.addToSet(campo, valor)

        // Ejecutar la actualización en la base de datos
        val resultado = collection.updateOne(filtro, update)

        // Retornar true si se realizó la modificación en la base de datos
        return resultado.modifiedCount > 0
    }

    override fun obtenerListaSeguidores(userId: String): List<String> {
        val filtro = Filters.eq("_id", userId)
        val userDocument = collection.find(filtro).firstOrNull()

        return userDocument?.let {
            // Acceder al campo "seguidores"
            val seguidos = it.get("seguidos")
            when (seguidos) {
                is List<*> -> seguidos.filterIsInstance<String>()  // Filtrar solo los elementos que son de tipo String
                else -> emptyList()  // Retornar lista vacía si no es del tipo esperado
            }
        } ?: emptyList()  // Retornar lista vacía si el documento o el campo no existe
    }














}