package com.project.demo

import android.content.Context
import com.project.demo.models.JoggingAppRelationalDatabaseImpl
import com.project.demo.models.JoggingAppRepository
import com.project.demo.models.JoggingAppRepositoryImpl

class JoggingAppContainer(context: Context) {
    val joggingAppRepository: JoggingAppRepository

    init {
        joggingAppRepository = JoggingAppRepositoryImpl(
            JoggingAppRelationalDatabaseImpl.getDatabase(context = context)
        )
    }
}