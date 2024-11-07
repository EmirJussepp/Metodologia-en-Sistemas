package utn.methodology.application.commands

import kotlinx.serialization.Serializable

@Serializable
class SeguirUserCommand(
    val userId: String, // ID del usuario que quiere seguir a otro
    val SeguirUserId: String // ID del usuario que va a ser seguido
)
