package ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogWindow
import data.repository.NotesDatabaseRepository
import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.stateholder.StateHolder
import moe.tlaster.precompose.viewmodel.viewModel
import queries.notes.Notes
import ui.components.PointerModifier
import utils.OsDetector
import utils.stringResource
import utils.unixToDateString
import viewmodel.MainViewModel
import viewmodel.NotesViewModel
import java.awt.Toolkit
import java.io.File
import java.util.UUID


@Composable
fun Home( vm: NotesViewModel) {
    val mainViewModel: MainViewModel = koinViewModel()

    val notesList = vm.notes.collectAsState().value
    val isAdding = vm.isAdding.collectAsState().value

    if (isAdding) {
        AddNoteDialog(vm)
    }
    Column(
        Modifier.fillMaxSize(), Arrangement.Center, Alignment.CenterHorizontally
    ) {
        Text("${stringResource("hello")}${mainViewModel.getName()} !")

        Button(
            modifier = Modifier,
            onClick = { vm.showDialog() }
        ) {
            Text(stringResource("add_note"))
        }

        NotesList(notesList, vm)

    }
}


@Composable
fun NoteCard(note: Notes, vm: NotesViewModel) {
    var showMenu by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }

    // Affichage du dialogue d'édition si nécessaire
    if (showEditDialog) {
        EditNoteDialog(note = note, vm = vm, onDismiss = { showEditDialog = false })
    }

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = note.title, style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = note.content, style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = unixToDateString(note.creationTime), style = MaterialTheme.typography.bodySmall)
                }
                IconButton(onClick = { showMenu = true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Menu")
                }
            }
            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false }
            ) {
                DropdownMenuItem(
                    onClick = {
                        showEditDialog = true
                        showMenu = false
                    }
                , text = {
                    Text("Éditer")})

                DropdownMenuItem(
                    onClick = {
                        vm.removeNote(note)
                        showMenu = false
                    }
                , text =  {
                        Text("Supprimer")
                })
            }
        }
    }
}
@Composable
fun NotesList(notes: List<Notes>, vm: NotesViewModel) {
    // Définir une configuration de grille pour adapter automatiquement le nombre de colonnes
    // en fonction de la largeur disponible et de la largeur minimale de chaque note
    val minNoteWidth = 300.dp // Vous pouvez ajuster cette valeur à vos besoins
    val gridConfig = GridCells.Adaptive(minSize = minNoteWidth)

    LazyVerticalGrid(
        columns = gridConfig,
        modifier = Modifier.fillMaxWidth()
    ) {
        items(notes) { note ->
            NoteCard(note, vm)
        }
    }
}


@Composable
fun AddNoteDialog(vm: NotesViewModel) {
    val title = remember { mutableStateOf("") }
    val content = remember { mutableStateOf("") }

    Dialog(onDismissRequest = { vm.hideDialog() }) {
        Card(modifier = Modifier.padding(16.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = stringResource("add_note"), style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    value = title.value,
                    onValueChange = { title.value = it },
                    label = { Text(stringResource("title")) }
                )
                Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    value = content.value,
                    onValueChange = { content.value = it },
                    label = { Text(stringResource("content")) }
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        val newNote = Notes(
                            noteId = 0, // Générer automatiquement si vous utilisez AUTOINCREMENT.
                            title = title.value,
                            content = content.value,
                            creationTime = System.currentTimeMillis() / 1000
                        )
                        vm.addNote(newNote)
                        vm.hideDialog()
                    }
                ) {
                    Text(text = stringResource("add"))
                }
            }
        }
    }
}

@Composable
fun EditNoteDialog(note: Notes, vm: NotesViewModel, onDismiss: () -> Unit) {
    var title by remember { mutableStateOf(note.title) }
    var content by remember { mutableStateOf(note.content) }
    Dialog(onDismissRequest = { onDismiss() }) {
        Card(modifier = Modifier.padding(16.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Éditer la note", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Titre") }
                )
                Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("Contenu") }
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        vm.updateNote(Notes(noteId = note.noteId, title = title, content = content, creationTime = note.creationTime))
                        onDismiss()
                    }
                ) {
                    Text("Mettre à jour")
                }
            }
        }
    }
}

