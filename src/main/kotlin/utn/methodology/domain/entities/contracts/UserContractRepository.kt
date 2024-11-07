package utn.methodology.domain.contracts


import utn.methodology.domain.entities.User

interface IUserRepository {
  fun save(user: User)
  fun findByField(field: String, value: String): List<User>
  fun existsById(userId: String): Boolean
  fun actualizarUsuario(user: User): Boolean


  fun agregarALista(userId: String, campo: String, valor: String): Boolean

  fun obtenerListaSeguidores(userId: String): List<String>
}


