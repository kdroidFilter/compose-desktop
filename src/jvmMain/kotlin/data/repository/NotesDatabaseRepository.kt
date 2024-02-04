package data.repository

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import notes.Database
import queries.notes.Notes
import utils.getApplicationStoragePath
import utils.getRessourcePath

object NotesDatabaseRepository {
    // ReadOnly database val path = getRessourcePath(true) + "/database.db"
    val path = getApplicationStoragePath("compose-desktop") + "/database.db"
    val driver: SqlDriver = JdbcSqliteDriver("jdbc:sqlite:$path")

    init {
        Database.Schema.create(driver)
    }

    private val database = Database(driver)

    fun getAllNotes(): List<Notes> {
        return database.notesQueries.selectAllNotes().executeAsList()
    }

    fun addNote(note: Notes) {
        database.notesQueries.insertNote(
            title = note.title,
            content = note.content,
            creationTime = note.creationTime
        )
    }

    fun updateNote(note: Notes) {
        val currentTime = System.currentTimeMillis() / 1000
        database.notesQueries.updateNote(
            title = note.title,
            content = note.content,
            creationTime = currentTime,
            noteId = note.noteId
        )
    }


    fun removeNote(note: Notes) {
        database.notesQueries.deleteNote(
            noteId = note.noteId
        )
    }
}