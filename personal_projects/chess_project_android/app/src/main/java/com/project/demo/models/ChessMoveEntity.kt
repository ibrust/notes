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

@Entity(tableName = "chess_moves_table")
class ChessMoveEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "position") val positionIndex: Int
)

@Dao
interface ChessMovesDao {

    @Query("SELECT * FROM chess_moves_table ORDER BY id DESC")
    fun getAllChessMoves(): Flow<List<ChessMoveEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(chessMove: ChessMoveEntity)

    @Query("DELETE FROM chess_moves_table")
    suspend fun deleteAllChessMoves()
}