package com.example.pong_group.Services

import android.graphics.Canvas
import android.graphics.Paint
import android.util.Log
import com.example.pong_group.Controller.App
import com.example.pong_group.R
import java.util.*
import kotlin.properties.Delegates

object GameSettings {
    var screenWidth: Float = 0f
    var screenHeight: Float = 0f
    var baseHeightDimen = 1800
    var speedCoefficient = 1f

    private val colorArray = App.instance.resources.obtainTypedArray(R.array.rainbow)
    var curPaint = Paint()
    var curCanvas = Canvas()

    //BallSettings
    val ballMaxSpeed = 30f * speedCoefficient
    val anglesCount = 10

    //pongSettings
    var ballCount = 0

    //Classic
    var classicBreakout = false

    // scores
    var highScorePong = 0
    var scorePong = 0

    var highScoreBreakout = 0
    var scoreBreakout = 0

    var highScoreBreakoutClassic = 0
    var scoreBreakoutClassic = 0


    fun getRandomColorFromArray(): Int{
        val r = Random()
        val randomIndex = r.nextInt(colorArray.length())
        val randomColorInt = colorArray.getColor(randomIndex, 0)
        curPaint.color = randomColorInt
        return randomColorInt
    }

    fun setScreenDimen(width: Float, height: Float){
        screenWidth = width
        screenHeight = height
        speedCoefficient = height / baseHeightDimen
    }

    fun updateScorePong(){
        if (scorePong > highScorePong)
            highScorePong = scorePong
    }

    fun updateScoreBreakout(){
        if (scoreBreakout > highScoreBreakout){
            highScoreBreakout = scoreBreakout
            SharedBreakout.highScoreBroken = true
        }
    }

    fun updateScoreBreakoutClassic(){
        if (scoreBreakoutClassic > highScoreBreakoutClassic){
            highScoreBreakoutClassic = scoreBreakoutClassic
            SharedBreakout.highScoreBroken = true
        }
    }
}