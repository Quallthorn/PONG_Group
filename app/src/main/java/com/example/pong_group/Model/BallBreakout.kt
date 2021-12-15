package com.example.pong_group.Model

import android.graphics.Paint
import com.example.pong_group.Model.GameViewBreakout.Companion.canvasBreakout
import com.example.pong_group.Services.GameSettings
import com.example.pong_group.Services.GameSounds
import kotlin.math.abs
import kotlin.math.sqrt

class BallBreakout() {
    var radius = 5f
    var posX = radius
    var posY = radius
    var paint = Paint()
    var speed = 10f

    var dirX = 0.5f
    var dirY = 0.5f

    private val anglesCount = 10 // max 10 possibly 10.9 but not recommended
    private val maxSpeed = 30f

    var changeColor = false

    init {
        posX = GameSettings.screenWidth * 0.5f
        posY = GameSettings.screenHeight * 0.5f
    }

    fun update(
        pPosX: Float,
        pPosY: Float,
        pWidth: Float
    ) {
        if (posX >= GameSettings.screenWidth - radius) {
            GameSounds.playSound()
            dirX = abs(dirX) * -1
        }
        else if (posX <= radius){
            GameSounds.playSound()
            dirX = abs(dirX)
        }
        if (posY >= GameSettings.screenHeight - radius) {
            centerBall(pPosX, pPosY)
            dirY = abs(dirY) * -1
        } else if (posY <= radius) {
            GameSounds.playSound()
            dirY = abs(dirY)
        }

        if (posY >= GameSettings.screenHeight - pPosY - radius
            && posY <= GameSettings.screenHeight - pPosY + speed
            && posX + radius >= pPosX - pWidth
            && posX - radius <= pPosX + pWidth
        ) {
            bounce(pPosX, pWidth)
        }
        posX += speed * dirX
        posY += speed * dirY
    }

    fun draw() {
        canvasBreakout.drawCircle(posX, posY, radius, paint)
    }

    private fun bounce(pPosX: Float, pWidth: Float) {
        when {
            posX < pPosX - pWidth -> {
                dirX = -0.9f
                if (speed < maxSpeed) {
                    speed += 1f
                }
            }
            posX > pPosX + pWidth -> {
                dirX = 0.9f
                if (speed < maxSpeed) {
                    speed += 1f
                }
            }
            else -> {
                for (i in 1 until anglesCount) {
                    if (posX >= pPosX - (pWidth / anglesCount) * (anglesCount + 1 - i) && posX <= pPosX - (pWidth / anglesCount) * (anglesCount - i))
                        dirX = -(anglesCount - i) / 10f
                    else if (posX <= pPosX + (pWidth / anglesCount) * (anglesCount + 1 - i) && posX >= pPosX + (pWidth / anglesCount) * (anglesCount - i)) {
                        dirX = (anglesCount - i) / 10f
                    }
                }
                if (speed < maxSpeed) {
                    speed += 0.1f
                }
            }
        }
        dirY = -sqrt((1 - dirX * dirX).toDouble()).toFloat()
        GameSounds.playSound()
        changeColor()
    }

    fun centerBall(pPosX: Float, pPosY: Float) {
        posX = pPosX
        posY = GameSettings.screenHeight - (pPosY + radius)
    }

    private fun changeColor() {
        changeColor = true
    }
}