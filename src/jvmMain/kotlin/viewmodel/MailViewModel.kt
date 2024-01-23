package viewmodel

import androidx.compose.runtime.mutableStateOf
import data.model.EmailModel
import enums.EmailStatus
import enums.NavigationDestination
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.PopUpTo
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import utils.EmailSender
import utils.RegexVerificator
import utils.pluralResource

class MailViewModel(val vm: MainViewModel, val navigator: Navigator) : ViewModel() {

    //MAIL SENDER
    private val _mailModel = mutableStateOf(EmailModel("", "", "", ""))
    val mailModel = _mailModel

    private val _wasClicked = mutableStateOf(false)
    val wasClicked = _wasClicked

    private val _sendingStatus = mutableStateOf(EmailStatus.WAITING)
    val sendingStatus = _sendingStatus

    private val _formStatus = mutableStateOf(true)
    val formStatus = _formStatus

    private val _isNameError = mutableStateOf(false)
    val isNameError = _isNameError

    private val _isEmailError = mutableStateOf(false)
    val isEmailError = _isEmailError

    private val _isSubjectError = mutableStateOf(false)
    val isSubjectError = _isSubjectError

    private var errorCount = 0


    fun updateMailModel(field: String, content: String) {
        when (field) {
            "name" -> {
                _mailModel.value = _mailModel.value.copy(name = content)
                _isNameError.value = content.isEmpty() && wasClicked.value
            }

            "email" -> {
                _mailModel.value = _mailModel.value.copy(email = content)
                _isEmailError.value = content.isEmpty() && wasClicked.value
            }

            "subject" -> {
                _mailModel.value = _mailModel.value.copy(subject = content)
                _isSubjectError.value = content.isEmpty() && wasClicked.value
            }

            "message" -> {
                _mailModel.value = _mailModel.value.copy(message = content)
            }
        }
    }


    fun sendMail() {
        _wasClicked.value = true
        sendingStatus.value = EmailStatus.SENDING
        val email = EmailModel(
            name = _mailModel.value.name,
            email = _mailModel.value.email,
            subject = _mailModel.value.subject,
            message = _mailModel.value.message,
        )
        if (mailModel.value.email.isBlank() || !RegexVerificator.isEmailValid(mailModel.value.email)) {
            isEmailError.value = true
            errorCount++
        }
        if (mailModel.value.name.isBlank()) {
            isNameError.value = true
            errorCount++
        }
        if (mailModel.value.subject.isBlank()) {
            isSubjectError.value = true
            errorCount++
        }

        if (isNameError.value || isEmailError.value || isSubjectError.value) {
            vm.showSnackbar(pluralResource("error_check_correct_fields", errorCount))
            println(errorCount)
            errorCount = 0
            println(errorCount)
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            vm.setLoading(true)
            _formStatus.value = false
            val result = EmailSender(email).send()
            result.onSuccess {
                sendingStatus.value = EmailStatus.SENT_SUCCESSFULLY
                _formStatus.value = false
                navigator.navigate(
                    NavigationDestination.ContactConfirmation.route,
                    NavOptions(popUpTo = PopUpTo(route = NavigationDestination.Home.route)),
                )
            }.onFailure { e ->
                println("Error: ${e.localizedMessage ?: "Error"}")
                sendingStatus.value = EmailStatus.ERROR
                _formStatus.value = true
                vm.showSnackbar("Error: ${e.localizedMessage ?: "Error"}")
            }
            vm.setLoading(false)
        }
    }
}