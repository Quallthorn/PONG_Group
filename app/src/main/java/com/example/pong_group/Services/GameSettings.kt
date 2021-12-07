package com.example.pong_group.Services

import android.graphics.Paint
import com.example.pong_group.Controller.App
import com.example.pong_group.Controller.MainActivity
import com.example.pong_group.R
import java.util.*

object GameSettings {

    val colorArray = App.instance.resources.obtainTypedArray(R.array.breakout)
    var curPaint = Paint()

    fun getRandomColorFromArray(): Int{
        val r = Random()
        val randomIndex = r.nextInt(colorArray.length())
        val randomColorInt = colorArray.getColor(randomIndex, 0)
        curPaint.color = randomColorInt
        return randomColorInt
    }
}