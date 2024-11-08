package utn.methodology.shared.mother

import utn.methodology.domain.entities.User
import java.util.UUID
import io.github.serpro69.kfaker.Faker

class UserMother {

    companion object {
        val faker = Faker()

        fun random(): User {
            return User.fromPrimitives(
                mapOf(
                    "uuid" to UUID.randomUUID().toString(),
                    "username" to faker.southPark.characters(),
                    "email" to faker.internet.email(),

                )
            )
        }

    }
}
