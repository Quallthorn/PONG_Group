package com.example.pong_group.services

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import com.example.pong_group.controller.prefs
import com.example.pong_group.R

enum class Sounds{
    WALL,
    BRICK,
    LIFE,
    CLICK
}


object GameSounds {
    //Game Sounds singleton class. Union class for manage sounds inside of game
    private var soundPool: SoundPool? = null
    private var pong_beep: Int = 0
    private var pong_bop: Int = 0
    private var brick: Int = 0
    private var lost: Int = 0
    private var click: Int = 0

    var alternate = false

    //Creating sound pool for can run multiply sounds simultaneously
    fun createSoundPool(context: Context) {
            val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()

            soundPool = SoundPool.Builder()
                .setMaxStreams(6)
                .setAudioAttributes(audioAttributes)
                .build()

        pong_beep = soundPool?.load(context, R.raw.pong_sound_high, 1) ?: 0
        pong_bop = soundPool?.load(context, R.raw.pong_sound, 1) ?: 0
        brick = soundPool?.load(context, R.raw.brick_break, 1) ?: 0
        lost = soundPool?.load(context, R.raw.crumble, 1) ?: 0
        click = soundPool?.load(context, R.raw.click_on, 1) ?: 0
    }

    fun playSound(sounds: Sounds) {
        if (!prefs.isGameMute) {
            when(sounds){
                Sounds.WALL -> playWall()
                Sounds.BRICK -> playBrick()
                Sounds.LIFE -> playLifeLost()
                Sounds.CLICK -> playClick()
            }
        }
    }

    // sound played when ball touches wall
    private fun playWall() {
            alternate = if (alternate) {
                soundPool!!.play(pong_beep, 1f, 1f, 0, 0, 1f)
                false
            } else {
                soundPool!!.play(pong_bop, 1f, 1f, 0, 0, 1f)
                true
        }
    }
    // sound of destroying brick
    private fun playBrick() {
            soundPool!!.play(brick, 1f, 1f, 0, 0, 1f)

    }

    // sound played every time when player lost life
    private fun playLifeLost() {
            soundPool!!.play(lost, 1f, 1f, 0, 0, 1f)
    }

    //sound of players click
    private fun playClick() {
                soundPool!!.play(click, 1f, 1f, 0, 0, 1f)
    }
}