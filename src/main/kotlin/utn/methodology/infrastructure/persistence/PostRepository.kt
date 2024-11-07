package utn.methodology.infrastructure.persistence

import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.UpdateOptions
import org.bson.Document
import com.mongodb.client.model.Sorts
import utn.methodology.domain.contracts.IPostRepository
import utn.methodology.domain.entities.Post


// Nombre de la colección en MongoDB para los posts
const val postCollectionName: String = "posts"
class PostRepository(private val database: MongoDatabase) : IPostRepository {

    private val collection: MongoCollection<Document> // Cambiar a Document

    init {
        collection = this.database.getCollection(postCollectionName) as MongoCollection<Document>
    }

    // Guarda el post, haciendo upsert
    override fun save(post: Post) {
        println("PostRepository - Saving post: ${post.message}")
        val options = UpdateOptions().upsert(true)

        val filter = Document("_id", post.uuid) // Usa el campo uuid como filtro para identificar el post
        val update = Document("\$set", post.toPrimitives())

        collection.updateOne(filter, update, options)
    }

    override fun findPostsByUserId(userId: String): List<Post> {
        val filter = Document("userId", userId)

        // Aquí puedes obtener documentos directamente como Document
        val primitives = collection.find(filter)
            .sort(Document("createdAt", 1)).toList() // Orden ascendente por "createdAt"

        return primitives.map { Post.fromPrimitives(it) } // Cambiado a `it`
    }

    override fun findById(id: String): Post? {
        val filter = Document("_id", id)
        val document = collection.find(filter).firstOrNull() ?: return null
        return Post.fromPrimitives(document) // Cambiado a `document`
    }

    override fun delete(id: String) {
        val filter = Document("_id", id)
        collection.deleteOne(filter)
    }

    override fun findPostsByUsers(userId: List<String>): List<Post> {
        val filter = Document("userId", Document("\$in", userId))
        val documents = collection.find(filter)

        // Asegúrate de que el casting sea seguro
        return documents.mapNotNull {
            Post.fromPrimitives(it) // Cambiado a `it`
        }
    }
}
