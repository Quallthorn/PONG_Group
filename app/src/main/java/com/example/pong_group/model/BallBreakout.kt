package com.example.pong_group.model

import com.example.pong_group.controller.App
import com.example.pong_group.controller.prefs
import com.example.pong_group.model.GameViewBreakout.Companion.breakReady

import com.example.pong_group.model.GameViewBreakout.Companion.lives
import com.example.pong_group.model.GameViewBreakout.Companion.outOfLives
import com.example.pong_group.R
import com.example.pong_group.services.GameSettings
import com.example.pong_group.services.GameSettings.anglesCount
import com.example.pong_group.services.GameSettings.ballMaxSpeed
import com.example.pong_group.services.GameSounds.playSound
import com.example.pong_group.services.SharedBreakout
import com.example.pong_group.services.Sounds.*
import kotlin.math.sqrt

class BallBreakout : BasicBall() {

    init {
        speed = 15f * GameSettings.speedCoefficient
        if (prefs.isClassicInterface)
            speed = SharedBreakout.ballSpeedStart * GameSettings.speedCoefficient
        paint.color = App.instance.resources.getColor(R.color.white, App.instance.theme)
    }

    /**
     * updates ball position
     * if player has just lost a life ball is centered to players paddle
     * if player is out of lives ball position movement is stopped
     * called every frame
     *
     * @param player paddle for player
     */
    fun update(player: PaddleBreakout) {
        if (letGo) {
            checkEdges(player)
            checkPaddle(player)
            move()

            if (lives == 0) {
                outOfLives = true
            }
            breakReady = true
        } else {
            centerBall(player.posX, player.posY)
        }
    }

    /**
     * sees when ball hits edges of screen
     * used by update() function
     *
     * @param player paddle for player of the breakout verity
     */
    private fun checkEdges(player: PaddleBreakout) {
        if (posX >= GameSettings.screenWidth - radius) {
            playSound(WALL)
            dirNegativeX()
        } else if (posX <= radius) {
            playSound(WALL)
            dirPositiveX()
        }
        if (posY >= GameSettings.screenHeight - radius) {
            centerBall(player.posX, player.posY)
            dirNegativeY()
            playSound(LIFE)
            lives -= 1
            letGo = false
        } else if (posY <= radius + GameViewBreakout.ballEdgeTop) {
            playSound(WALL)
            dirPositiveY()
            if (prefs.isClassicInterface && !SharedBreakout.upperWallHit) {
                player.halfSize()
                SharedBreakout.upperWallHit = true
            }
        }
    }

    /**
     * sees when ball hits paddle
     * used by update() function
     *
     * @param player paddle for player of the breakout verity
     */
    private fun checkPaddle(player: PaddleBreakout) {
        if (posY >= GameSettings.screenHeight - player.posY - radius
            && posY <= GameSettings.screenHeight - player.posY + speed
            && posX + radius >= player.posX - player.width
            && posX - radius <= player.posX + player.width
        ) {
            checkSpeed(player.posX, player.width)
        }
    }

    /**
     * updates direction and speed of the ball according to where the ball hit the paddle
     *
     * @param pPosX center of the paddles X pos
     * @param pWidth width of the paddle
     */
    private fun checkSpeed(pPosX: Float, pWidth: Float) {
        checkIfCorner(pPosX, pWidth)
        if (prefs.isClassicInterface)
            speed = SharedBreakout.ballSpeed
        dirY = -sqrt((1 - dirX * dirX))
        playSound(WALL)
        changeColor()
    }

    /**
     * if the ball hits one of the paddles corners it gains more speed and the horizontal direction is maximised
     *
     * @param pPosX center of the paddles X pos
     * @param pWidth width of the paddle
     */
    private fun checkIfCorner(pPosX: Float, pWidth: Float) {
        when {
            posX < pPosX - pWidth -> {
                dirX = -0.9f
                addSpeed(1f)
            }
            posX > pPosX + pWidth -> {
                dirX = 0.9f
                addSpeed(1f)
            }
            else -> {
                bounceDir(pPosX, pWidth)
                addSpeed(0.1f)
            }
        }
    }

    /**
     * sends ball off in a direction according to where ball hit paddle
     * edges = more horizontal movement
     * center = more vertical movement
     *
     * used by checkSpeed()
     *
     * @param pPosX center of the paddles X pos
     * @param pWidth width of the paddle
     */
    private fun bounceDir(pPosX: Float, pWidth: Float) {
        for (i in 1 until anglesCount) {
            if (posX >= pPosX - (pWidth / anglesCount) * (anglesCount + 1 - i) && posX <= pPosX - (pWidth / anglesCount) * (anglesCount - i))
                dirX = -(anglesCount - i) / 10f
            else if (posX <= pPosX + (pWidth / anglesCount) * (anglesCount + 1 - i) && posX >= pPosX + (pWidth / anglesCount) * (anglesCount - i)) {
                dirX = (anglesCount - i) / 10f
            }
        }
    }

    /**
     * adds speed to ball
     * will not add speed if balls max speed is reached
     * used by checkIfCorner() function when adding speed
     *
     * @param v speed to be added
     */
    private fun addSpeed(v: Float) {
        if (speed < ballMaxSpeed && !prefs.isClassicInterface) {
            speed += v
        }
    }

    /**
     * moves ball a direction according to dirX and dirY
     * the ball slows down and goes the reverse direction if it overshoots a brick
     * until it hits the brick it overshot
     */
    private fun move() {
        posX += speed * dirX
        posY += speed * dirY
    }

    /**
     * moves ball to players paddle
     * used when a life is lost
     *
     * @param pPosX the X position for the player
     * @param pPosY the Y position for the player
     */
    fun centerBall(pPosX: Float, pPosY: Float) {
        dirNegativeY()
        posX = pPosX
        posY = GameSettings.screenHeight - (pPosY + radius)
    }
}