package com.example.pong_group.services

import android.graphics.Canvas
import android.graphics.Paint
import com.example.pong_group.controller.App
import com.example.pong_group.controller.prefs
import com.example.pong_group.R
import java.util.*

//Singleton class for internal sharing settings
object GameSettings {
    //settings based on screen size
    var screenWidth: Float = 0f
    var screenHeight: Float = 0f
    private var baseHeightDimen = 1962f
    var speedCoefficient = 1f

    //color and canvas settings. Create united canvas for all surfaceview
    private val colorArray = App.instance.resources.obtainTypedArray(R.array.rainbow)
    var curPaint = Paint()
    var curCanvas = Canvas()

    //BallSettings. Setup angle and ball speed based on screen size
    val ballMaxSpeed = 40f * speedCoefficient
    const val anglesCount = 10

    //PongSettings. number of balls and game over option
    var ballCount = 0
    var gameOver = false

    //Scores. Keeping all current scores for games
    var scoreBreakout = 0
    var savedScore = 0
    //TODO: Do we really need different variables for scores?
    var highScoreBreakout = 0
    var highScoreBreakoutClassic = 0
    var highScoreBreakoutInfinite = 0

    //func for getting random color from color array
    fun getRandomColorFromArray(): Int {
        val r = Random()
        val randomIndex = r.nextInt(colorArray.length())
        val randomColorInt = colorArray.getColor(randomIndex, 0)
        curPaint.color = randomColorInt
        return randomColorInt
    }

    //func for calculate ball speed coefficient based on screen size just for getting equal speed of ball for different screens
    fun setScreenDimen(width: Float, height: Float) {
        screenWidth = width
        screenHeight = height
        speedCoefficient = height / baseHeightDimen
    }

    //keep high scores based on game type
    //todo: can we trim this func?
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