package com.example.pong_group.Model

import android.graphics.Paint
import com.example.pong_group.Services.GameSettings
import com.example.pong_group.Services.GameSettings.anglesCount
import com.example.pong_group.Services.GameSettings.ballMaxSpeed
import com.example.pong_group.Services.GameSettings.curCanvas
import com.example.pong_group.Services.GameSounds
import kotlin.math.abs
import kotlin.math.sqrt

class Ball() {
    var radius = 7f
    var posX = radius
    var posY = radius
    var dirX = 0.5f
    var dirY = 0.5f
    var paint = Paint()

//    private val anglesCount = 10 // max 10 possibly 10.9 but not recommended
    private val startSpeed = 10f
    var speed = 10f
//    private val maxSpeed = 30f

    var changeColor = false
    var start = false
    var p1Scored = false

    init {
        posX = GameSettings.screenWidth * 0.5f
        posY = GameSettings.screenHeight * 0.5f
    }

    fun update(
        player: PaddlePong,
        cpuX: Float
    ) {
        if (start) {
            //right side
            if (posX >= GameSettings.screenWidth - radius) {
                GameSounds.playSound()
                if (dirY > 0) {
                    dirY += ((0..2).random()) / 10f
                    if (dirY > 1 || dirY < 0)
                        dirY = 0.5f
                } else {
                    dirY -= ((0..2).random()) / 10f
                    if (dirY < -1 || dirY > 0)
                        dirY = -0.5f
                }
                dirX = -sqrt(1 - dirY * dirY)

                //left side
            } else if (posX <= radius) {
                GameSounds.playSound()
                if (dirY > 0) {
                    dirY += ((0..2).random()) / 10f
                    if (dirY > 1 || dirY < 0)
                        dirY = 0.5f
                } else {
                    dirY -= ((0..2).random()) / 10f
                    if (dirY < -1 || dirY > 0)
                        dirY = -0.5f
                }
                dirX = sqrt(1 - dirY * dirY)
            }


            //player side
            if (posY >= GameSettings.screenHeight - radius) {
                dirY = -1f
                dirX = 0f
                p1Scored = false
                posY = GameSettings.screenHeight - (player.posY + radius)
                resetBall(false)
                //cpu side
            } else if (posY <= radius) {
                dirY = 1f
                dirX = 0f
                p1Scored = true
                posY = player.posY + player.height + radius
                resetBall(true)
            }


            //player paddle
            if (posY + radius >= GameSettings.screenHeight - player.posY
                && posY + radius <= GameSettings.screenHeight - player.posY + player.height + speed
                && posX + radius >= player.posX - player.width
                && posX - radius <= player.posX + player.width
            ) {
                bounceP1(player)
            }

            //cpu paddle
            if (posY - radius <= player.posY + player.height
                && posY - radius >= player.posY - speed
                && posX >= cpuX - player.width
                && posX <= cpuX + player.width
            ) {
                bounceCPU(cpuX, player.width)
            }

            posX += speed * dirX
            posY += speed * dirY
        } else {
            posX = if (p1Scored)
                cpuX
            else {
                player.posX
            }
        }
    }

    fun draw() {
        curCanvas.drawCircle(posX, posY, radius, paint)
    }

    private fun bounceP1(player: PaddlePong) {
        when {
            posX < player.posX - player.width -> {
                dirX = -0.9f
                if (speed < ballMaxSpeed && abs(player.posX - player.posXOld) > GameSettings.screenWidth / 54) {
                    speed += 5f
                }
            }
            posX > player.posX + player.width -> {
                dirX = 0.9f
                if (speed < ballMaxSpeed && abs(player.posX - player.posXOld) > GameSettings.screenWidth / 54) {
                    speed += 5f
                }
            }
            else -> {
                for (i in 1 until anglesCount) {
                    if (posX >= player.posX - (player.width / anglesCount) * (anglesCount + 1 - i) && posX <= player.posX - (player.width / anglesCount) * (anglesCount - i))
                        dirX = -(anglesCount - i) / 10f
                    else if (posX <= player.posX + (player.width / anglesCount) * (anglesCount + 1 - i) && posX >= player.posX + (player.width / anglesCount) * (anglesCount - i)) {
                        dirX = (anglesCount - i) / 10f
                    }
                }
                if (speed < ballMaxSpeed) {
                    speed += if (abs(player.posX - player.posXOld) > GameSettings.screenWidth / 54)
                        5f
                    else
                        0.1f

                    if (speed < ballMaxSpeed)
                        speed = ballMaxSpeed
                }
            }
        }
        GameSounds.playSound()
        changeColor()
        dirY = -sqrt(1 - dirX * dirX)
    }

    private fun bounceCPU(cpuX: Float, pWidth: Float) {
        when {
            posX < cpuX - pWidth -> {
                dirX = -0.9f
            }
            posX > cpuX + pWidth -> {
                dirX = 0.9f
            }
            else -> {
                for (i in 1 until anglesCount) {
                    if (posX >= cpuX - (pWidth / anglesCount) * (anglesCount + 1 - i) && posX <= cpuX - (pWidth / anglesCount) * (anglesCount - i))
                        dirX = -(anglesCount - i) / 10f
                    else if (posX <= cpuX + (pWidth / anglesCount) * (anglesCount + 1 - i) && posX >= cpuX + (pWidth / anglesCount) * (anglesCount - i)) {
                        dirX = (anglesCount - i) / 10f
                    }
                }
            }
        }
        GameSounds.playSound()
        changeColor()
        dirY = sqrt(1 - dirX * dirX)

        if (speed < ballMaxSpeed) {
            speed += 0.1f
        }
    }

    private fun resetBall(isCpu: Boolean) {
        if (isCpu) {
            PaddlePong.playerScore += 1
            if (PaddlePong.playerScore > PaddlePong.absoluteScore) {
                PaddlePong.absoluteScore = PaddlePong.playerScore
            }
        } else {
            PaddlePong.cpuScore += 1
            if (PaddlePong.cpuScore > PaddlePong.absoluteScore) {
                PaddlePong.absoluteScore = PaddlePong.cpuScore
            }
        }
        if (PaddlePong.absoluteScore > 12) {
            PaddlePong.cpuScore = 0
            PaddlePong.playerScore = 0
            PaddlePong.absoluteScore = 0
        }

        start = false
        speed = startSpeed
    }

    fun centerBall() {
        posX = GameSettings.screenWidth / 2
        posY = GameSettings.screenHeight / 2
    }

    private fun changeColor() {
        changeColor = true
    }
}

//                if (Math.abs(pPosX - pOldX) > 5f) {
//                    // [old] --> [new]
//                    if (pPosX - pOldX > 0) {
//                        // o -->
//                        if (dirX >= 0)
//                            dirX += pPosX - pOldX
//                        // <-- o
//                        else
//                            dirX = dirX * -1
//                    }
//                    // [new] <-- [old]
//                    else if (pPosX - pOldX < 0)
//                    // <-- o
//                    if (dirX < 0)
//                        dirX -= pPosX - pOldX
//                    // o -->
//                    else
//                        dirX = dirX * -1
//                    if (dirX > 1.0f)
//                        dirX = 0.8f
//                    else if (dirX < -1.0f)
//                        dirX = -0.8f
//
//                    dirY = -sqrt(1 - dirX * dirX)
//                }
//            }