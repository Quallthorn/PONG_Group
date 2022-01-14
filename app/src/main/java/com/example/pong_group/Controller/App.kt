package com.example.pong_group.Controller

import android.app.Application
import android.content.res.Resources
import android.util.Log
import com.example.pong_group.Services.Prefs
import io.realm.Realm
import io.realm.RealmConfiguration

val prefs: Prefs by lazy {
    App.prefs!!
}

class App : Application() {



    //special class for getting context from everywhere in app by introduce instance of app
    override fun onCreate() {
        super.onCreate()
        instance = this
        prefs = Prefs(applicationContext)

        Realm.init(instance)
        val realmName = "PongScores"
        var realmConfig = RealmConfiguration.Builder().name(realmName).allowWritesOnUiThread(true).build()
        Realm.setDefaultConfiguration(realmConfig)
    }

    companion object {
        lateinit var instance: App
            private set
        var prefs: Prefs? = null
    }
}