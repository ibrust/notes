package com.project.demo

import android.content.Context
import com.project.demo.models.DogFactAppRelationalDatabaseImpl
import com.project.demo.models.DogFactAppRepository
import com.project.demo.models.DogFactAppRepositoryImpl

class DogFactAppContainer(context: Context) {
    val joggingAppRepository: DogFactAppRepository

    init {
        joggingAppRepository = DogFactAppRepositoryImpl(
            DogFactAppRelationalDatabaseImpl.getDatabase(context = context)
        )
    }
}