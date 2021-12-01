package com.example.pong_group.Model

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Binder
import android.view.SurfaceView

class Ball(context: Context) {
    var posX = 0f
    var posY = 0f
    var paint = Paint()
    var size = 5f
    var speed = 10f

    var dirX = 1
    var dirY = 1

    fun update(surface: SurfaceView){

        if (posX >= surface.width.toFloat() - size)
            dirX = -1
        else if (posX <= size)
            dirX = 1

        if (posY >= surface.height.toFloat() - size)
            dirY = -1
        else if (posY <= size)
            dirY = 1

//        if (posY >= surface.height.toFloat() - paddleY - size
//            && posY <= surface.height.toFloat() - paddleY + paddleH
//            && posX >= this.paddleX - paddleW
//            && posX <= this.paddleX + paddleW
//        )
//            dirY = dirY * -1

        posX += speed
        posY += speed
    }

    fun draw (canvas: Canvas?){
        canvas?.drawCircle(posX,posY,size,paint)
    }
}