package com.project.demo.models

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

interface DogFactAppRelationalDatabase {
    fun dogFactDao(): DogFactDao
}

@Database(entities = arrayOf(DogFactEntity::class), version = 1, exportSchema = false)
public abstract class DogFactAppRelationalDatabaseImpl : RoomDatabase(),
    DogFactAppRelationalDatabase {

    override abstract fun dogFactDao(): DogFactDao

    companion object {
        @Volatile
        var dbInstance: DogFactAppRelationalDatabaseImpl? = null

        fun getDatabase(context: Context): DogFactAppRelationalDatabaseImpl {
            return dbInstance ?: synchronized(this) {
                val instance = Room.databaseBuilder(        // if singleton instance is null initialize it
                    context.applicationContext,
                    DogFactAppRelationalDatabaseImpl::class.java,
                    "room_database"
                ).fallbackToDestructiveMigration().addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        val dogFactDb = db as? DogFactAppRelationalDatabase ?: return
                        CoroutineScope(Dispatchers.IO).launch {
                            dogFactDb.dogFactDao().insert(DogFactEntity(dogFact = "dogs are hungry"))
                        }
                    }
                }).build()

                dbInstance = instance
                instance
            }
        }
    }
}