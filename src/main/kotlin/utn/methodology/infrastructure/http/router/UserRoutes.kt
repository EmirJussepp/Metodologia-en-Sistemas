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
import utn.methodology.application.commands.SeguirUserCommand
import utn.methodology.application.commandhandlers.SeguirUsuarioCommandHandler


fun Application.userRoutes() {
    val mongoDatabase = connectToMongoDB()
    val userRepository = UserRepository(mongoDatabase)
    val confirmUserAction = ConfirmUserAction(CreateUserCommandHandler(userRepository))
     val seguirUsuarioCommandHandler = SeguirUsuarioCommandHandler(userRepository)
    

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
        
        patch("/users/{id}") {
            val userId = call.parameters["id"] ?: return@patch call.respond(HttpStatusCode.BadRequest, "ID del usuario es requerido")

            // Recibir el cuerpo de la solicitud
            val command = call.receive<ActualizarUsuarioCommand>()

            // Crear el handler y ejecutar
            val commandHandler = ActualizarUsuarioCommandHandler(userRepository)

            try {
                // Crear un nuevo comando con el ID de usuario y datos a actualizar
                val updatedCommand = ActualizarUsuarioCommand(
                    uuid = userId,
                    username = command.username,
                    surname = command.surname,
                    email = command.email,
                    password = command.password
                )

                // Manejar el comando
                commandHandler.handle(updatedCommand)
                call.respond(HttpStatusCode.OK, "Usuario actualizado correctamente")

            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "Error al actualizar el usuario: ${e.message}")
            }
        }




        post("/users/{userId}/follow/{seguirUserId}") {
            val userId = call.parameters["userId"] ?: return@post call.respond(HttpStatusCode.BadRequest, "ID del usuario es requerido")
            val seguirUserId = call.parameters["seguirUserId"] ?: return@post call.respond(HttpStatusCode.BadRequest, "ID del usuario a seguir es requerido")

            val command = SeguirUserCommand(userId, seguirUserId)

            try {
                seguirUsuarioCommandHandler.handle(command) // Llamar al método handle en la instancia
                call.respond(HttpStatusCode.OK, "Usuario seguido correctamente")
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, e.message ?: "Error en la solicitud")
            } catch (e: IllegalStateException) {
                call.respond(HttpStatusCode.InternalServerError, e.message ?: "Error al seguir al usuario")
            }
        }
    }
}
