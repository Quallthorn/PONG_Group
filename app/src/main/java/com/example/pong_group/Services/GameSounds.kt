package com.example.pong_group.Services

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import com.example.pong_group.Controller.prefs
import com.example.pong_group.R


object GameSounds {

    private var soundPool: SoundPool? = null
    private var pong_beep: Int = 0
    private var pong_bop: Int = 0
    private var brick: Int = 0
    private var lost: Int = 0
    private var click: Int = 0

    private var alternate = true

    fun createSoundPool(context: Context) {
        soundPool = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
            SoundPool.Builder()
                .setMaxStreams(6)
                .setAudioAttributes(audioAttributes)
                .build()
        } else {
            SoundPool(6, AudioManager.STREAM_MUSIC, 0)
        }
        pong_beep = soundPool?.load(context, R.raw.pong_sound_high, 1) ?: 0
        pong_bop = soundPool?.load(context, R.raw.pong_sound, 1) ?: 0
        brick = soundPool?.load(context, R.raw.brick_break, 1) ?: 0
        lost = soundPool?.load(context, R.raw.crumble, 1) ?: 0
        click = soundPool?.load(context, R.raw.click_on, 1) ?: 0
    }

    fun playWall() {
        if (!prefs.isGameMute) {
            alternate = if (alternate) {
                soundPool!!.play(pong_beep, 1f, 1f, 0, 0, 1f)
                false
            } else {
                soundPool!!.play(pong_bop, 1f, 1f, 0, 0, 1f)
                true
            }
        }
    }

    fun playBrick() {
        if (!prefs.isGameMute) {
            soundPool!!.play(brick, 1f, 1f, 0, 0, 1f)
        }
    }

    fun playLifeLost() {
        if (!prefs.isGameMute) {
            soundPool!!.play(lost, 1f, 1f, 0, 0, 1f)
        }
    }

    fun playClick() {
        if (!prefs.isGameMute) {
                soundPool!!.play(click, 1f, 1f, 0, 0, 1f)
        }
    }
}