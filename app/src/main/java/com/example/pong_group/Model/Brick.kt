package com.example.pong_group.Model

import android.graphics.Paint
import android.util.Log
import com.example.pong_group.Model.GameViewBreakout.Companion.canvasBreakout
import com.example.pong_group.Services.GameSounds
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

class Brick(w: Float, h: Float, x: Float, y: Float) {
    var posX = 0f
    var posY = 0f
    var width = 0f
    var height = 0f
    var paint = Paint()

    var dT = 0f
    var dB = 0f
    var dR = 0f
    var dL = 0f
    var d = 0f

    var broken = false

    init {
        posX = x
        posY = y
        width = w
        height = h
    }
    fun update(ball: BallBreakout) {
        if (!broken) {
            if (ball.posX >= posX && ball.posX <= posX + width && ball.posY + ball.radius >= posY && ball.posY - ball.radius <= posY + height
                || ball.posX + ball.radius >= posX && ball.posX - ball.radius <= posX + width && ball.posY >= posY && ball.posY <= posY + height
                || sqrt((ball.posX - posX).pow(2) + (ball.posY - posY).pow(2)) <= ball.radius
                || sqrt((ball.posX - posX - width).pow(2) + (ball.posY - posY).pow(2)) <= ball.radius
                || sqrt((ball.posY - posY).pow(2) + (ball.posX - posX).pow(2)) <= ball.radius
                || sqrt((ball.posY - posY - height).pow(2) + (ball.posX - posX).pow(2)) <= ball.radius
            ) {
                breakBrick(ball)
            }
        }
    }

    fun draw() {
        if (!broken) {
            canvasBreakout?.drawRect(
                posX,
                posY,
                posX + width,
                posY + height,
                paint
            )
        }
    }

    private fun breakBrick(ball: BallBreakout) {
        dT = abs(ball.posY + ball.radius - posY)
        dB = abs(ball.posY - ball.radius - (posY + height))
        dR = abs(ball.posX - ball.radius - (posX + width))
        dL = abs(ball.posX + ball.radius - posX)

        d = minOf(dT, dB, dR, dL)
//        Log.d("values", "all: $dT, $dB, $dR, $dL")
//        Log.d("values", "min: $d")

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
        GameSounds.playSound()
        broken = true
    }
}