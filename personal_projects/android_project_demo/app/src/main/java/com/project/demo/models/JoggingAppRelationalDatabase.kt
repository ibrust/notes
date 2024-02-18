package com.project.demo.models

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

interface JoggingAppRelationalDatabase {
    fun joggingSessionDao(): JoggingSessionDao
}

@Database(entities = arrayOf(JoggingSessionEntity::class), version = 1, exportSchema = false)
public abstract class JoggingAppRelationalDatabaseImpl : RoomDatabase(),
    JoggingAppRelationalDatabase {

    override abstract fun joggingSessionDao(): JoggingSessionDao

    companion object {
        @Volatile
        var dbInstance: JoggingAppRelationalDatabaseImpl? = null

        fun getDatabase(context: Context): JoggingAppRelationalDatabaseImpl {
            return dbInstance ?: synchronized(this) {
                val instance = Room.databaseBuilder(        // if singleton instance is null initialize it
                    context.applicationContext,
                    JoggingAppRelationalDatabaseImpl::class.java,
                    "room_database"
                ).fallbackToDestructiveMigration().build()

                dbInstance = instance
                instance
            }
        }
    }
}