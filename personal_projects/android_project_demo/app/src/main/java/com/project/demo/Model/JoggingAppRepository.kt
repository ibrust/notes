package com.project.demo.Model

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow


class JoggingAppRepository(private val db: JoggingAppRelationalDatabase) {

    private val joggingSessionDao: JoggingSessionDao
        get() = db.joggingSessionDao()

    val allJoggingSessions: Flow<List<JoggingSessionEntity>> = joggingSessionDao.getJoggingSessions()

    @Suppress("RedundantSuspendModifier")   // room already runs queries on a background thread
    @WorkerThread
    suspend fun insert(entity: JoggingSessionEntity) {
        joggingSessionDao.insert(entity)
    }
}