package com.example.pong_group.Model

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import com.example.pong_group.R

class Paddle(context: Context, width: Float, height: Float) {

    var posX: Float = 1f
    var posY: Float = 250f
    var height: Float = 10f
    var width: Float = 80f
    var paint = Paint()

    var screenWidth = width
    var screenHeight = height

    var posXOld: Float = 0f
    val frameCheck = 2
    var currentFrame = 0
    val updateOldPos = true

    init {
        this.paint.color = context.resources.getColor(R.color.white)
    }

    fun update(){
        if (currentFrame == frameCheck){
            posXOld = posX
            currentFrame = 0
        }
        else
            currentFrame++
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