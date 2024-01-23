package data.api

import data.model.AppVersion

interface VersionApi {
    suspend fun getCurrentVersion(): AppVersion
}
