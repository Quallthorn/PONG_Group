package com.example.pong_group.services

import android.content.Context
import android.content.SharedPreferences

//preferences class for keeping all games preference with help of shared preferences and load settings from memory
class Prefs(context: Context) {

    private val preferences: SharedPreferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)

    private val GAME_SOUND_STATE = "gameSound"
    private val RAINBOW_COLOR = "rainbowColor"
    private val FIRST_TO_PONG = "firstTO"
    private val P2_SELECTOR = "isP2Human"
    private val IS_CLASSIC_INTERFACE = "isClassicInterface"
    private val IS_INFINITE_LEVELS = "isInfiniteLevel"

    var isGameMute: Boolean
        get() = preferences.getBoolean(GAME_SOUND_STATE, true)
        set(value) = preferences.edit().putBoolean(GAME_SOUND_STATE, value).apply()

    var isRainbowColor: Boolean
        get() = preferences.getBoolean(RAINBOW_COLOR, true)
        set(value) = preferences.edit().putBoolean(RAINBOW_COLOR, value).apply()

    var firstToPongPrefs: Int
        get() = preferences.getInt(FIRST_TO_PONG, 1)
        set(value) = preferences.edit().putInt(FIRST_TO_PONG, value).apply()

    var isP2Human: Boolean
        get() = preferences.getBoolean(P2_SELECTOR, false)
        set(value) = preferences.edit().putBoolean(P2_SELECTOR, value).apply()

    var isClassicInterface: Boolean
        get() = preferences.getBoolean(IS_CLASSIC_INTERFACE, false)
        set(value) = preferences.edit().putBoolean(IS_CLASSIC_INTERFACE, value).apply()

    var isInfiniteLevels: Boolean
        get() = preferences.getBoolean(IS_INFINITE_LEVELS, false)
        set(value) = preferences.edit().putBoolean(IS_INFINITE_LEVELS, value).apply()
}