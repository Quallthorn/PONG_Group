package com.example.pong_group.Controller

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

class App : Application() {

    //special class for getting context from everywhere in app by introduce instance of app
    override fun onCreate() {
        super.onCreate()
        instance = this

        Realm.init(instance)
        val realmName: String = "PongScores"
        var realmConfig = RealmConfiguration.Builder().name(realmName).build()
        Realm.setDefaultConfiguration(realmConfig)
    }

    companion object {
        lateinit var instance: App
            private set
    }
}