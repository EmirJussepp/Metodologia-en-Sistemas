package utn.methodology.application.commandhandlers

import utn.methodology.domain.contracts.IUserRepository
import utn.methodology.application.commands.SeguirUserCommand

class SeguirUsuarioCommandHandler(private val userRepository: IUserRepository) {

    fun handle(command: SeguirUserCommand) {
        // Verificar que ambos usuarios existan
        val usuarioSeguidor = userRepository.findByField("_id", command.userId)
        val usuarioSeguido = userRepository.findByField("_id", command.SeguirUserId)

        if (usuarioSeguidor.isEmpty()) {
            throw IllegalStateException("El usuario seguidor no existe")
        }
        if(usuarioSeguido.isEmpty()){
            throw IllegalArgumentException("El usuario a seguir no existe")
        }

        // Validar si el usuario está tratando de seguirse a sí mismo
        if (command.userId == command.SeguirUserId) {
            throw IllegalArgumentException("No puedes seguirte a ti mismo")
        }
        // Verificar si ya sigue al usuario
        val seguidos = userRepository.obtenerListaSeguidores(command.userId)
        if (seguidos.contains(command.SeguirUserId)) {
            throw IllegalArgumentException("Ya sigues a este usuario")
        }
        // Verificar si ya sigue al usuario


        // Agregar el usuario a la lista de seguidos
        // Agregar el usuario a la lista de seguidos del usuario seguidor
        val seguidosActualizado = userRepository.agregarALista(command.userId, "seguidos", command.SeguirUserId)

        // Agregar el usuario seguidor a la lista de seguidores del usuario seguido
        val seguidoresActualizado = userRepository.agregarALista(command.SeguirUserId, "seguidores", command.userId)

        // Verificar que ambas actualizaciones se realizaron correctamente
        if (!seguidosActualizado || !seguidoresActualizado) {
            throw IllegalArgumentException("Error al seguir al usuario")
        }

    }
}
