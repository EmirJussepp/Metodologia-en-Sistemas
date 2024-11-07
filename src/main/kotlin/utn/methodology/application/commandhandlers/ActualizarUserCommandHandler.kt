package utn.methodology.application.commandhandlers

import utn.methodology.application.commands.ActualizarUsuarioCommand

import utn.methodology.domain.contracts.IUserRepository

class ActualizarUsuarioCommandHandler(private val userRepository: IUserRepository) {

    fun handle(command: ActualizarUsuarioCommand) {
        // Validar el comando
        command.validate()  // Validación de entrada

        // Recuperar el usuario existente desde el repositorio
        val usuarioExistente = userRepository.findByField("uuid", command.uuid).firstOrNull()
            ?: throw IllegalArgumentException("El usuario con el ID proporcionado no existe.")

        // Crear una copia del usuario existente, actualizando solo los campos no nulos del comando
        val usuarioActualizado = usuarioExistente.copy(
            username = command.username ?: usuarioExistente.username,
            surname = command.surname ?: usuarioExistente.surname,
            email = command.email ?: usuarioExistente.email,
            password = command.password ?: usuarioExistente.password

        )

        // Llamar al repositorio para actualizar el usuario
        if (!userRepository.actualizarUsuario(usuarioActualizado)) {
            throw IllegalStateException("Error al actualizar el usuario en la base de datos.")
        }
    }
}


