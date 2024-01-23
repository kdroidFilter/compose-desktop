package data.repository

object TextRepository {
    val readmeUrl = "https://raw.githubusercontent.com/kdroidFilter/Compose-Multiplatform-Template/master/README.md"
    val licenseUrl = "https://raw.githubusercontent.com/kdroidFilter/Compose-Multiplatform-Template/master/LICENSE"

    val getLicense = TextRepository::class.java.classLoader.getResourceAsStream("LICENSE.txt")
        ?.readBytes()
        ?.decodeToString()
}