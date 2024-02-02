package data.repository

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import notes.Database
import utils.getRessourcePath

object NotesDatabaseRepository {
    val path = getRessourcePath(true) + "/database.db"
    val driver: SqlDriver = JdbcSqliteDriver("jdbc:sqlite:$path")
    init {
        Database.Schema.create(driver)
    }
    val database = Database(driver)
}