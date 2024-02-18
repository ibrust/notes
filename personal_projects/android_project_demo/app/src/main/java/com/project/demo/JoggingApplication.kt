package com.project.demo

import android.app.Application

class JoggingApplication : Application() {
    lateinit var container: JoggingAppContainer

    override fun onCreate() {
        super.onCreate()
        container = JoggingAppContainer(context = this)
    }
}