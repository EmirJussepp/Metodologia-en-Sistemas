package utn.methodology.application.commands

data class CreateUser(val username: String, val email: String, val password: String,
                      val errors: MutableList<String> = mutableListOf()) {

    init {
        validate()
    }
    private fun validate() {
        if (username.length < 3) {
            errors.add("El nombre de usuario debe tener al menos 3 caracteres.")
        }
        if (!isValidEmail(email)) {
            errors.add("El email proporcionado no es válido.")
        }
        if (!isValidPassword(password)) {
            errors.add("La contraseña debe tener al menos 7 caracteres y contener una combinación de letras y números.")
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return email.isNotEmpty() && email.contains("@")
    }

    private fun isValidPassword(password: String): Boolean {
        return password.length >= 7 && password.any { it.isDigit() } && password.any { it.isLetter() }
    }
}