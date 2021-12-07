package com.example.pong_group.Model

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.media.MediaPlayer
import android.util.Log
import com.example.pong_group.Controller.App
import com.example.pong_group.R
import com.example.pong_group.Services.GameSounds

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

    //bSize: Float, bPosX: Float, bPosY: Float,
    fun update(ball: BallBreakout) {
        if (!broken) {
            if (ball.posX + ball.size >= posX
                && ball.posX - ball.size <= posX + width
                && ball.posY + ball.size >= posY
                && ball.posY - ball.size <= posY + height
            ) {
                dT = Math.abs(ball.posY + ball.size - posY)
                dB = Math.abs(ball.posY - ball.size - (posY + height))
                dR = Math.abs(ball.posX - ball.size - (posX + width))
                dL = Math.abs(ball.posX + ball.size - posX)
                d = minOf(dT, dB, dR, dL)
                Log.d("values", "all: $dT, $dB, $dR, $dL")
                Log.d("values", "min: $d")

                if(d == dT)
                    ball.dirY = Math.abs(ball.dirY) * -1
                else if (d == dB)
                    ball.dirY = Math.abs(ball.dirY)
                else if (d == dR)
                    ball.dirX = Math.abs(ball.dirX) * -1
                else
                    ball.dirX = Math.abs(ball.dirX)

//                if (Math.abs(ball.posX + ball.size - posX) <= 10f)
//                    ball.dirX = Math.abs(ball.dirX) * -1
//                if (Math.abs(ball.posX - ball.size - (posX + width)) <= 10f)
//                    ball.dirX = Math.abs(ball.dirX)
//                if (Math.abs(ball.posY + ball.size - posY) <= 10f)
//                    ball.dirY = Math.abs(ball.dirY) * -1
//                if (Math.abs(ball.posY - ball.size - (posY + height)) <= 10f)
//                    ball.dirY = Math.abs(ball.dirY)
                broken = true
                GameSounds.playSound()
            }
        }
    }

    fun draw(canvas: Canvas) {
        if (!broken) {
            canvas?.drawRect(
                posX,
                posY,
                posX + width,
                posY + height,
                paint
            )
        }
    }
}