package com.example.pong_group.Services

import android.media.MediaPlayer
import com.example.pong_group.Controller.App
import com.example.pong_group.R
import com.example.pong_group.Services.GameSounds.pongSound

object GameSounds {

    val pongSound = MediaPlayer.create(App.instance, R.raw.pong_sound)

    fun playSound(){
        pongSound.start()
    }
}