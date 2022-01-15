package com.example.pong_group.Services

import android.media.MediaPlayer
import com.example.pong_group.Controller.App
import com.example.pong_group.Controller.prefs
import com.example.pong_group.R

object GameSounds {

    private val pongSound: MediaPlayer = MediaPlayer.create(App.instance, R.raw.pong_sound)

    fun playSound(){
        if(prefs.isGameSoundOn) {
            pongSound.start()
        }
    }
}