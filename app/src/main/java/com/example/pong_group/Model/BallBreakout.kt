package com.example.pong_group.Model

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.media.MediaPlayer
import com.example.pong_group.R
import com.example.pong_group.Services.NumberPrinter

class BallBreakout(context: Context, width: Float, height: Float) {
    var size = 5f
    var posX = size
    var posY = size
    var paint = Paint()
    var speed = 10f
    var context = context

    var dirX = 0.5f
    var dirY = 0.5f

    var screenWidth = width
    var screenHeight = height

    val anglesCount = 10 // max 10 possibly 10.9 but not recommended
    val pongSoung = MediaPlayer.create(context, R.raw.pong_sound)
    val maxSpeed = 30f

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
            playSound()
            if (dirY > 0) {
                dirY += ((0..2).random()) / 10f
                if (dirY > 1 || dirY < 0)
                    dirY = 0.5f
            } else {
                dirY -= ((0..2).random()) / 10f
                if (dirY < -1 || dirY > 0)
                    dirY = -0.5f
            }
            dirX = -Math.sqrt((1 - dirY * dirY).toDouble()).toFloat()
        } else if (posX <= size) {
            playSound()
            if (dirY > 0) {
                dirY += ((0..2).random()) / 10f
                if (dirY > 1 || dirY < 0)
                    dirY = 0.5f
            } else {
                dirY -= ((0..2).random()) / 10f
                if (dirY < -1 || dirY > 0)
                    dirY = -0.5f
            }
            dirX = Math.sqrt((1 - dirY * dirY).toDouble()).toFloat()
        }
        if (posY >= screenHeight - size) {
            centerBall(pPosX, pPosY)
            dirY = Math.abs(dirY) * -1
            NumberPrinter.scoreCPU += 1
        } else if (posY <= size) {
            playSound()
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
        playSound()
        changeColor()
        dirY = -Math.sqrt((1 - dirX * dirX).toDouble()).toFloat()

        if (speed < maxSpeed) {
            if (Math.abs(pPosX - pOldX) > screenWidth / 54)
                speed += 5f
            else
                speed += 0.1f

            if (speed < maxSpeed)
                speed == maxSpeed
        }
    }

    fun centerBall(pPosX: Float, pPosY: Float) {
        posX = pPosX
        posY = screenHeight - (pPosY + screenHeight * 0.01f)
    }

    fun playSound() {
        pongSoung.start()
    }

    fun changeColor() {
        changeColor = true
    }
}