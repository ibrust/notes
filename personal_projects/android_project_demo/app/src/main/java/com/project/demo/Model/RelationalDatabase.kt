package com.project.demo.Model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(JoggingSessionEntity::class), version = 1, exportSchema = false)
public abstract class RelationalDatabase : RoomDatabase() {

    abstract fun joggingSessionDao(): JoggingSessionDao

    companion object {
        @Volatile
        private var dbInstance: RelationalDatabase? = null

        fun getDatabase(context: Context): RelationalDatabase {
            return dbInstance ?: synchronized(this) {
                val instance = Room.databaseBuilder(        // if singleton instance is null initialize it
                    context.applicationContext,
                    RelationalDatabase::class.java,
                    "room_database"
                ).build()

                dbInstance = instance
                instance
            }
        }
    }
}