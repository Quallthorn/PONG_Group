package com.example.pong_group.Model

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.media.MediaPlayer
import com.example.pong_group.Controller.App
import com.example.pong_group.R
import com.example.pong_group.Services.GameSounds
import com.example.pong_group.Services.NumberPrinter

class BallBreakout(width: Float, height: Float) {
    var size = 5f
    var posX = size
    var posY = size
    var paint = Paint()
    var speed = 30f

    var dirX = 0.5f
    var dirY = 0.5f

    var screenWidth = width
    var screenHeight = height

    val anglesCount = 10 // max 10 possibly 10.9 but not recommended
    val maxSpeed = 20f

    var changeColor = false

    init {
        posX = screenWidth * 0.5f
        posY = screenHeight * 0.5f
    }

    fun update(
        pPosX: Float,
        pPosY: Float,
        pWidth: Float,
        pOldX: Float
    ) {
        if (posX >= screenWidth - size) {
            GameSounds.playSound()
            dirX = Math.abs(dirX) * -1
        }
        else if (posX <= size){
            GameSounds.playSound()
            dirX = Math.abs(dirX)
        }
        if (posY >= screenHeight - size) {
            centerBall(pPosX, pPosY)
            dirY = Math.abs(dirY) * -1
        } else if (posY <= size) {
            GameSounds.playSound()
            dirY = Math.abs(dirY)
        }

        if (posY >= screenHeight - pPosY - size
            && posY <= screenHeight - pPosY + speed
            && posX >= pPosX - pWidth
            && posX <= pPosX + pWidth
        ) {
            bounceP1(pPosX, pWidth, pOldX)
        }
        posX += speed * dirX
        posY += speed * dirY
    }

    fun draw(canvas: Canvas?) {
        canvas?.drawCircle(posX, posY, size, paint)
    }

    fun bounceP1(pPosX: Float, pWidth: Float, pOldX: Float) {
        for (i in 1 until anglesCount) {
            if (posX >= pPosX - (pWidth / anglesCount) * (anglesCount + 1 - i) && posX <= pPosX - (pWidth / anglesCount) * (anglesCount - i))
                dirX = -(anglesCount - i) / 10f
            else if (posX <= pPosX + (pWidth / anglesCount) * (anglesCount + 1 - i) && posX >= pPosX + (pWidth / anglesCount) * (anglesCount - i)) {
                dirX = (anglesCount - i) / 10f
            }
        }
        GameSounds.playSound()
        changeColor()
        dirY = -Math.sqrt((1 - dirX * dirX).toDouble()).toFloat()

        if (speed < maxSpeed) {
            speed += 0.1f
        }
    }

    fun centerBall(pPosX: Float, pPosY: Float) {
        posX = pPosX
        posY = screenHeight - (pPosY + screenHeight * 0.01f)
    }

    fun changeColor() {
        changeColor = true
    }
}