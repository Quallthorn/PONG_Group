package com.example.pong_group.Model

import android.graphics.Paint
import com.example.pong_group.Controller.App
import com.example.pong_group.Model.GameViewBreakout.Companion.breakBuffer
import com.example.pong_group.Model.GameViewBreakout.Companion.canvasBreakout
import com.example.pong_group.Model.GameViewBreakout.Companion.lives
import com.example.pong_group.Model.GameViewBreakout.Companion.outOfLives
import com.example.pong_group.R
import com.example.pong_group.Services.GameSettings
import com.example.pong_group.Services.GameSounds
import kotlin.math.abs
import kotlin.math.sqrt

class BallBreakout() {
    private val classic = GameSettings.classicBreakout
    var radius = 7f
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
        if (classic)
            speed = GameSettings.ballSpeedStart
        paint.color = App.instance.resources.getColor(R.color.white)
        posX = GameSettings.screenWidth * 0.5f
        posY = GameSettings.screenHeight * 0.5f
    }

    fun update(player: PaddleBreakout) {
        if (posX >= GameSettings.screenWidth - radius) {
            GameSounds.playSound()
            dirX = abs(dirX) * -1
        }
        else if (posX <= radius){
            GameSounds.playSound()
            dirX = abs(dirX)
        }
        if (posY >= GameSettings.screenHeight - radius) {
            centerBall(player.posX, player.posY)
            dirY = abs(dirY) * -1
            lives -= 1
        } else if (posY <= radius) {
            GameSounds.playSound()
            dirY = abs(dirY)
            if (classic && !GameSettings.upperWallHit){
                player.halfSize()
                GameSettings.upperWallHit = true
            }
        }

        if (posY >= GameSettings.screenHeight - player.posY - radius
            && posY <= GameSettings.screenHeight - player.posY + speed
            && posX + radius >= player.posX - player.width
            && posX - radius <= player.posX + player.width
        ) {
            speedCheck(player.posX, player.width)
        }
        posX += speed * dirX
        posY += speed * dirY

        if (lives == 0){
            outOfLives = true
        }
        breakBuffer = true
    }

    fun draw() {
        canvasBreakout.drawCircle(posX, posY, radius, paint)
    }

    private fun speedCheck(pPosX: Float, pWidth: Float) {
            when {
                posX < pPosX - pWidth -> {
                    dirX = -0.9f
                    if (speed < maxSpeed && !classic) {
                        speed += 1f
                    }
                }
                posX > pPosX + pWidth -> {
                    dirX = 0.9f
                    if (speed < maxSpeed && !classic) {
                        speed += 1f
                    }
                }
                else -> {
                    bounce(pPosX, pWidth)
                    if (speed < maxSpeed && !classic) {
                        speed += 0.1f
                    }
                }
            }
        if (classic)
            speed = GameSettings.ballSpeed
        dirY = -sqrt((1 - dirX * dirX).toDouble()).toFloat()
        GameSounds.playSound()
        changeColor()
    }

    private fun bounce(pPosX: Float, pWidth: Float){
        for (i in 1 until anglesCount) {
            if (posX >= pPosX - (pWidth / anglesCount) * (anglesCount + 1 - i) && posX <= pPosX - (pWidth / anglesCount) * (anglesCount - i))
                dirX = -(anglesCount - i) / 10f
            else if (posX <= pPosX + (pWidth / anglesCount) * (anglesCount + 1 - i) && posX >= pPosX + (pWidth / anglesCount) * (anglesCount - i)) {
                dirX = (anglesCount - i) / 10f
            }
        }
    }

    fun centerBall(pPosX: Float, pPosY: Float) {
        posX = pPosX
        posY = GameSettings.screenHeight - (pPosY + radius)
    }

    private fun changeColor() {
        changeColor = true
    }
}