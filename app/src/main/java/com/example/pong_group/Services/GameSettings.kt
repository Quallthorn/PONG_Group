package com.example.pong_group.Services

import android.app.Activity
import android.graphics.Canvas
import android.graphics.Paint
import com.example.pong_group.Controller.App
import com.example.pong_group.Controller.prefs
import com.example.pong_group.R
import java.io.File
import java.io.IOException
import java.io.PrintWriter
import java.lang.Exception
import java.util.*

object GameSettings {
    private var settingsFile: File? = null

    var screenWidth: Float = 0f
    var screenHeight: Float = 0f
    var baseHeightDimen = 1800
    var speedCoefficient = 1f

    private val colorArray = App.instance.resources.obtainTypedArray(R.array.rainbow)
    var curPaint = Paint()
    var curCanvas = Canvas()

    //ColorSettings
    var rainbowColor = prefs.isRainbowColor

    //BallSettings
    val ballMaxSpeed = 30f * speedCoefficient
    val anglesCount = 10

    //PongSettings
    var ballCount = 0
    var bestOf = prefs.bestOfPongPrefs
    var opponentP2 = prefs.isP2Human
    var gameOver = false

    //BreakoutSettings
    var classicBreakout = prefs.isClassicInterface
    var infiniteLevel = prefs.isInfiniteLevels

    // scores
    var highScorePong = 0
    var scorePong = 0

    var scoreBreakout = 0
    var highScoreBreakout = 0
    var highScoreBreakoutClassic = 0
    var highScoreBreakoutInfinite = 0

    fun createSettingsFile(activity: Activity) {
        settingsFile = File(activity.filesDir, "savedSettings.txt")
    }

    fun saveSettings() {
        try {
            val save = PrintWriter(settingsFile)

            save.write(
                if (GameSounds.appMuted)
                    "1"
                else
                    "0"
            )
            save.write("\n")
            save.write(
                if (rainbowColor)
                    "1"
                else
                    "0"
            )
            save.write("\n")
            save.write(bestOf.toString())
            save.write("\n")
            save.write(
                if (opponentP2)
                    "1"
                else
                    "0"
            )
            save.write("\n")
            save.write(
                if (classicBreakout)
                    "1"
                else
                    "0"
            )
            save.write("\n")
            save.write(
                if (infiniteLevel)
                    "1"
                else
                    "0"
            )
            save.close()
        } catch (ignored: IOException) {
        }
    }

    fun loadSettings() {
        var load: String
        try {
            val sc = Scanner(settingsFile)
            load = sc.nextLine()
            GameSounds.appMuted = load == "1"
            load = sc.nextLine()
            rainbowColor = load == "1"
            load = sc.nextLine()
            bestOf = load.toInt()
            load = sc.nextLine()
            opponentP2 = load == "1"
            load = sc.nextLine()
            classicBreakout = load == "1"
            load = sc.nextLine()
            infiniteLevel = load == "1"
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getRandomColorFromArray(): Int {
        val r = Random()
        val randomIndex = r.nextInt(colorArray.length())
        val randomColorInt = colorArray.getColor(randomIndex, 0)
        curPaint.color = randomColorInt
        return randomColorInt
    }

    fun setScreenDimen(width: Float, height: Float) {
        screenWidth = width
        screenHeight = height
        speedCoefficient = height / baseHeightDimen
    }

    fun updateScorePong() {
        if (scorePong > highScorePong)
            highScorePong = scorePong
    }

    fun updateScoreBreakout() {
        if( scoreBreakout > when {
            classicBreakout -> highScoreBreakoutClassic
            infiniteLevel -> highScoreBreakoutInfinite
            else -> highScoreBreakout
        })
            SharedBreakout.highScoreBroken = true
    }
}