package utn.methodology.shared.mocks

import utn.methodology.domain.contracts.IUserRepository
import utn.methodology.domain.entities.User

class MockUserRepository : IUserRepository {

    // Lista mutable para almacenar usuarios
    private var users: MutableList<User> = mutableListOf()

    // Guarda un usuario en el repositorio
    override fun save(user: User) {
        // Elimina cualquier usuario existente con el mismo UUID antes de guardar el nuevo
        users.removeIf { it.getUserId() == user.getUserId() }
        users.add(user) // Agrega el nuevo usuario
    }

    // Verifica si un usuario existe por su ID
    override fun existsById(userId: String): Boolean {
        return users.any { it.getUserId() == userId } // Retorna true si existe, false en caso contrario
    }

    // Encuentra usuarios por un campo específico (ID, nombre de usuario o correo electrónico)
    override fun findByField(field: String, value: String): List<User> {
        return when (field) {
            "_id" -> users.filter { it.getUserId() == value } // Asegúrate de que getUserId no sea nulo
            "username" -> users.filter { it.username == value }
            "email" -> users.filter { it.email == value }
            else -> emptyList() // Devuelve lista vacía si el campo no es válido
        }
    }


    // Agrega un valor a una lista de seguidos o seguidores
    override fun agregarALista(userId: String, campo: String, valor: String): Boolean {
        val user = users.find { it.getUserId() == userId } ?: return false // Encuentra el usuario
        when (campo) {
            "seguidos" -> if (!user.seguidos.contains(valor)) { // Verifica si ya está seguido
                user.seguidos = user.seguidos.plus(valor) // Agrega a la lista de seguidos
                return true
            }
            "seguidores" -> if (!user.seguidores.contains(valor)) { // Verifica si ya es seguidor
                user.seguidores = user.seguidores.plus(valor) // Agrega a la lista de seguidores
                return true
            }
        }
        return false // Retorna false si no se pudo agregar
    }

    // Actualiza un usuario existente
    override fun actualizarUsuario(user: User): Boolean {
        val existingUserIndex = users.indexOfFirst { it.getUserId() == user.getUserId() } // Encuentra el índice del usuario
        return if (existingUserIndex != -1) {
            users[existingUserIndex] = user // Actualiza el usuario
            true
        } else {
            false // Retorna false si el usuario no existe
        }
    }

    // Obtiene la lista de seguidores de un usuario
    override fun obtenerListaSeguidores(userId: String): List<String> {
        return users.find { it.getUserId() == userId }?.seguidores ?: emptyList() // Retorna la lista de seguidores
    }

    // Limpia la lista de usuarios
    fun clean() {
        users.clear() // Limpia la lista para preparar para pruebas
    }


}
