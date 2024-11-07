package utn.methodology.application.commandhandlers

import utn.methodology.domain.contracts.IPostRepository
import utn.methodology.domain.contracts.IUserRepository
import utn.methodology.domain.entities.Post
import utn.methodology.application.commands.PostsSeguidosCommand

class PostsSeguidosCommandHandler(
    private val userRepository: IUserRepository,
    private val postRepository: IPostRepository
) {
    fun handle(command: PostsSeguidosCommand): List<Post> {
        val userId = command.userIds

        // Obtener los usuarios seguidos por el usuario
        val seguidos = userRepository.obtenerListaSeguidores(userId)

        // Validación para ver si el usuario sigue a alguien
        if (seguidos.isEmpty()) {
            throw IllegalArgumentException("El usuario no sigue a nadie.")
        }

        // Obtener y filtrar los posts de los usuarios seguidos, ordenados por fecha de creación
        return postRepository.findPostsByUsers(seguidos).sortedByDescending { it.createdAt }
    }
}
