package com.project.demo

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class DogFactApplication : Application() {
    lateinit var container: DogFactAppContainer

    override fun onCreate() {
        super.onCreate()
        val applicationScope = CoroutineScope(SupervisorJob())
        container = DogFactAppContainer(context = this, scope = applicationScope)
    }
}