package utn.methodology.infrastructure.http.actions

import utn.methodology.application.commandhandlers.CreateUserCommandHandler
import utn.methodology.application.commands.CreateUserCommand

class ConfirmUserAction(
  private val handler: CreateUserCommandHandler
){
  fun execute(body: CreateUserCommand) {
    body.validate().let {
      handler.handle(it)
    }

  }
}