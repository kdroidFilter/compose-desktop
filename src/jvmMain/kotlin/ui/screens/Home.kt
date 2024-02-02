package ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import data.repository.NotesDatabaseRepository
import moe.tlaster.precompose.navigation.Navigator
import queries.notes.Notes
import utils.stringResource
import utils.unixToDateString
import viewmodel.MainViewModel


@Composable
fun Home(vm: MainViewModel, navigator: Navigator) {
    Column(
        Modifier.fillMaxSize(), Arrangement.Center, Alignment.CenterHorizontally
    ) {
        Text("${stringResource("hello")}${vm.getName()} !")
        val databaseRepo = NotesDatabaseRepository
        val notesList = databaseRepo.database.notesQueries.selectAllNotes().executeAsList()

        NotesList(notesList)
    }
}


@Composable
fun NoteCard(note: Notes) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = note.title, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = note.content, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = unixToDateString( note.creationTime), style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun NotesList(notes: List<Notes>) {
    LazyRow {
        items(notes) { note ->
            NoteCard(note)
        }
    }
}

