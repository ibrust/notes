package com.project.demo

import android.content.Context
import com.project.demo.models.DogFactRelationalDatabaseImpl
import com.project.demo.models.DogFactRepository
import com.project.demo.models.DogFactRepositoryImpl
import kotlinx.coroutines.CoroutineScope

class DogFactAppContainer(context: Context, scope: CoroutineScope) {
    val joggingAppRepository: DogFactRepository

    init {
        joggingAppRepository = DogFactRepositoryImpl(
            DogFactRelationalDatabaseImpl.getDatabase(context = context, scope = scope)
        )
    }
}