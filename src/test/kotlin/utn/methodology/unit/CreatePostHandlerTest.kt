package utn.methodology.unit


import junit.framework.TestCase.assertEquals
import utn.methodology.application.commandhandlers.CreatePostCommandHandler
import utn.methodology.application.commands.CreatePostCommand
import utn.methodology.shared.mocks.MockPostRepository
import utn.methodology.shared.mocks.MockUserRepository
import utn.methodology.shared.mother.UserMother
import utn.methodology.shared.mother.PostMother


import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNotNull

class CreatePostHandlerTest {

    private val mockUserRepository: MockUserRepository = MockUserRepository()
    private val mockPostRepository: MockPostRepository = MockPostRepository()

    private var sut: CreatePostCommandHandler = CreatePostCommandHandler(mockPostRepository,mockUserRepository)


    @BeforeTest
    fun beforeEach() {
        mockPostRepository.clean()
        mockUserRepository.clean()
    }

    @Test
    fun `should create a post and persist into database`() {
        // arrange
        val user = UserMother.random()
        val content = PostMother.faker.southPark.quotes()

        mockUserRepository.save(user)

        val command = CreatePostCommand(

            user.getUserId(),
            content
        )

        // act
        sut.handle(command)


        val posts = mockPostRepository.findPostsByUserId(user.getUserId())
        assert(posts.size == 1)
        assert(posts[0].getMensaje() == content)

    }
}