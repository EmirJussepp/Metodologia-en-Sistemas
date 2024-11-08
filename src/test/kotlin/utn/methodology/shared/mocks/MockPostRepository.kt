package utn.methodology.shared.mocks

import utn.methodology.domain.contracts.IPostRepository
import utn.methodology.domain.entities.Post

class MockPostRepository : IPostRepository {

    // Lista mutable para almacenar los posts
    private var posts: MutableList<Post> = mutableListOf()

    // Guarda un post en el repositorio
    override fun save(post: Post) {
        // Elimina el post si ya existe, basándose en su UUID
        posts.removeIf { it.uuid == post.uuid }
        // Agrega el nuevo post
        posts.add(post)
    }

    // Encuentra todos los posts de un usuario específico por userId
    override fun findPostsByUserId(userId: String): List<Post> {
        return this.posts.filter { it.getOwnerId() == userId } // Filtra los posts por userId
    }
    // Busca un post por su UUID
    override fun findById(id: String): Post? {
        return posts.find { it.uuid == id } // Devuelve el post que coincide con el UUID, o null si no existe
    }

    // Elimina un post por su UUID
    override fun delete(id: String) {
        posts.removeIf { it.uuid == id } // Elimina el post por id
    }

    // Encuentra todos los posts de una lista de usuarios
    override fun findPostsByUsers(userId: List<String>): List<Post> {
        return posts.filter { it.userId in userId } // Filtra los posts de los usuarios dados
    }


    fun clean() {
        posts.clear() // Limpia la lista de posts para que no haya interferencias entre pruebas
    }
}
