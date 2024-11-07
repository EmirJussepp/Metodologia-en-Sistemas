package utn.methodology.application.commands

import kotlinx.serialization.Serializable

@Serializable
class CreateUserCommand(
  val username: String,
  val surname: String,
  val email: String,
  val password: String,
) {

  fun validate(): CreateUserCommand {
    if (username.isEmpty()) {
      throw IllegalArgumentException("El nombre de usuario no debe ser vacio.")
    }
    if(surname.isEmpty()){
      throw IllegalArgumentException("El apellido no debe ser vacio.")
    }
    if (!isValidEmail(email)) {
      throw IllegalArgumentException("El email proporcionado no es válido.")
    }
    if (!isValidPassword(password)) {
      throw IllegalArgumentException("La contraseña debe tener al menos 7 caracteres y contener una combinación de letras y números.")
    }
    return this
  }

   fun isValidEmail(email: String): Boolean {
    return email.isNotEmpty() && email.contains("@")
  }

  fun isValidPassword(password: String): Boolean {
    return password.length >= 7 && password.any { it.isDigit() } && password.any { it.isLetter() }
  }
}
