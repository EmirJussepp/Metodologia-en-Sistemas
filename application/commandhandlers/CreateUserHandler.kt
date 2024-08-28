package utn.methodology.application.commandhandlers

import utn.methodology.application.commands.CreateUser
import utn.methodology.infrastructure.persistence.UserRepository

class CreateUserHandler {
    fun handle(command: CreateUser) {
        if (command.errors.isNotEmpty()) {
            throw IllegalArgumentException("Errores en el comando: ${command.errors.joinToString(", ")}")
        }

        // Almacenar el usuario en la base de datos
        UserRepository.addUser(command)
        println("Usuario creado y almacenado en MongoDB.")
    }
}
