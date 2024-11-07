package utn.methodology.application.commandhandlers

import utn.methodology.application.commands.CreateUserCommand
import utn.methodology.domain.contracts.IUserRepository
import utn.methodology.domain.entities.User
import utn.methodology.infrastructure.utils.Response

class CreateUserCommandHandler(
  private val userRepository: IUserRepository
) {
  fun handle(command: CreateUserCommand): Response {
    // Validamos el comando
    return try {
      command.validate() // Llama al método de validación del comando

      // Creamos el usuario
      val user = User.create(
        command.username,
        command.surname,
        command.email,
        command.password
      )
      userRepository.save(user)

      // Retornamos la respuesta
      Response(201, "User created successfully")
    } catch (e: IllegalArgumentException) {
      Response(400, e.message ?: "Invalid user data")
    }
  }
}
