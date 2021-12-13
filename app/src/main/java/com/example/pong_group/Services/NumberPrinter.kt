package com.example.pong_group.Services

import com.example.pong_group.Model.GameViewPONG.Companion.canvas

object NumberPrinter {

    val numberW = 20f
    val numberWL = 90f
    val numberH = 180f
    var paint = GameSettings.curPaint

    fun drawNumber(number: Int, startX: Float, startY: Float, startX2: Float) {
        if (number <= 9)
            draw(number, startX, startY)
        else {
            val first: Int = number / 10
            val second: Int = number % 10
            draw(first, startX, startY)
            draw(second, startX2, startY)
        }

    }

    fun draw(digit: Int, startX: Float, startY: Float){
        when (digit) {
            0 -> drawZero(startX, startY)
            1 -> drawOne(startX, startY)
            2 -> drawTwo(startX, startY)
            3 -> drawThree(startX, startY)
            4 -> drawFour(startX, startY)
            5 -> drawFive(startX, startY)
            6 -> drawSix(startX, startY)
            7 -> drawSeven(startX, startY)
            8 -> drawEight(startX, startY)
            9 -> drawNine(startX, startY)
            else -> drawZero(startX, startY)
        }
    }

    //Numbers
    fun drawZero(startX: Float, startY: Float) {
        for (i in 0..1)
            lineHorizontal(startX, startY + numberH / 7 * i * 6)
        for (i in 0..1)
            lineVerticalLong(startX + (numberWL - numberW) * i, startY)
    }

    fun drawOne(startX: Float, startY: Float) {
        lineVerticalLong(startX + numberWL - numberW, startY)
    }

    fun drawTwo(startX: Float, startY: Float) {
        for (i in 0..2)
            lineHorizontal(startX, startY + numberH / 7 * i * 3)
        lineVerticalHalf(startX + numberWL - numberW, startY)
        lineVerticalHalf(startX, startY + numberH / 7 * 3)
    }

    fun drawThree(startX: Float, startY: Float) {
        for (i in 0..2)
            lineHorizontal(startX, startY + numberH / 7 * i * 3)
        lineVerticalLong(startX + numberWL - numberW, startY)
    }

    fun drawFour(startX: Float, startY: Float) {
        lineVerticalHalf(startX, startY)
        lineVerticalLong(startX + numberWL - numberW, startY)
        lineHorizontal(startX, startY + numberH / 7 * 3)
    }

    fun drawFive(startX: Float, startY: Float) {
        for (i in 0..2)
            lineHorizontal(startX, startY + numberH / 7 * i * 3)
        lineVerticalHalf(startX + numberWL - numberW, startY + numberH / 7 * 3)
        lineVerticalHalf(startX, startY)
    }

    fun drawSix(startX: Float, startY: Float) {
        for (i in 1..2)
            lineHorizontal(startX, startY + numberH / 7 * i * 3)
        lineVerticalHalf(startX, startY + numberH / 7 * 3)
        lineVerticalLong(startX + numberWL - numberW, startY)
    }

    fun drawSeven(startX: Float, startY: Float) {
        lineHorizontal(startX, startY)
        lineVerticalLong(startX + numberWL - numberW, startY)
    }

    fun drawEight(startX: Float, startY: Float) {
        for (i in 0..2)
            lineHorizontal(startX, startY + numberH / 7 * i * 3)
        for (i in 0..1)
            lineVerticalLong(startX + (numberWL - numberW) * i, startY)
    }

    fun drawNine(startX: Float, startY: Float) {
        for (i in 0..1)
            lineHorizontal(startX, startY + numberH / 7 * i * 3)
        lineVerticalHalf(startX, startY)
        lineVerticalLong(startX + numberWL - numberW, startY)
    }


    //Lines
    fun lineVerticalLong(posX: Float, posY: Float) {
        canvas.drawRect(
            posX,
            posY,
            posX + numberW,
            posY + numberH,
            paint
        )
    }

    fun lineVerticalHalf(posX: Float, posY: Float) {
        canvas.drawRect(
            posX,
            posY,
            posX + numberW,
            posY + numberH / 7 * 4,
            paint
        )
    }

    fun lineHorizontal(posX: Float, posY: Float) {
        canvas.drawRect(
            posX,
            posY,
            posX + numberWL,
            posY + numberH / 7,
            paint
        )
    }
}