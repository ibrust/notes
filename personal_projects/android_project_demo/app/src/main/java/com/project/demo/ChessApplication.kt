package com.project.demo

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class ChessApplication : Application() {
    lateinit var container: ChessAppContainer

    override fun onCreate() {
        super.onCreate()
        val applicationScope = CoroutineScope(SupervisorJob())
        container = ChessAppContainer(context = this, scope = applicationScope)
    }
}