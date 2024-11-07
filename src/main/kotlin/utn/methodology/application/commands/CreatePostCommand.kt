package utn.methodology.application.commands

import kotlinx.serialization.Contextual
import utn.methodology.infrastructure.persistence.UserRepository
import utn.methodology.domain.entities.Post
import kotlinx.serialization.Serializable

@Serializable
class CreatePostCommand (

    val userId: String,
    val message: String,


){
    fun validate(): CreatePostCommand {

        if (message.isEmpty())
        {
            throw IllegalArgumentException("Escriba una breve description.")
        }
        if(!validateMessage(message))
        {
            throw IllegalArgumentException("El mensaje debe tener menos de 250 caracteres.")
        }




        return this

    }
    fun validateMessage(message: String): Boolean {
       return message.length<250
    }



}

