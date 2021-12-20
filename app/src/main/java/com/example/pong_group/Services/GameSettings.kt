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

    //Classic
    var classicBreakout = true

    // scores
    var highScoreBreakout = 0f
    var scoreBreakout = 0


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
}