package com.example.pong_group.Controller

import android.app.Application

class App : Application() {

    //special class for getting context from everywhere in app by introduce instance of app
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: App
            private set
    }
}