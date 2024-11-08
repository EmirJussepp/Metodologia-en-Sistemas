package utn.methodology

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.*




class ApplicationTest {
    @Test
    fun testRoot() = testApplication {
        application {
            testModule() // Llama a testModule que ahora invoca module()
        }
        client.get("/").apply {
            println("Response: ${bodyAsText()}") // Esto te mostrará la respuesta
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("Hello World!", bodyAsText())
        }

    }
}
