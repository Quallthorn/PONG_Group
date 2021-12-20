package com.example.pong_group.Services

import android.graphics.Paint
import android.util.Log
import com.example.pong_group.Controller.App
import com.example.pong_group.R
import java.util.*
import kotlin.properties.Delegates

object GameSettings {
    var screenWidth: Float = 0f
    var screenHeight: Float = 0f

    private val colorArray = App.instance.resources.obtainTypedArray(R.array.rainbow)
    var curPaint = Paint()


    //Breakout
    var classicBreakout = false
    const val ballSpeedStart = 10f
    var ballSpeed = ballSpeedStart
    private const val speedIncrease = 4f
    var hits = 0
    var orangeHit = false
    var redHit = false
    var upperWallHit = false
    var maxSpeedAchieved = false
    // scores
    var highScoreBreakout = 0f
    var scoreBreakout = 0f
    var brickCounts = mutableListOf<Int>()
    var lowestBrick by Delegates.notNull<Int>()
    var totalRows by Delegates.notNull<Int>()

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
    }

    fun gameSetUpBreakout(countX: Int, countY: Int){
        for (i in 0 until countY){
//            if (brickCounts[i])
//                brickCounts[i] = countX
//            else
                brickCounts.add(countX)
        }
        lowestBrick = countY - 1
        totalRows = countY
    }

    fun addScore(pointBase: Int){
        scoreBreakout += pointBase
        if (pointBase + lowestBrick != totalRows)
            scoreBreakout += lowestBrick * brickCounts[lowestBrick]
        brickCounts[pointBase]--
        while (brickCounts[lowestBrick] == 0 && lowestBrick > 0)
            lowestBrick--

        Log.d("scoreNormal", "$scoreBreakout")
    }

    fun addScoreClassic(pointBase: Int){
        scoreBreakout += pointBase
        Log.d("classic", "Points: $scoreBreakout")
    }

    fun updateSpeedClassic(brickColor: Int){
        if (hits <= 12)
            hits++
        if (hits == 4 || hits == 12)
            ballSpeed += speedIncrease
        if (brickColor == 5 && !orangeHit){
            ballSpeed += speedIncrease
            orangeHit = true
        }
        if (brickColor == 7 && !redHit){
            ballSpeed += speedIncrease
            redHit = true
        }
        if (ballSpeed >= ballSpeedStart + speedIncrease * 4){
            ballSpeed = ballSpeedStart + speedIncrease * 4
            maxSpeedAchieved = true
        }
        Log.d("classic", "Hits: $hits")
        Log.d("classic", "Speed: $ballSpeed")
    }
}