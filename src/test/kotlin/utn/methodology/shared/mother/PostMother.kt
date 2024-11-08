package utn.methodology.shared.mother

import utn.methodology.domain.entities.Post
import java.time.LocalDateTime
import java.util.UUID
import io.github.serpro69.kfaker.Faker
import java.time.format.DateTimeFormatter
import java.time.ZoneOffset

class PostMother {

    companion object {
        val faker = Faker()

        fun random(userId: String): Post {
            return Post.fromPrimitives(
                mapOf(
                    "uuid" to UUID.randomUUID().toString(),
                    "message" to faker.lorem.words(),
                    "userId" to userId,
                    "createdAt" to LocalDateTime.now().toEpochSecond(ZoneOffset.UTC),

                )
            )
        }
    }
}
