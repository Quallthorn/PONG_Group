package com.example.pong_group.Model

import com.example.pong_group.Controller.App
import com.example.pong_group.Controller.prefs
import com.example.pong_group.Model.GameViewBreakout.Companion.breakReady

import com.example.pong_group.Model.GameViewBreakout.Companion.lives
import com.example.pong_group.Model.GameViewBreakout.Companion.outOfLives
import com.example.pong_group.R
import com.example.pong_group.Services.GameSettings
import com.example.pong_group.Services.GameSettings.anglesCount
import com.example.pong_group.Services.GameSettings.ballMaxSpeed
import com.example.pong_group.Services.GameSounds.playSoundWall
import com.example.pong_group.Services.SharedBreakout
import kotlin.math.sqrt

class BallBreakout : BasicBall() {
    var checkCollision = false

    init {
        speed = 15f * GameSettings.speedCoefficient
        if (prefs.isClassicInterface)
            speed = SharedBreakout.ballSpeedStart * GameSettings.speedCoefficient
        paint.color = App.instance.resources.getColor(R.color.white, App.instance.theme)
    }

    fun update(player: PaddleBreakout) {
        if (letGo) {
            checkEdges(player)
            checkPaddle(player)
            move()

            if (lives == 0) {
                outOfLives = true
            }
            breakReady = true
        }
        else{
            centerBall(player.posX, player.posY)
        }
    }

    private fun checkEdges(player: PaddleBreakout){
        if (posX >= GameSettings.screenWidth - radius) {
            playSoundWall()
            dirNegativeX()
        } else if (posX <= radius) {
            playSoundWall()
            dirPositiveX()
        }
        if (posY >= GameSettings.screenHeight - radius) {
            centerBall(player.posX, player.posY)
            dirNegativeY()
            lives -= 1
            letGo = false
        } else if (posY <= radius + GameViewBreakout.ballEdgeTop) {
            playSoundWall()
            dirPositiveY()
            if (prefs.isClassicInterface && !SharedBreakout.upperWallHit) {
                player.halfSize()
                SharedBreakout.upperWallHit = true
            }
        }
    }

    private fun checkPaddle(player:PaddleBreakout){
        if (posY >= GameSettings.screenHeight - player.posY - radius
            && posY <= GameSettings.screenHeight - player.posY + speed
            && posX + radius >= player.posX - player.width
            && posX - radius <= player.posX + player.width
        ) {
            checkSpeed(player.posX, player.width)
        }
    }

    private fun checkSpeed(pPosX: Float, pWidth: Float) {
        //if the ball hits one of the paddles corners it gains more speed ant the direction is more horizontal
        //otherwise it checks where it hit the paddle and moves in a direction according to how far away from the middle the ball hit
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
        if (prefs.isClassicInterface)
            speed = SharedBreakout.ballSpeed
        dirY = -sqrt((1 - dirX * dirX))
        playSoundWall()
        changeColor()
    }

    private fun addSpeed(v: Float){
        if (speed < ballMaxSpeed && !prefs.isClassicInterface) {
            speed += v
        }
    }

    private fun bounceDir(pPosX: Float, pWidth: Float) {
        for (i in 1 until anglesCount) {
            if (posX >= pPosX - (pWidth / anglesCount) * (anglesCount + 1 - i) && posX <= pPosX - (pWidth / anglesCount) * (anglesCount - i))
                dirX = -(anglesCount - i) / 10f
            else if (posX <= pPosX + (pWidth / anglesCount) * (anglesCount + 1 - i) && posX >= pPosX + (pWidth / anglesCount) * (anglesCount - i)) {
                dirX = (anglesCount - i) / 10f
            }
        }
    }

    private fun move(){
        //goes "forward" if everything is normal
        if (!checkCollision) {
            posX += speed * dirX
            posY += speed * dirY
        } else // goes "backwards" if ball overshoots
        {
            posX -= speed * dirX * 0.25f
            posY -= speed * dirY * 0.25f
        }
    }

    fun centerBall(pPosX: Float, pPosY: Float) {
        dirNegativeY()
        posX = pPosX
        posY = GameSettings.screenHeight - (pPosY + radius)
    }
}