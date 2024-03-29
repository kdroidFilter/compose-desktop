package ui.screens.contact

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.ContactMail
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Subject
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.OutlinedRichTextEditor
import moe.tlaster.precompose.koin.koinViewModel
import ui.components.InfoContainer
import ui.components.PointerModifier
import utils.stringResource
import utils.texteditor.components.RichTextStyleRow
import viewmodel.MailViewModel
import viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactHome() {
    val mainViewModel: MainViewModel = koinViewModel()
    val mailViewModel: MailViewModel = koinViewModel()

    val mailModel = mailViewModel.mailModel.collectAsState()
    val message = rememberRichTextState()
    val fieldStatus = mailViewModel.formStatus.collectAsState().value

    Box(contentAlignment = Alignment.TopCenter, modifier = Modifier.fillMaxSize()) {
        Column(
            Modifier.padding(start = 32.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            InfoContainer(stringResource("contact_info_message"))

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                TextField(
                    value = mailModel.value.name,
                    onValueChange = { mailViewModel.updateMailModel("name", it) },
                    placeholder = { Text(stringResource("enter_name")) },
                    modifier = Modifier.weight(1f),
                    label = { Text(stringResource("name")) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.ContactMail,
                            contentDescription = null
                        )
                    },
                    enabled = fieldStatus,
                    isError = mailViewModel.isNameError.collectAsState().value,
                    maxLines = 1
                )
                TextField(
                    value = mailModel.value.email,
                    onValueChange = { mailViewModel.updateMailModel("email", it) },
                    placeholder = { Text(stringResource("enter_email")) },
                    modifier = Modifier.weight(1f),
                    label = { Text(stringResource("email")) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.AlternateEmail,
                            contentDescription = null
                        )
                    },
                    enabled = fieldStatus,
                    isError = mailViewModel.isEmailError.collectAsState().value,
                    maxLines = 1
                )
            }

            TextField(
                value = mailModel.value.subject,
                onValueChange = { mailViewModel.updateMailModel("subject", it) },
                placeholder = { Text(stringResource("enter_subject")) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource("subject")) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Subject,
                        contentDescription = null
                    )
                },
                enabled = fieldStatus,
                isError = mailViewModel.isSubjectError.collectAsState().value,
                maxLines = 1
            )

            RichTextStyleRow(
                vm = mainViewModel,
                modifier = Modifier.fillMaxWidth(),
                state = message
            )


            Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                OutlinedRichTextEditor(
                    modifier = Modifier.fillMaxSize(),
                    state = message,
                    placeholder = { Text(stringResource("enter_message")) },
                    label = { Text(stringResource("message")) },
                    enabled = fieldStatus,
                )
            }


            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(enabled = fieldStatus,
                    onClick = {
                        mailViewModel.updateMailModel("message", message.toHtml())
                        mailViewModel.sendMail()
                    }) {
                    Row(PointerModifier, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(stringResource("send"))
                        Icon(imageVector = Icons.Default.Send, contentDescription = null,
                            modifier = Modifier.graphicsLayer {
                                rotationZ =
                                    if (mainViewModel.isCurrentLanguageRtl()) 180f else 0f
                            }
                        )
                    }
                }
            }
        }
    }
}

