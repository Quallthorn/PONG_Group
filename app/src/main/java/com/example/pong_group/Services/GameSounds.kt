package com.example.pong_group.Services

import android.media.MediaPlayer
import com.example.pong_group.Controller.App
import com.example.pong_group.R

object GameSounds {

    var appMuted: Boolean = false

    private val pongSound: MediaPlayer = MediaPlayer.create(App.instance, R.raw.pong_sound)

    fun playSound(){
        if(!appMuted) {
            pongSound.start()
        }
    }
}