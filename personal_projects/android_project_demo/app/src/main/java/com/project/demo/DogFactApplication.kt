package com.project.demo

import android.app.Application

class DogFactApplication : Application() {
    lateinit var container: DogFactAppContainer

    override fun onCreate() {
        super.onCreate()
        container = DogFactAppContainer(context = this)
    }
}