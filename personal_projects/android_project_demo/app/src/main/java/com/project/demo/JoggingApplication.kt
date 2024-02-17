package com.project.demo

import android.app.Application
import com.project.demo.models.JoggingAppRelationalDatabaseImpl
import com.project.demo.models.JoggingAppRepository
import com.project.demo.models.JoggingAppRepositoryImpl

class JoggingApplication : Application() {
    lateinit var joggingAppRepository: JoggingAppRepository

    override fun onCreate() {
        super.onCreate()
        joggingAppRepository = JoggingAppRepositoryImpl(
            JoggingAppRelationalDatabaseImpl.getDatabase(context = this)
        )
    }
}