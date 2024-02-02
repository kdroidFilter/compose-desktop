package viewmodel

import data.repository.NotesDatabaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import moe.tlaster.precompose.viewmodel.ViewModel
import queries.notes.Notes

class NotesViewModel(val repository : NotesDatabaseRepository): ViewModel() {
    private val _notes = MutableStateFlow(repository.getAllNotes())
    val notes = _notes.asStateFlow()

    private val _isAdding = MutableStateFlow(false)
    val isAdding = _isAdding.asStateFlow()

    fun hideDialog() {
        _isAdding.value = false
    }

    fun showDialog() {
        _isAdding.value = true
    }

    fun addNote(note: Notes) {
        repository.addNote(note)
        _notes.value = repository.getAllNotes()
        hideDialog() // Cachez le dialogue après l'ajout de la note.
    }

    fun removeNote(note: Notes) {
        repository.removeNote(note)
        _notes.value = repository.getAllNotes()
    }

    fun updateNote(note: Notes) {
        repository.updateNote(note)
        _notes.value = repository.getAllNotes()
    }
}