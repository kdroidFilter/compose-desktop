package utils

import data.model.EmailModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.gson.gson
import javax.net.ssl.X509TrustManager

class EmailSender(val email: EmailModel) {
    suspend fun send(): Result<String> {
        val client = HttpClient(CIO) {
            engine { https { trustManager = TrustAllCertsHttpClient.trustAllCerts[0] as X509TrustManager } }
            install(ContentNegotiation) {
                gson()
            }
        }

        return try {
            val emailData = mapOf(
                "subject" to email.subject,
                "body" to email.message,
                "from_email" to email.email,
                "from_name" to email.name

            )

            val response: HttpResponse = client.request("https://gefen.cloud/wp-json/myapp/v1/send-email/") {
                method = HttpMethod.Post
                contentType(ContentType.Application.Json)
                setBody(emailData)
            }

            println(response.bodyAsText())

            if (response.status == HttpStatusCode.OK) {
                Result.success("Mail sent successfully")
            } else {
            Result.failure(Exception("${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        } finally {
            client.close()
        }
    }
}
