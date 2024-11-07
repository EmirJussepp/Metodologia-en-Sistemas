package utn.methodology.domain.contracts
import utn.methodology.domain.entities.Post

interface IPostRepository {
    fun save(post: Post)
    fun findPostsByUserId(userId: String): List<Post>
    fun delete(id: String)
    fun findById(id: String): Post?
    fun findPostsByUsers(userId: List<String>): List<Post>


}
