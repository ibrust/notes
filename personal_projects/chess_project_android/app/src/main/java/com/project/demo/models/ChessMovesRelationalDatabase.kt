package com.project.demo.models

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

interface ChessMovesRelationalDatabase {
    fun chessMovesDao(): ChessMovesDao
}

@Database(entities = arrayOf(ChessMoveEntity::class), version = 1, exportSchema = false)
public abstract class ChessMovesRelationalDatabaseImpl : RoomDatabase(),
    ChessMovesRelationalDatabase {

    override abstract fun chessMovesDao(): ChessMovesDao

    companion object {
        @Volatile
        var dbInstance: ChessMovesRelationalDatabaseImpl? = null

        fun getDatabase(context: Context, scope: CoroutineScope): ChessMovesRelationalDatabaseImpl {

            return dbInstance ?: synchronized(this) {
                val instance = Room.databaseBuilder(        // if singleton instance is null initialize it
                    context.applicationContext,
                    ChessMovesRelationalDatabaseImpl::class.java,
                    "room_database"
                ).fallbackToDestructiveMigration()
                .addCallback(PopulateDatabaseCallback(scope))
                .build()

                dbInstance = instance
                instance
            }
        }
    }

    private class PopulateDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        // called when DB is first built, but after the DB tables are setup
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            dbInstance?.let { database ->
                scope.launch {
                    val chessMovesDao = database.chessMovesDao()
                    chessMovesDao.deleteAllChessMoves()
                    chessMovesDao.insert(ChessMoveEntity(positionIndex = 12))
                }
            }
        }
    }
}
