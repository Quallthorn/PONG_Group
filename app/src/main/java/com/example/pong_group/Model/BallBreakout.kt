package com.example.pong_group.Model

import com.example.pong_group.Controller.App
import com.example.pong_group.Model.GameViewBreakout.Companion.breakReady

import com.example.pong_group.Model.GameViewBreakout.Companion.lives
import com.example.pong_group.Model.GameViewBreakout.Companion.outOfLives
import com.example.pong_group.R
import com.example.pong_group.Services.GameSettings
import com.example.pong_group.Services.GameSettings.anglesCount
import com.example.pong_group.Services.GameSettings.ballMaxSpeed
import com.example.pong_group.Services.GameSettings.classicBreakout
import com.example.pong_group.Services.GameSounds.playSound
import com.example.pong_group.Services.SharedBreakout
import kotlin.math.abs
import kotlin.math.sqrt

class BallBreakout(): BasicBall() {
    var checkCollision = false

    init {
        speed = 15f*GameSettings.speedCoefficient
        if (classicBreakout)
            speed = SharedBreakout.ballSpeedStart
        paint.color = App.instance.resources.getColor(R.color.white, App.instance.theme)
    }

    fun update(player: PaddleBreakout) {
        //screen edges
        if (posX >= GameSettings.screenWidth - radius) {
            playSound()
            dirX = abs(dirX) * -1
        } else if (posX <= radius) {
            playSound()
            dirX = abs(dirX)
        }
        if (posY >= GameSettings.screenHeight - radius) {
            centerBall(player.posX, player.posY)
            dirY = abs(dirY) * -1
            lives -= 1
        } else if (posY <= radius + GameViewBreakout.ballEdgeTop) {
            playSound()
            dirY = abs(dirY)
            if (classicBreakout && !SharedBreakout.upperWallHit) {
                player.halfSize()
                SharedBreakout.upperWallHit = true
            }
        }

        //player paddle
        if (posY >= GameSettings.screenHeight - player.posY - radius
            && posY <= GameSettings.screenHeight - player.posY + speed
            && posX + radius >= player.posX - player.width
            && posX - radius <= player.posX + player.width
        ) {
            speedCheck(player.posX, player.width)
        }

        //goes "forward" if everything is normal
        if (!checkCollision) {
            posX += speed * dirX
            posY += speed * dirY
        } else // goes "backwards" if ball overshoots
        {
            posX -= speed * dirX * 0.25f
            posY -= speed * dirY * 0.25f
        }

        if (lives == 0) {
            outOfLives = true
        }
        breakReady = true
    }

    private fun speedCheck(pPosX: Float, pWidth: Float) {
        when {
            posX < pPosX - pWidth -> {
                dirX = -0.9f
                if (speed < ballMaxSpeed && !classicBreakout) {
                    speed += 1f
                }
            }
            posX > pPosX + pWidth -> {
                dirX = 0.9f
                if (speed < ballMaxSpeed && !classicBreakout) {
                    speed += 1f
                }
            }
            else -> {
                bounce(pPosX, pWidth)
                if (speed < ballMaxSpeed && !classicBreakout) {
                    speed += 0.1f
                }
            }
        }
        if (classicBreakout)
            speed = SharedBreakout.ballSpeed
        dirY = -sqrt((1 - dirX * dirX).toDouble()).toFloat()
        playSound()
        changeColor()
    }

    private fun bounce(pPosX: Float, pWidth: Float) {
        for (i in 1 until anglesCount) {
            if (posX >= pPosX - (pWidth / anglesCount) * (anglesCount + 1 - i) && posX <= pPosX - (pWidth / anglesCount) * (anglesCount - i))
                dirX = -(anglesCount - i) / 10f
            else if (posX <= pPosX + (pWidth / anglesCount) * (anglesCount + 1 - i) && posX >= pPosX + (pWidth / anglesCount) * (anglesCount - i)) {
                dirX = (anglesCount - i) / 10f
            }
        }
    }

    fun centerBall(pPosX: Float, pPosY: Float) {
        dirY = abs(dirY) * -1
        posX = pPosX
        posY = GameSettings.screenHeight - (pPosY + radius)
    }

}