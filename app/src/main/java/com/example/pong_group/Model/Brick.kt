package com.example.pong_group.Model

import android.graphics.Color
import android.graphics.Paint
import com.example.pong_group.Model.GameViewBreakout.Companion.breakReady
//import com.example.pong_group.Model.GameViewBreakout.Companion.canvasBreakout
import com.example.pong_group.Services.GameSettings
import com.example.pong_group.Services.GameSettings.curCanvas
import com.example.pong_group.Services.GameSounds
import com.example.pong_group.Services.SharedBreakout
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

class Brick(w: Float, h: Float, x: Float, y: Float, s: Int, n: Int) {
    private val classic = GameSettings.classicBreakout
    private var posX = 0f
    private var posY = 0f
    private var width = 0f
    private var height = 0f
    private var pointBase = 0
    private var nr = 0
    var paint = Paint()

    //d -> delta/difference
    private var dT = 0f
    private var dB = 0f
    private var dR = 0f
    private var dL = 0f
    private var d = 0f

    //ex -> exposed
    var exT = false
    var exB = false
    var exR = false
    var exL = false

    private var broken = false
    private var breakable = false

    init {
        posX = x
        posY = y
        width = w
        height = h
        pointBase = s
        nr = n
    }

    fun update(ball: BallBreakout) {
        if (!broken && breakable) {
            if (ball.posX >= posX && ball.posX <= posX + width && ball.posY + ball.radius >= posY && ball.posY + ball.radius <= posY + height //ball bottom edge
                || ball.posX >= posX && ball.posX <= posX + width && ball.posY - ball.radius >= posY && ball.posY - ball.radius <= posY + height //ball top edge
                || ball.posX + ball.radius >= posX && ball.posX + ball.radius <= posX + width && ball.posY >= posY && ball.posY <= posY + height //ball right edge
                || ball.posX - ball.radius >= posX && ball.posX - ball.radius <= posX + width && ball.posY >= posY && ball.posY <= posY + height //ball left edge
                || sqrt((ball.posX - posX).pow(2) + (ball.posY - posY).pow(2)) <= ball.radius
                || sqrt((ball.posX - posX - width).pow(2) + (ball.posY - posY).pow(2)) <= ball.radius
                || sqrt((ball.posY - posY).pow(2) + (ball.posX - posX).pow(2)) <= ball.radius
                || sqrt((ball.posY - posY - height).pow(2) + (ball.posX - posX).pow(2)) <= ball.radius
            ) {
                //make sure if the ball slips past two bricks it goes back to one of the two bricks it was supposed to hit
                //
                // .    = ball (direction is up-right)
                // [X]  = brick it hits but is not supposed to (left or top side is exposed so brick is breakable)
                // [_]  = brick it can hit
                //
                // configuration:
                // [X][_]
                // [_] .
                if (ball.dirX > 0 && ball.dirY > 0 && !exL && !exT
                    || ball.dirX > 0 && ball.dirY < 0 && !exL && !exB
                    || ball.dirX < 0 && ball.dirY < 0 && !exR && !exB
                    || ball.dirX < 0 && ball.dirY > 0 && !exR && !exT) {
                    ball.checkCollision = true
                }
                else if (breakReady)
                    ballCollide(ball)
            }
        }

        //make sure if the ball overshoots it goes back until it hits an eligible brick
        if (!broken && !breakable){
            if (ball.posX >= posX && ball.posX <= posX + width && ball.posY + ball.radius >= posY && ball.posY + ball.radius <= posY + height //ball bottom edge
                || ball.posX >= posX && ball.posX <= posX + width && ball.posY - ball.radius >= posY && ball.posY - ball.radius <= posY + height //ball top edge
                || ball.posX + ball.radius >= posX && ball.posX + ball.radius <= posX + width && ball.posY >= posY && ball.posY <= posY + height //ball right edge
                || ball.posX - ball.radius >= posX && ball.posX - ball.radius <= posX + width && ball.posY >= posY && ball.posY <= posY + height //ball left edge
            ) {
                ball.checkCollision = true
            }
        }

        //makes the brick breakable if any of the edges are exposed
        if (!breakable) {
            if (exT || exB || exR || exL)
                canBreak()
        }
    }

    fun draw() {
        if (!broken) {
            curCanvas.drawRect(
                posX,
                posY,
                posX + width,
                posY + height,
                paint
            )
        }
    }


    private fun ballCollide(ball: BallBreakout) {
        ball.checkCollision = false
        breakReady = false
        dT = abs(ball.posY - posY)
        dB = abs(ball.posY - (posY + height))
        dR = abs(ball.posX - (posX + width))
        dL = abs(ball.posX - posX)

        //d = minOf(dT, dB, dR, dL)

        //checks delta according to exposed side of brick and direction of ball
        //left & up
        d = if (ball.dirX < 0 && ball.dirY < 0) {
            if (exB && exR)
                minOf(dB, dR)
            else if (exB && !exR)
                dB
            else
                dR
        //right & up
        } else if (ball.dirX > 0 && ball.dirY < 0) {
            if (exB && exL)
                minOf(dB, dL)
            else if (exB && !exL)
                dB
            else
                dL
        //left & down
        } else if (ball.dirX < 0 && ball.dirY > 0) {
            if (exT && exR)
                minOf(dT, dR)
            else if (exT && !exR)
                dT
            else
                dR
        //right & down (whatever is not already checked)
        } else {
            if (exT && exL)
                minOf(dT, dL)
            else if (exT && !exL)
                dT
            else
                dL
        }

        //sends ball of in direction according to where it hit the brick
        //sets ball to edge of brick in case it is inside the brick
        when (d) {
            dT -> {
                ball.posY = posY - ball.radius
                ball.dirY = abs(ball.dirY) * -1
            }
            dB -> {
                ball.posY = posY + height + ball.radius
                ball.dirY = abs(ball.dirY)
            }
            dR -> {
                ball.posX = posX + width + ball.radius
                ball.dirX = abs(ball.dirX)
            }
            else -> {
                ball.posX = posX - ball.radius
                ball.dirX = abs(ball.dirX) * -1
            }
        }
        SharedBreakout.checkSurroundings(nr)
        breakBrick()
    }

    private fun breakBrick() {
        GameSounds.playSound()
        broken = true
        GameViewBreakout.totalCountOfBricks -= 1
        if (!classic) {
            SharedBreakout.addScore(pointBase)
        } else {
            SharedBreakout.addScoreClassic(pointBase)
            if (!SharedBreakout.maxSpeedAchieved)
                SharedBreakout.updateSpeedClassic(pointBase)
        }
    }


    private fun canBreak() {
        breakable = true
        //paint.color = Color.WHITE
    }
}