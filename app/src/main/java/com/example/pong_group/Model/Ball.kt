package com.example.pong_group.Model

import android.graphics.Paint
import com.example.pong_group.Model.GameViewPONG.Companion.canvas
import com.example.pong_group.Services.GameSettings
import com.example.pong_group.Services.GameSounds

class Ball() {
    var radius = 10f
    var posX = radius
    var posY = radius
    var dirX = 0.5f
    var dirY = 0.5f
    var paint = Paint()

    private val anglesCount = 10 // max 10 possibly 10.9 but not recommended
    private val startSpeed = 10f
    var speed = 10f
    private val maxSpeed = 30f

    var changeColor = false
    var start = false
    var p1Scored = false

    init {
        posX = GameSettings.screenWidth * 0.5f
        posY = GameSettings.screenHeight * 0.5f
    }

    fun update(
        pPosX: Float,
        pPosY: Float,
        pWidth: Float,
        pOldX: Float,
        cpuX: Float
    ) {
        if (start){
            //right side
            if (posX >= GameSettings.screenWidth - radius) {
                GameSounds.playSound()
                if (dirY > 0){
                    dirY += ((0..2).random())/10f
                    if (dirY > 1 || dirY < 0)
                        dirY = 0.5f
                }
                else{
                    dirY -= ((0..2).random())/10f
                    if (dirY < -1 || dirY > 0)
                        dirY = -0.5f
                }
                dirX = -Math.sqrt((1 - dirY * dirY).toDouble()).toFloat()

            //left side
            } else if (posX <= radius) {
                GameSounds.playSound()
                if (dirY > 0){
                    dirY += ((0..2).random())/10f
                    if (dirY > 1 || dirY < 0)
                        dirY = 0.5f
                }
                else{
                    dirY -= ((0..2).random())/10f
                    if (dirY < -1 || dirY > 0)
                        dirY = -0.5f
                }
                dirX = Math.sqrt((1 - dirY * dirY).toDouble()).toFloat()
            }


            //player side
            if (posY >= GameSettings.screenHeight - radius) {
                dirY = -1f
                dirX = 0f
                p1Scored = true
                posY = GameSettings.screenHeight - (pPosY + radius)
                resetBall(false)
            //cpu side
            } else if (posY <= radius) {
                dirY = 1f
                dirX = 0f
                p1Scored = false
                posY = pPosY + radius
                resetBall(true)
            }


            //player paddle
            if (posY >= GameSettings.screenHeight - pPosY - radius
                && posY <= GameSettings.screenHeight - pPosY + speed
                && posX + radius >= pPosX - pWidth
                && posX - radius <= pPosX + pWidth
            ) {
                bounceP1(pPosX, pWidth, pOldX)
            }

            //cpu paddle
            if (posY <= pPosY + radius
                && posY >= pPosY - speed
                && posX >= cpuX - pWidth
                && posX <= cpuX + pWidth
            ) {
                bounceCPU(cpuX, pWidth)
            }

            posX += speed * dirX
            posY += speed * dirY
        }
        else{
            posX = if (p1Scored)
                pPosX
            else{
                cpuX
            }
        }
    }

    fun draw() {
        canvas.drawCircle(posX, posY, radius, paint)
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
            if (Math.abs(pPosX - pOldX) > GameSettings.screenWidth / 54)
                speed += 5f
            else
                speed += 0.1f

            if (speed < maxSpeed)
                speed == maxSpeed
        }
    }

    fun bounceCPU(CPUX: Float, pWidth: Float) {
        for (i in 1 until anglesCount) {
            if (posX >= CPUX - (pWidth / anglesCount) * (anglesCount + 1 - i) && posX <= CPUX - (pWidth / anglesCount) * (anglesCount - i))
                dirX = -(anglesCount - i) / 10f
            else if (posX <= CPUX + (pWidth / anglesCount) * (anglesCount + 1 - i) && posX >= CPUX + (pWidth / anglesCount) * (anglesCount - i)) {
                dirX = (anglesCount - i) / 10f
            }
        }
        GameSounds.playSound()
        changeColor()
        dirY = Math.sqrt((1 - dirX * dirX).toDouble()).toFloat()

        if (speed < maxSpeed) {
            speed += 0.1f
        }
    }

    fun resetBall(isCpu: Boolean) {
            if (isCpu) {
                Paddle.cpuScore += 1
                if (Paddle.cpuScore > Paddle.absoluteScore) {
                    Paddle.absoluteScore = Paddle.cpuScore
                }
            } else {
                Paddle.playerScore += 1
                if (Paddle.playerScore > Paddle.absoluteScore) {
                    Paddle.absoluteScore = Paddle.playerScore
                }
            }
        if (Paddle.absoluteScore > 50) {
            Paddle.playerScore = 0
            Paddle.cpuScore = 0
            Paddle.absoluteScore = 0
        }

        start = false
        speed = startSpeed
    }

    fun centerBall(){
        posX = GameSettings.screenWidth/2
        posY = GameSettings.screenHeight/2
    }

    fun changeColor() {
        changeColor = true
    }
}

//            if (posY >= screenHeight - pPosY - size
//                && posY <= screenHeight - pPosY + pHeight
//                && posX >= pPosX - pWidth
//                && posX <= pPosX + pWidth
//            ) {
//                dirY = dirY * -1

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
//                    dirY = -Math.sqrt((1 - dirX * dirX).toDouble()).toFloat()
//                }
//            }