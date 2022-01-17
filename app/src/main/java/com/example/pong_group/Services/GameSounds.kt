package com.example.pong_group.Services

import android.media.MediaPlayer
import com.example.pong_group.Controller.App
import com.example.pong_group.Controller.prefs
import com.example.pong_group.R

object GameSounds {

    private val bounce: MediaPlayer = MediaPlayer.create(App.instance, R.raw.pong_sound)
    private val brickDestroy: MediaPlayer = MediaPlayer.create(App.instance, R.raw.crumble)

    fun playSoundBounce(){
        if(!prefs.isGameMute) {
            bounce.start()
        }
    }

    fun playSoundBrick(){
        if(!prefs.isGameMute) {
            brickDestroy.start()
        }
    }
}