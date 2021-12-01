package com.example.pong_group.Model

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint

class Ball(context: Context, width: Float, height: Float) {
    private var size = 5f
    var posX = size
    var posY = size
    var paint = Paint()
    var speed = 10f

    var dirX = 0.5f
    var dirY = 0.5f

    var screenWidth = width
    var screenHeight = height

    var draw = true

    init {
        posX = screenWidth * 0.5f
        posY = screenHeight * 0.5f
    }


    fun update(pPosX: Float, pPosY: Float, pWidth: Float, pHeight: Float) {
        if (draw){
            if (posX >= screenWidth - size) {
                var s = 0f
                s = (0..100).random() / 100f
                dirX = s * -1f
                dirY = 1 - (Math.abs(dirY)/dirY * s)
            } else if (posX <= size) {
                var s = 0f
                s = (0..100).random() / 100f
                dirX = s * 1f
                dirY = 1 - (Math.abs(dirY)/dirY * s)
            }
            if (posY >= screenHeight - size){
                var s = 0f
                s = (0..100).random() / 100f
                dirY = s * -1f
                dirX = 1 - (Math.abs(dirX)/dirX * s)
                //draw = false
            }
            else if (posY <= size) {
                var s = 0f
                s = (0..100).random() / 100f
                dirY = s * 1f
                dirX = 1 - (Math.abs(dirX)/dirX * s)
            }

            if (posY >= screenHeight - pPosY - size
                && posY <= screenHeight - pPosY + pHeight
                && posX >= pPosX - pWidth
                && posX <= pPosX + pWidth
            )
            dirY = dirY * -1

            posX += speed * dirX
            posY += speed * dirY
        }
    }

    fun draw(canvas: Canvas?) {
        if (draw)
            canvas?.drawCircle(posX, posY, size, paint)
    }

    fun setSize(size: Float) {
        this.posX += size - this.size
        this.posY += size - this.size
        this.size = size
    }
}