package utn.methodology.application.commandhandlers

import utn.methodology.application.commands.CreatePostCommand
import utn.methodology.domain.contracts.IPostRepository
import utn.methodology.domain.entities.Post
import utn.methodology.domain.contracts.IUserRepository

class CreatePostCommandHandler(
    private val postRepository: IPostRepository,
    private val userRepository: IUserRepository

) {
    fun handle(command: CreatePostCommand) {
        if (!userRepository.existsById(command.userId)) {
            throw IllegalArgumentException("El usuario con id ${command.userId} no existe")
        }

        val post = Post.create(
            command.userId,
            command.message,
        )
        postRepository.save(post)

    }
}