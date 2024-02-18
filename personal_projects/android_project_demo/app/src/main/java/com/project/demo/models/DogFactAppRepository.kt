package com.project.demo.models

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

interface DogFactAppRepository {
    val allDogFacts: Flow<List<DogFactEntity>>
}

class DogFactAppRepositoryImpl(private val db: DogFactAppRelationalDatabase) :
    DogFactAppRepository {

    private val dogFactDao: DogFactDao
        get() = db.dogFactDao()

    override val allDogFacts: Flow<List<DogFactEntity>> = dogFactDao.getAllDogFacts()

    @WorkerThread
    suspend fun deleteAllDogFacts() {
        dogFactDao.deleteAllDogFacts()
    }

    @WorkerThread
    suspend fun insert(entity: DogFactEntity) {
        dogFactDao.insert(entity)
    }
}