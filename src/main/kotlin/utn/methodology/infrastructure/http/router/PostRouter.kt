package utn.methodology.infrastructure.http.router



import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import utn.methodology.application.commandhandlers.CreatePostCommandHandler
import utn.methodology.infrastructure.http.actions.ConfirmPostAction
import utn.methodology.application.commands.CreatePostCommand
import utn.methodology.infrastructure.persistence.connectToMongoDB
import utn.methodology.infrastructure.persistence.PostRepository
import utn.methodology.infrastructure.persistence.UserRepository
import utn.methodology.application.commandhandlers.PostsSeguidosCommandHandler
import utn.methodology.application.commands.PostsSeguidosCommand

fun Application.postRoutes() {
    val mongoDatabase = connectToMongoDB()
    val postRepository = PostRepository(mongoDatabase)
    val userRepository = UserRepository(mongoDatabase)  // Repositorio de usuarios

    // Pasar ambos repositorios al manejador de comandos
    val createPostCommandHandler = CreatePostCommandHandler(postRepository, userRepository)
    val confirmPostAction = ConfirmPostAction(createPostCommandHandler)
    val obtenerPostsSeguidosHandler = PostsSeguidosCommandHandler(userRepository, postRepository)


    routing {
        post("/posts") {
            val body = call.receive<CreatePostCommand>()
            try{
            body.validate()
            confirmPostAction.execute(body)
            call.respond(HttpStatusCode.Created, mapOf("message" to "Post Created Successfully"))
            }
            catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
            }
        }


        // Ruta para listar todos los posts
        get("/posts/{userId}") {
            val userId = call.parameters["userId"]
            if (userId.isNullOrBlank()) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "User ID is required"))
                return@get
            }

            val posts = postRepository.findPostsByUserId(userId)

            if (posts.isEmpty()) {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "No posts found"))
            } else {
                call.respond(HttpStatusCode.OK, posts)
            }
        }

        delete("/posts/{id}") {
            val postId = call.parameters["id"]


            if (postId == null) {
                call.respond(HttpStatusCode.BadRequest, "Post ID faltante")
                return@delete
            }

            try {
                val post = postRepository.findById(postId)
                if (post == null) {
                    call.respond(HttpStatusCode.NotFound, "Post no encontrado")
                    return@delete
                }

                postRepository.delete(postId)
                call.respond(HttpStatusCode.OK, "Post eliminado exitosamente")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "Error eliminando post: ${e.message}")
            }
        }
        get("/posts/users/{userId}") {
            // Obtiene el ID del usuario de los parámetros de la solicitud
            val userId = call.parameters["userId"]?.trim() ?: return@get call.respond(HttpStatusCode.BadRequest, "ID del usuario es requerido")


            val command = PostsSeguidosCommand(userId)

            try {
                // Maneja el comando y obtiene los posts
                val posts = obtenerPostsSeguidosHandler.handle(command)
                // Responde con los posts encontrados
                call.respond(HttpStatusCode.OK, posts)
            } catch (e: IllegalArgumentException) {
                // Responde con un error 400 si hay una excepción de argumento ilegal
                call.respond(HttpStatusCode.BadRequest, e.message ?: "Error en la solicitud")
            }
        }

    }




}







