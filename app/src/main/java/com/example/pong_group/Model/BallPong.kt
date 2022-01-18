package com.example.pong_group.Model

import com.example.pong_group.Controller.prefs
import com.example.pong_group.Services.GameSettings
import com.example.pong_group.Services.GameSettings.anglesCount
import com.example.pong_group.Services.GameSettings.ballMaxSpeed
import com.example.pong_group.Services.GameSettings.gameOver
import com.example.pong_group.Services.GameSounds
import com.example.pong_group.Services.GameSounds.playWall
import kotlin.math.abs
import kotlin.math.sqrt

class BallPong : BasicBall() {

    private val startSpeed = 10f * GameSettings.speedCoefficient
    var p1Scored = false
    var playersReady = false

    /**
     * updates ball position
     * bounces of walls or players paddle
     * if ball reaches top or bottom edge a point is scored to opposing player
     * and ball is reset to middle of screen and stops until player who did not score a point is ready
     *
     * called every frame
     *
     * @param player paddle for player 1
     * @param cpuP2 paddle for cpu / player 2
     */
    fun update(
        player: PaddlePong,
        cpuP2: PaddlePong
    ) {
        if (letGo && !gameOver && playersReady) {
            checkWalls()
            checkPoint(player)
            checkPaddle(player, cpuP2)
            move()

        } else if (playersReady) {
            posX = if (p1Scored)
                cpuP2.posX
            else {
                player.posX
            }
        }
    }

    /**
     * checks if the ball has hit right or left edge
     *
     * called by update()
     */
    private fun checkWalls(){
        if (posX >= GameSettings.screenWidth - radius) {
            playWall()
            posX = GameSettings.screenWidth - radius - 1f
            if (dirY > 0) {
                addRandomDirYPositive()
            } else {
                addRandomDirYNegative()
            }
            dirX = -sqrt(1 - dirY * dirY)
        } else if (posX <= radius) {
            posX = radius + 1f
            playWall()
            if (dirY > 0) {
                addRandomDirYPositive()
            } else {
                addRandomDirYNegative()
            }
            dirX = sqrt(1 - dirY * dirY)
        }
    }

    /**
     * adds a random vertical direction when hitting wall
     *
     * called by checkWalls()
     */
    private fun addRandomDirYPositive(){
        dirY += ((0..2).random()) / 10f
        if (dirY > 1 || dirY < 0)
            dirY = 0.5f
    }

    /**
     * subtracts a random vertical direction when hitting wall
     *
     * called by checkWalls()
     */
    private fun addRandomDirYNegative(){
        dirY -= ((0..2).random()) / 10f
        if (dirY < -1 || dirY > 0)
            dirY = -0.5f
    }

    /**
     * checks if ball hits upper or lower edge
     *
     * called by update()
     *
     * @param player paddle for player 1 (player 2 position can be figured out by player 1 values)
     */
    private fun checkPoint(player: PaddlePong){
        if (posY >= GameSettings.screenHeight - radius) {
            dirY = -1f
            dirX = 0f
            p1Scored = false
            posY = GameSettings.screenHeight - (player.posY + radius)
            resetBall(false)
            GameSounds.playBrick()
        } else if (posY <= radius) {
            dirY = 1f
            dirX = 0f
            p1Scored = true
            posY = player.posY + player.height + radius
            resetBall(true)
            GameSounds.playBrick()
        }
    }

    /**
     * checks if ball hits a player
     *
     * called by update()
     *
     * @param player paddle for player 1
     * @param cpuP2 paddle for cpu / player 2
     */
    private fun checkPaddle(player: PaddlePong, cpuP2: PaddlePong){
        if (posY + radius >= GameSettings.screenHeight - player.posY
            && posY + radius <= GameSettings.screenHeight - player.posY + player.height + speed
            && posX + radius >= player.posX - player.width
            && posX - radius <= player.posX + player.width
        ) {
            posY = GameSettings.screenHeight - player.posY - radius - 1f
            bouncePlayer(player)
        }

        if (posY - radius <= player.posY + player.height
            && posY - radius >= player.posY - speed
            && posX >= cpuP2.posX - player.width
            && posX <= cpuP2.posX + player.width
        ) {
            posY = player.posY + player.height + radius + 1f
            if (prefs.isP2Human)
                bouncePlayer(cpuP2)
            else
                bounceCPU(cpuP2.posX, cpuP2.width)
        }
    }

    /**
     * moves ball a direction according to dirX and dirY
     *
     * called by update()
     */
    private fun move() {
        posX += speed * dirX
        posY += speed * dirY
    }

    /**
     * sends ball off in a direction according to where ball hit paddle
     * edges = more horizontal movement
     * center = more vertical movement
     *
     * adds extra speed if hits corner and paddle moves fast
     *
     * called by checkPaddle()
     *
     * @param player paddle for player (1 or 2)
     */
    private fun bouncePlayer(player: PaddlePong) {
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

                    if (speed > ballMaxSpeed)
                        speed = ballMaxSpeed
                }
            }
        }
        playWall()
        changeColor()
        dirY = -sqrt(1 - dirX * dirX)
    }


    /**
     * sends ball off in a direction according to where ball hit paddle
     * edges = more horizontal movement
     * center = more vertical movement
     * unlike bouncePlayer(), ball does not gain speed if it hits the paddles corner
     * because cpu is always moving
     *
     * called by checkPaddle()
     *
     * @param cpuX horizontal position of cpu
     * @param cpuW width of cpu
     */
    private fun bounceCPU(cpuX: Float, cpuW: Float) {
        when {
            posX < cpuX - cpuW -> {
                dirX = -0.9f
            }
            posX > cpuX + cpuW -> {
                dirX = 0.9f
            }
            else -> {
                for (i in 1 until anglesCount) {
                    if (posX >= cpuX - (cpuW / anglesCount) * (anglesCount + 1 - i) && posX <= cpuX - (cpuW / anglesCount) * (anglesCount - i))
                        dirX = -(anglesCount - i) / 10f
                    else if (posX <= cpuX + (cpuW / anglesCount) * (anglesCount + 1 - i) && posX >= cpuX + (cpuW / anglesCount) * (anglesCount - i)) {
                        dirX = (anglesCount - i) / 10f
                    }
                }
            }
        }
        playWall()
        changeColor()
        dirY = sqrt(1 - dirX * dirX)

        if (speed < ballMaxSpeed) {
            speed += 0.1f
        }
    }

    /**
     * adds score to player or cpu depending on who scored
     *
     * called by checkPoint()
     *
     * @param isCpu did player 1 or cpu / player 2 score?
     */
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
        if (PaddlePong.absoluteScore >= prefs.firstToPongPrefs) {
            gameOver = true
        }

        letGo = false
        speed = startSpeed
    }

    /**
     * centers ball to middle of court
     *
     * called by GameViewPONG setUp()
     */
    fun centerBall() {
        posX = GameSettings.screenWidth / 2
        posY = GameSettings.screenHeight / 2
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