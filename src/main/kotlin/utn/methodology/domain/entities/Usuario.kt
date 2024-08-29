package utn.methodology.domain.entities



data class User(
    val uuid: Int = 0,
    val name: String,
    val surname: String,
    val email: String,
    val password: String,
)
