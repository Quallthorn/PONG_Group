package com.example.pong_group.Services

import android.graphics.Canvas
import android.graphics.Paint
import com.example.pong_group.Controller.App
import com.example.pong_group.Controller.prefs
import com.example.pong_group.R
import java.util.*

object GameSettings {
    var screenWidth: Float = 0f
    var screenHeight: Float = 0f
    var baseHeightDimen = 1962f
    var speedCoefficient = 1f

    private val colorArray = App.instance.resources.obtainTypedArray(R.array.rainbow)
    var curPaint = Paint()
    var curCanvas = Canvas()

    //BallSettings
    val ballMaxSpeed = 40f * speedCoefficient
    const val anglesCount = 10

    //PongSettings
    var ballCount = 0
    var gameOver = false

    //Scores
    var scoreBreakout = 0
    var highScoreBreakout = 0
    var highScoreBreakoutClassic = 0
    var highScoreBreakoutInfinite = 0

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

    fun updateScoreBreakout() {
        if (scoreBreakout > when {
                prefs.isClassicInterface -> highScoreBreakoutClassic
                prefs.isInfiniteLevels -> highScoreBreakoutInfinite
                else -> highScoreBreakout
            }
        )
            SharedBreakout.highScoreBroken = true
    }
}