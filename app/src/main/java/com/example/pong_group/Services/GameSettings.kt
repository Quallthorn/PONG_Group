package com.example.pong_group.Services

import android.graphics.Paint
import com.example.pong_group.Controller.App
import com.example.pong_group.R
import java.util.*

object GameSettings {
    var screenWidth: Float = 0f
    var screenHeight: Float = 0f

    private val colorArray = App.instance.resources.obtainTypedArray(R.array.rainbow)
    var curPaint = Paint()

    fun getRandomColorFromArray(): Int{
        val r = Random()
        val randomIndex = r.nextInt(colorArray.length())
        val randomColorInt = colorArray.getColor(randomIndex, 0)
        curPaint.color = randomColorInt
        return randomColorInt
    }

//    fun getColorFromArray(): Int{
//        val r = Random()
//        val randomIndex = r.nextInt(breakoutColorArray.length())
//        val colorInt = colorArray.getColor(randomIndex, 0)
//        return colorInt
//    }

    fun setScreenDimen(width: Float, height: Float){
        screenWidth = width
        screenHeight = height
    }
}