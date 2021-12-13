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
    private var waitOneFrame = false

    private var inXColumn = false
    private var inYRow = false
    private var xFirst = false

    init {
        posX = x
        posY = y
        width = w
        height = h
    }

    //bSize: Float, bPosX: Float, bPosY: Float,
    fun update(ball: BallBreakout) {
        if (!broken) {
            if (ball.posX + ball.radius >= posX && ball.posX - ball.radius <= posX + width){
                if (sqrt((ball.posX-posX).pow(2) + (ball.posY-posY).pow(2)) <= ball.radius
                    || sqrt((ball.posX-posX - width).pow(2) + (ball.posY-posY).pow(2)) <= ball.radius
                    || ball.posX >= posX && ball.posX <= posX + width){
                    xFirst = !inYRow
                    inXColumn = true
                }
            }
            else
                inXColumn = false

            if (ball.posY + ball.radius >= posY && ball.posY - ball.radius <= posY + height){
                if (sqrt((ball.posY-posY).pow(2) + (ball.posX-posX).pow(2)) <= ball.radius
                    || sqrt((ball.posY-posY - height).pow(2) + (ball.posX-posX).pow(2)) <= ball.radius
                    || ball.posY >= posY && ball.posY <= posY + height){
                    xFirst = inXColumn
                    inYRow = true
                }
            }
            else
                inYRow = false

            if (inXColumn && inYRow){
                if (xFirst)
                    ball.dirY *= -1
                else
                    ball.dirX *= -1
                GameSounds.playSound()
                broken = true
            }
//            if (ball.posX + ball.radius + ball.speed * abs(ball.dirX) >= posX
//                && ball.posX - ball.radius - ball.speed * abs(ball.dirX) <= posX + width
//                && ball.posY + ball.radius + ball.speed * abs(ball.dirY) >= posY
//                && ball.posY - ball.radius - ball.speed * abs(ball.dirY) <= posY + height
//                && !waitOneFrame
//            ) {
//                waitOneFrame = true
//            }
//            if (ball.posX + ball.radius >= posX
//                && ball.posX - ball.radius <= posX + width
//                && ball.posY + ball.radius >= posY
//                && ball.posY - ball.radius <= posY + height
//                && waitOneFrame
//            ) {
//                breakBrick(ball)
//            }
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
        Log.d("values", "all: $dT, $dB, $dR, $dL")
        Log.d("values", "min: $d")

        when (d) {
            dT -> ball.dirY = abs(ball.dirY) * -1
            dB -> ball.dirY = abs(ball.dirY)
            dR -> ball.dirX = abs(ball.dirX)
            else -> ball.dirX = abs(ball.dirX) * -1
        }
        GameSounds.playSound()
        broken = true
    }
}