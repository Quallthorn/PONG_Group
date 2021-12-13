package com.example.pong_group.Model

import android.graphics.Paint
import com.example.pong_group.Model.GameViewBreakout.Companion.canvasBreakout
import com.example.pong_group.Services.GameSettings
import com.example.pong_group.Services.GameSounds
import kotlin.math.abs
import kotlin.math.sqrt

class BallBreakout() {
    var radius = 50f
    var posX = radius
    var posY = radius
    var paint = Paint()
    var speed = 5f

    var dirX = 0.5f
    var dirY = 0.5f

    private val anglesCount = 10 // max 10 possibly 10.9 but not recommended
    private val maxSpeed = 20f

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
            bounceP1(pPosX, pWidth)
        }
        posX += speed * dirX
        posY += speed * dirY
    }

    fun draw() {
        canvasBreakout?.drawCircle(posX, posY, radius, paint)
    }

    private fun bounceP1(pPosX: Float, pWidth: Float) {
        for (i in 1 until anglesCount) {
            if (posX >= pPosX - (pWidth / anglesCount) * (anglesCount + 1 - i) && posX <= pPosX - (pWidth / anglesCount) * (anglesCount - i))
                dirX = -(anglesCount - i) / 10f
            else if (posX <= pPosX + (pWidth / anglesCount) * (anglesCount + 1 - i) && posX >= pPosX + (pWidth / anglesCount) * (anglesCount - i)) {
                dirX = (anglesCount - i) / 10f
            }
        }
        GameSounds.playSound()
        changeColor()
        dirY = -sqrt((1 - dirX * dirX).toDouble()).toFloat()

//        if (speed < maxSpeed) {
//            speed += 0.1f
//        }
    }

    fun centerBall(pPosX: Float, pPosY: Float) {
        posX = pPosX
        posY = GameSettings.screenHeight - (pPosY + GameSettings.screenHeight * 0.05f)
    }

    fun changeColor() {
        changeColor = true
    }
}