package utn.methodology.infrastructure.http.actions


import utn.methodology.application.commandhandlers.CreatePostCommandHandler
import utn.methodology.application.commands.CreatePostCommand

class ConfirmPostAction(
    private val handler: CreatePostCommandHandler
){
    fun execute(body: CreatePostCommand) {
        body.validate().let {
            handler.handle(it)
        }

    }
}