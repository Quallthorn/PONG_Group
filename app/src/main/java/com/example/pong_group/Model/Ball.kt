package com.example.pong_group.Model

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint

class Ball(context: Context, width: Float, height: Float) {
    var posX = 50f
    var posY = 50f
    var paint = Paint()
    var size = 5f
    var speed = 10f

    var dirX = 1f
    var dirY = 1f

    var screenWidth = width
    var screenHeight = height

    init{
        posX = screenWidth * 0.5f
        posY = screenHeight * 0.5f
    }



    fun update(pPosX: Float, pPosY: Float, pWidth: Float, pHeight: Float){
        if (posX >= screenWidth - size)
            dirX = dirX * -1f
        else if (posX <= size)
            dirX = dirX * -1f

        if (posY >= screenHeight - size)
            dirY = dirY * -1f
        else if (posY <= size)
            dirY = dirY * -1f

        if (posY >= screenHeight - pPosY - size
            && posY <= screenHeight - pPosY + pHeight
            && posX >= pPosX - pWidth
            && posX <= pPosX + pWidth
        )
            dirY = dirY * -1

        posX += speed * dirX
        posY += speed * dirY
    }

    fun draw (canvas: Canvas?){
        canvas?.drawCircle(posX,posY,size,paint)
    }
}