package utils

object RegexVerificator {
    fun isEmailValid(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9](.*)([@]{1})(.{1,})(\\.)(.{1,})$"
        return email.matches(emailRegex.toRegex())
    }

}