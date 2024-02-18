package com.project.demo.models

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import kotlin.collections.List

@Entity(tableName = "dog_facts_table")
class DogFactEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "dog_fact") val dogFact: String
)

@Dao
interface DogFactDao {

    @Query("SELECT * FROM dog_facts_table ORDER BY id DESC")
    fun getAllDogFacts(): Flow<List<DogFactEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(dogFactEntity: DogFactEntity)

    @Query("DELETE FROM dog_facts_table")
    suspend fun deleteAllDogFacts()
}