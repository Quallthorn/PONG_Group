package com.example.pong_group.Model

import android.graphics.Canvas
import android.graphics.Paint

object NumberPrinter {

    val numberW = 20f
    val numberWL = 90f
    val numberH = 180f

    fun drawOne(canvas: Canvas, startX: Float, startY: Float, paint: Paint){
        canvas?.drawRect(
            startX,
            startY,
            startX + numberW,
            startY + numberH,
            paint
        )
    }

    fun drawTwo(canvas: Canvas, startX: Float, startY: Float, paint: Paint){
        var posX = startX
        var posY = startY
        val segment = (numberH)/7
        for (i in 1..7){
            if (i == 1 || i == 4 || i == 7)
                canvas?.drawRect(
                    posX,
                    posY,
                    posX + numberWL,
                    posY + segment,
                    paint
                )
            else if (i == 2 || i == 3) {
                canvas?.drawRect(
                    posX + numberWL - numberW,
                    posY,
                    posX + numberWL,
                    posY + segment,
                    paint
                )
            }
            else {
                canvas?.drawRect(
                    posX,
                    posY,
                    posX + numberW,
                    posY + segment,
                    paint
                )
            }
            posY += segment
        }
    }

    fun drawThree(canvas: Canvas, startX: Float, startY: Float, paint: Paint){
        var posX = startX
        var posY = startY
        val segment = (numberH)/7
        for (i in 1..7){
            if (i == 1 || i == 4 || i == 7)
                canvas?.drawRect(
                    posX,
                    posY,
                    posX + numberWL,
                    posY + segment,
                    paint
                )
            else {
                canvas?.drawRect(
                    posX + numberWL - numberW,
                    posY,
                    posX + numberWL,
                    posY + segment,
                    paint
                )
            }
            posY += segment
        }
    }
}