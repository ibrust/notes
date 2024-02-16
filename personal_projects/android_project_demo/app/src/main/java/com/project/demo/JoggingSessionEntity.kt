package com.project.demo

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query

@Entity(tableName = "jogging_sessions_table")
class JoggingSessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "step_count") val stepCount: Int
)

@Dao
interface JoggingSessionDao {

    @Query("SELECT * FROM jogging_sessions_table ORDER BY id DESC")
    fun getJoggingSessions(): List<JoggingSessionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(joggingSessionEntity: JoggingSessionEntity)

    @Query("DELETE FROM jogging_sessions_table")
    suspend fun deleteAll()
}