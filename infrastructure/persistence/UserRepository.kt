package utn.methodology.infrastructure.persistence

import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import org.bson.Document
import utn.methodology.application.commands.CreateUser

object UserRepository {
    private lateinit var collection: MongoCollection<Document>

    fun initialize(database: MongoDatabase) {
        collection = database.getCollection("users")
    }

    fun addUser(command: CreateUser) {
        val userDocument = Document()
            .append("username", command.username)
            .append("email", command.email)
            .append("password", hashPassword(command.password))
        collection.insertOne(userDocument)
    }

    private fun hashPassword(password: String): String {
        return password.reversed()
    }
}