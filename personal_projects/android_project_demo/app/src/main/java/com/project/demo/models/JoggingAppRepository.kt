package com.project.demo.models

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

interface JoggingAppRepository {
    val allJoggingSessions: Flow<List<JoggingSessionEntity>>
}

class JoggingAppRepositoryImpl(private val db: JoggingAppRelationalDatabase) :
    JoggingAppRepository {

    private val joggingSessionDao: JoggingSessionDao
        get() = db.joggingSessionDao()

    override val allJoggingSessions: Flow<List<JoggingSessionEntity>> = joggingSessionDao.getJoggingSessions()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteAllJoggingSessions() {
        joggingSessionDao.deleteAllJoggingSessions()
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(entity: JoggingSessionEntity) {
        joggingSessionDao.insert(entity)
    }
}