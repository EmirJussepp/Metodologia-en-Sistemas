package utn.methodology.unit


import utn.methodology.application.commandhandlers.CreateUserCommandHandler
import utn.methodology.application.commands.CreateUserCommand
import utn.methodology.shared.mocks.MockUserRepository
import utn.methodology.shared.mother.UserMother

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNotNull


class CreateUserCommandHandlerTest {

     val mockUserRepository: MockUserRepository = MockUserRepository()

    var sut: CreateUserCommandHandler = CreateUserCommandHandler(mockUserRepository)

    @BeforeTest
    fun beforeEach() {
        mockUserRepository.clean()

    }

    @Test
    fun`should create a user and persist into database`(){


        // Arrange
        val username = UserMother.faker.southPark.characters()

        val command = CreateUserCommand(
            username,
            surname= UserMother.faker.southPark.characters(),
            password= UserMother.faker.southPark.characters(),
            email= UserMother.faker.internet.email()


        )
        sut.handle(command)
        // Act
        val user = mockUserRepository.findByField("username",username)
        assertNotNull(user)



    }
}
