package com.example.pong_group.Services

import android.graphics.Paint
import android.util.Log
import com.example.pong_group.Controller.App
import com.example.pong_group.Model.Brick
import com.example.pong_group.R
import java.util.*
import kotlin.properties.Delegates

object GameSettings {
    var screenWidth: Float = 0f
    var screenHeight: Float = 0f

    private val colorArray = App.instance.resources.obtainTypedArray(R.array.rainbow)
    var curPaint = Paint()


    //Breakout scores
    var highScoreBreakout = 0f
    var scoreBreakout = 0f
    var brickCounts = mutableListOf<Int>()
    var lowestBrick by Delegates.notNull<Int>()
    var rows by Delegates.notNull<Int>()

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
            if (brickCounts[i] != null)
                brickCounts[i] = countX
            else
                brickCounts.add(countX)
        }
        lowestBrick = countY
        rows = countY
    }

    fun addScore(pointBase: Int){
        scoreBreakout += pointBase * 100
//        if (pointBase + lowestBrick != rows + 1)
//            scoreBreakout += lowestBrick * 10 * brickCounts[lowestBrick]
        brickCounts[pointBase]--
        Log.d("count", "${brickCounts[pointBase]}")
//        while (brickCounts[lowestBrick] == 0 && lowestBrick > 0)
//            lowestBrick--
    }
}