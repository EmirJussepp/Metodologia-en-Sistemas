package utn.methodology.infrastructure.http.router

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import utn.methodology.application.commandhandlers.CreateUserCommandHandler
import utn.methodology.infrastructure.http.actions.ConfirmUserAction
import utn.methodology.application.commands.CreateUserCommand
import utn.methodology.infrastructure.persistence.connectToMongoDB
import utn.methodology.infrastructure.persistence.UserRepository
import utn.methodology.application.commands.ActualizarUsuarioCommand
import utn.methodology.application.commandhandlers.ActualizarUsuarioCommandHandler



fun Application.userRoutes() {
    val mongoDatabase = connectToMongoDB()
    val userRepository = UserRepository(mongoDatabase)
    val confirmUserAction = ConfirmUserAction(CreateUserCommandHandler(userRepository))

    routing {
        post("/users") {
            val body = call.receive<CreateUserCommand>()

            try {
                body.validate() // Validar aquí
                confirmUserAction.execute(body)
                call.respond(HttpStatusCode.Created, mapOf("message" to "User Created Successfully"))
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
            }
        }
        
        get("/users/{username}") {
            val username = call.parameters["username"]
            if (username.isNullOrBlank()) {
                call.respond(HttpStatusCode.BadRequest, "Username is required")
                return@get
            }

            val users = userRepository.findByField("username", username)

            if (users.isEmpty()) {
                call.respond(HttpStatusCode.NotFound, "User not found")
            } else {
                call.respond(HttpStatusCode.OK, users)
            }
        }
    }
}
