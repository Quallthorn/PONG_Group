package com.example.pong_group.Services

import com.example.pong_group.Controller.App
import com.example.pong_group.R
import java.util.*

object GameSettings {

    val colorArray = App.instance.resources.obtainTypedArray(R.array.rainbow)
    //val breakoutColorArray = App.instance.resources.obtainTypedArray(R.array.breakout_bricks)
    val colorArray = App.instance.resources.obtainTypedArray(R.array.breakout)
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
}