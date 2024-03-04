package com.project.demo.models

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.TypeConverter
import kotlinx.coroutines.flow.Flow
import kotlin.collections.List


@Entity(tableName = "all_movesets_table")
class ChessMoveSetEntity(
    @PrimaryKey(autoGenerate = true) val movesetId: Int = 0,
    val tableName: String,
    val moveWhiteCastledOn: Int? = null,
    val moveBlackCastledOn: Int? = null
)

@Entity(tableName = "moveset_table", foreignKeys = [
    ForeignKey(
        entity = ChessMoveSetEntity::class,
        parentColumns = ["movesetId"],
        childColumns = ["movesetId"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )
])
class ChessPositionEntity(
    @PrimaryKey(autoGenerate = true) val positionId: Int = 0,
    val movesetId: Int,
    val fullMoveNumber: Int,
    val colorsMove: ChessColor,
    val enPassantTargetSquare: Square?,
    val piecePlacementData: String,
    val moveInfo: String? = null
)


@Dao
interface ChessMoveSetsDao {

    @Query("SELECT * FROM all_movesets_table ORDER BY movesetId DESC")
    fun getAvailableChessMoveSets(): Flow<List<ChessMoveSetEntity>>

    @Query("SELECT * FROM moveset_table WHERE movesetId = :movesetId ORDER BY positionId DESC")
    fun getChessMoveSet(movesetId: Int): Flow<List<ChessPositionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(chessPosition: ChessPositionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(chessMoveSet: ChessMoveSetEntity)

    @Query("DELETE FROM all_movesets_table WHERE movesetId = :movesetId")
    suspend fun deleteChessMoveset(movesetId: Int)
}