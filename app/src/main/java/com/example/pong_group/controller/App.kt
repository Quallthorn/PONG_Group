package com.example.pong_group.controller

import android.app.Application
import com.example.pong_group.services.Prefs
import io.realm.Realm
import io.realm.RealmConfiguration

//shared preferences for global access from everywhere
val prefs: Prefs by lazy {
    App.prefs!!
}

class App : Application() {
    //special class for getting context from everywhere in app by introduce instance of app and init Realm preferences on start of app
    override fun onCreate() {
        super.onCreate()

        //instance -  global variable for getting context
        instance = this
        //init global preferences
        prefs = Prefs(applicationContext)

        //Init Realm database
        Realm.init(instance)
        val realmName = "PongScores"
        val realmConfig = RealmConfiguration.Builder().name(realmName).allowWritesOnUiThread(true).build()
        Realm.setDefaultConfiguration(realmConfig)
    }

    companion object {

        //singleton preferences
        lateinit var instance: App
            private set
        var prefs: Prefs? = null
    }
}