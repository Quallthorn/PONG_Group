package com.example.pong_group.Services

import android.graphics.Canvas
import android.graphics.Paint

object NumberPrinter {

    var scoreCPU = 0
    var scoreP = 0

    val numberW = 20f
    val numberWL = 90f
    val numberH = 180f

    //Numbers
    fun drawOne(canvas: Canvas, startX: Float, startY: Float, paint: Paint) {
        lineVerticalLong(canvas,startX,startY,paint)
    }

    fun drawTwo(canvas: Canvas, startX: Float, startY: Float, paint: Paint) {
        for (i in 0..2)
            lineHorizontal(canvas,startX,startY+ numberH /7*i*3,paint)
        lineVerticalHalf(canvas,startX+ numberWL - numberW,startY,paint)
        lineVerticalHalf(canvas,startX,startY+ numberH /7*3,paint)
    }

    fun drawThree(canvas: Canvas, startX: Float, startY: Float, paint: Paint) {
        for (i in 0..2)
            lineHorizontal(canvas,startX,startY+ numberH /7*i*3,paint)
        lineVerticalLong(canvas,startX+ numberWL - numberW,startY,paint)
    }

    fun drawFour(canvas: Canvas, startX: Float, startY: Float, paint: Paint) {
        lineVerticalHalf(canvas,startX,startY,paint)
        lineVerticalLong(canvas,startX+ numberWL - numberW,startY,paint)
        lineHorizontal(canvas,startX,startY+ numberH /7*3,paint)
    }

    fun drawFive(canvas: Canvas, startX: Float, startY: Float, paint: Paint) {
        for (i in 0..2)
            lineHorizontal(canvas,startX,startY+ numberH /7*i*3,paint)
        lineVerticalHalf(canvas,startX+ numberWL - numberW,startY+ numberH /7*3,paint)
        lineVerticalHalf(canvas,startX,startY,paint)
    }

    fun drawSix(canvas: Canvas, startX: Float, startY: Float, paint: Paint) {
        for (i in 1..2)
            lineHorizontal(canvas,startX,startY+ numberH /7*i*3,paint)
        lineVerticalHalf(canvas,startX,startY+ numberH /7*3,paint)
        lineVerticalLong(canvas,startX+ numberWL - numberW,startY,paint)
    }

    fun drawSeven(canvas: Canvas, startX: Float, startY: Float, paint: Paint) {
        lineHorizontal(canvas,startX,startY,paint)
        lineVerticalLong(canvas,startX+ numberWL - numberW,startY,paint)
    }

    fun drawEight(canvas: Canvas, startX: Float, startY: Float, paint: Paint) {
        for (i in 0..2)
            lineHorizontal(canvas,startX,startY+ numberH /7*i*3,paint)
        for (i in 0..1)
            lineVerticalLong(canvas,startX+(numberWL - numberW)*i,startY,paint)
    }

    fun drawNine(canvas: Canvas, startX: Float, startY: Float, paint: Paint) {
        for (i in 0..1)
            lineHorizontal(canvas,startX,startY+ numberH /7*i*3,paint)
        lineVerticalHalf(canvas,startX,startY,paint)
        lineVerticalLong(canvas,startX+ numberWL - numberW,startY,paint)
    }

    fun drawZero(canvas: Canvas, startX: Float, startY: Float, paint: Paint) {
        for (i in 0..1)
            lineHorizontal(canvas,startX,startY+ numberH /7*i*6,paint)
        for (i in 0..1)
            lineVerticalLong(canvas,startX+(numberWL - numberW)*i,startY,paint)
    }


    //Lines
    fun lineVerticalLong(canvas: Canvas, posX: Float, posY: Float, paint: Paint){
        canvas?.drawRect(
            posX,
            posY,
            posX + numberW,
            posY + numberH,
            paint
        )
    }

    fun lineVerticalHalf(canvas: Canvas, posX: Float, posY: Float, paint: Paint){
        canvas?.drawRect(
            posX,
            posY,
            posX + numberW,
            posY + numberH /7*4,
            paint
        )
    }

    fun lineHorizontal(canvas: Canvas, posX: Float, posY: Float, paint: Paint){
        canvas?.drawRect(
            posX,
            posY,
            posX + numberWL,
            posY + numberH /7,
            paint
        )
    }
}