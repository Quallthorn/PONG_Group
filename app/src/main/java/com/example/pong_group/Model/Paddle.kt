package com.example.pong_group.Model

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.SurfaceView

class Paddle(context: Context, width: Float, height: Float) {

    var posX: Float = 1f
    var posY: Float = 250f
    var height: Float = 10f
    var width: Float = 80f
    var paint = Paint()

    var screenWidth = width
    var screenHeight = height

    fun update(){

    }

    fun draw (canvas: Canvas?){
        canvas?.drawRect(
            posX - width,
            screenHeight - posY,
            posX + width,
            screenHeight - posY + height,
            paint
        )
    }
}