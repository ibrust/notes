package com.project.demo.models

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

interface DogFactAppRelationalDatabase {
    fun dogFactDao(): DogFactDao
}

@Database(entities = arrayOf(DogFactEntity::class), version = 1, exportSchema = false)
public abstract class DogFactRelationalDatabaseImpl : RoomDatabase(),
    DogFactAppRelationalDatabase {

    override abstract fun dogFactDao(): DogFactDao

    companion object {
        @Volatile
        var dbInstance: DogFactRelationalDatabaseImpl? = null

        fun getDatabase(context: Context, scope: CoroutineScope): DogFactRelationalDatabaseImpl {

            return dbInstance ?: synchronized(this) {
                val instance = Room.databaseBuilder(        // if singleton instance is null initialize it
                    context.applicationContext,
                    DogFactRelationalDatabaseImpl::class.java,
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
                    val dogFactDao = database.dogFactDao()
                    dogFactDao.deleteAllDogFacts()
                    dogFactDao.insert(DogFactEntity(dogFact = "dogs are hungry"))
                }
            }
        }
    }
}
