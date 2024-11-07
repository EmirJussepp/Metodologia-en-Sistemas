package utn.methodology.application.commands



import kotlinx.serialization.Serializable

@Serializable
class ActualizarUsuarioCommand(
    val uuid: String,
    val username: String? = null,  // Cambiado a nullable
    val surname: String? = null,   // Cambiado a nullable
    val email: String? = null,     // Cambiado a nullable
    val password: String? = null   // Cambiado a nullable
) {
    fun validate() {
        // Verifica que el UUID no esté vacío
        if (uuid.isEmpty()) {
            throw IllegalArgumentException("El ID del usuario no puede estar vacío.")
        }

        // Verifica que al menos uno de los campos a actualizar no sea nulo
        if (username == null && surname == null && email == null && password == null) {
            throw IllegalArgumentException("Debes proporcionar al menos un campo para actualizar.")
        }

        // Validaciones de email y password solo si se proporciona el campo
        if (email != null && !isValidEmail(email)) {
            throw IllegalArgumentException("El email proporcionado no es válido.")
        }
        if (password != null && !isValidPassword(password)) {
            throw IllegalArgumentException("La contraseña debe tener al menos 7 caracteres y contener una combinación de letras y números.")
        }
    }
}

// Función de validación de email
fun isValidEmail(email: String): Boolean {
    return email.isNotEmpty() && email.contains("@")
}

// Función de validación de contraseña
fun isValidPassword(password: String): Boolean {
    return password.length >= 7 && password.any { it.isDigit() } && password.any { it.isLetter() }
}
