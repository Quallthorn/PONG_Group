package com.example.pong_group.Services

import com.example.pong_group.Services.GameSettings.curCanvas
import com.example.pong_group.Services.GameSettings.screenWidth
import com.example.pong_group.Services.GameSettings.screenHeight


object NumberPrinter {

    const val numberW = 20f
    const val numberWL = 90f
    const val numberH = 180f
    var paint = GameSettings.curPaint

    private var posXWin = 0f
    private var posYWin = 0f

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

    fun draw(digit: Int, startX: Float, startY: Float) {
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

    fun drawP1Wins() {
        posXWin = (screenWidth - numberW) / 2 - numberWL
        posYWin = startYWinsText()

        drawP(posXWin, posYWin)
        addWidth()
        drawFancyOne(posXWin, posYWin)

        addHeight()
        posXWin = startXWinsText()

        drawW(posXWin, posYWin)
        addWidthLong()
        drawI(posXWin, posYWin)
        addWidth()
        drawN(posXWin, posYWin)
        addWidth()
        drawFive(posXWin, posYWin)
    }

    fun drawP2Wins() {
        posXWin = (screenWidth - numberW) / 2 - numberWL
        addHeight()

        drawTwo(posXWin, posYWin)
        addWidth()
        drawD(posXWin, posYWin)

        posXWin = startXWinsText()
        posYWin = startYWinsText()

        drawFive(posXWin, posYWin)
        addWidth()
        drawN(posXWin, posYWin)
        addWidth()
        drawI(posXWin, posYWin)
        addWidth()
        drawM(posXWin, posYWin)
    }

    fun drawCpuWins() {
        posXWin = (screenWidth - numberWL * 3) / 2 - numberW
        posYWin = startYWinsText()

        drawC(posXWin, posYWin)
        addWidth()
        drawP(posXWin, posYWin)
        addWidth()
        drawU(posXWin, posYWin)

        addHeight()
        posXWin = startXWinsText()

        drawW(posXWin, posYWin)
        addWidthLong()
        drawI(posXWin, posYWin)
        addWidth()
        drawN(posXWin, posYWin)
        addWidth()
        drawFive(posXWin, posYWin)
    }

    private fun addWidth() {
        posXWin += numberW + numberWL
    }

    private fun addWidthLong() {
        posXWin += numberWL * 2
    }

    private fun addHeight() {
        posYWin += numberH + numberW
    }

    private fun startXWinsText(): Float {
        return (screenWidth - numberWL * 5 - numberW * 2) / 2
    }

    private fun startYWinsText(): Float {
        return (screenHeight - numberW) / 2 - numberH
    }

    //Numbers
    private fun drawZero(startX: Float, startY: Float) {
        for (i in 0..1)
            lineHorizontal(startX, startY + numberH / 7 * i * 6)
        for (i in 0..1)
            lineVerticalLong(startX + (numberWL - numberW) * i, startY)
    }

    private fun drawOne(startX: Float, startY: Float) {
        lineVerticalLong(startX + numberWL - numberW, startY)
    }

    private fun drawTwo(startX: Float, startY: Float) {
        for (i in 0..2)
            lineHorizontal(startX, startY + numberH / 7 * i * 3)
        lineVerticalHalf(startX + numberWL - numberW, startY)
        lineVerticalHalf(startX, startY + numberH / 7 * 3)
    }

    private fun drawThree(startX: Float, startY: Float) {
        for (i in 0..2)
            lineHorizontal(startX, startY + numberH / 7 * i * 3)
        lineVerticalLong(startX + numberWL - numberW, startY)
    }

    private fun drawFour(startX: Float, startY: Float) {
        lineVerticalHalf(startX, startY)
        lineVerticalLong(startX + numberWL - numberW, startY)
        lineHorizontal(startX, startY + numberH / 7 * 3)
    }

    private fun drawFive(startX: Float, startY: Float) {
        for (i in 0..2)
            lineHorizontal(startX, startY + numberH / 7 * i * 3)
        lineVerticalHalf(startX + numberWL - numberW, startY + numberH / 7 * 3)
        lineVerticalHalf(startX, startY)
    }

    private fun drawSix(startX: Float, startY: Float) {
        for (i in 1..2)
            lineHorizontal(startX, startY + numberH / 7 * i * 3)
        lineVerticalHalf(startX + numberWL - numberW, startY + numberH / 7 * 3)
        lineVerticalLong(startX, startY)
    }

    private fun drawSeven(startX: Float, startY: Float) {
        lineHorizontal(startX, startY)
        lineVerticalLong(startX + numberWL - numberW, startY)
    }

    private fun drawEight(startX: Float, startY: Float) {
        for (i in 0..2)
            lineHorizontal(startX, startY + numberH / 7 * i * 3)
        for (i in 0..1)
            lineVerticalLong(startX + (numberWL - numberW) * i, startY)
    }

    private fun drawNine(startX: Float, startY: Float) {
        for (i in 0..1)
            lineHorizontal(startX, startY + numberH / 7 * i * 3)
        lineVerticalHalf(startX, startY)
        lineVerticalLong(startX + numberWL - numberW, startY)
    }

    private fun drawFancyOne(startX: Float, startY: Float) {
        lineVerticalLong(startX + numberWL / 8 * 3, startY)
        lineHorizontal(startX, startY + (numberH / 7 * 6))
        lineHorizontalHalf(startX, startY)
    }

    //Letters
    private fun drawP(startX: Float, startY: Float) {
        for (i in 0..1)
            lineHorizontal(startX, startY + numberH / 7 * i * 3)
        lineVerticalLong(startX, startY)
        lineVerticalHalf(startX + (numberWL - numberW), startY)
    }

    private fun drawD(startX: Float, startY: Float) {
        for (i in 1..2)
            lineHorizontal(startX, startY + numberH / 7 * i * 3)
        lineVerticalLong(startX + (numberWL - numberW), startY)
        lineVerticalHalf(startX, startY + numberH / 7 * 3)
    }

    private fun drawW(startX: Float, startY: Float) {
        for (i in 0..2)
            lineVerticalLong(startX + (numberWL - numberW) * i, startY)
        for (i in 0..1)
            lineHorizontal(startX + (numberWL - numberW) * i, startY + numberH / 7 * 6)
    }

    private fun drawM(startX: Float, startY: Float) {
        for (i in 0..2)
            lineVerticalLong(startX + (numberWL - numberW) * i, startY)
        for (i in 0..1)
            lineHorizontal(startX + (numberWL - numberW) * i, startY)
    }

    private fun drawI(startX: Float, startY: Float) {
        lineVerticalLong(startX + numberWL / 8 * 3, startY)
        for (i in 0..1)
            lineHorizontal(startX, startY + (numberH / 7 * 6) * i)
    }

    private fun drawN(startX: Float, startY: Float) {
        for (i in 0..1)
            lineVerticalLong(startX + (numberWL - numberW) * i, startY)
        dot(startX + numberW, startY + numberH / 4)
        dot(startX + numberW + (numberWL - numberW * 2) / 2, startY + numberH / 2)
    }

    private fun drawU(startX: Float, startY: Float) {
        for (i in 0..1)
            lineVerticalLong(startX + (numberWL - numberW) * i, startY)
        lineHorizontal(startX, startY + numberH / 7 * 6)
    }

    private fun drawC(startX: Float, startY: Float) {
        lineVerticalLong(startX, startY)
        for (i in 0..1)
            lineHorizontal(startX, startY + (numberH / 7 * 6) * i)
    }

    //Lines
    private fun lineVerticalLong(posX: Float, posY: Float) {
        curCanvas.drawRect(
            posX,
            posY,
            posX + numberW,
            posY + numberH,
            paint
        )
    }

    private fun lineVerticalHalf(posX: Float, posY: Float) {
        curCanvas.drawRect(
            posX,
            posY,
            posX + numberW,
            posY + numberH / 7 * 4,
            paint
        )
    }

    private fun lineHorizontal(posX: Float, posY: Float) {
        curCanvas.drawRect(
            posX,
            posY,
            posX + numberWL,
            posY + numberH / 7,
            paint
        )
    }

    private fun dot(posX: Float, posY: Float) {
        curCanvas.drawRect(
            posX,
            posY,
            posX + (numberWL - numberW * 2) / 2,
            posY + numberH / 4,
            paint
        )
    }

    private fun lineHorizontalHalf(posX: Float, posY: Float) {
        curCanvas.drawRect(
            posX,
            posY,
            posX + numberWL / 2,
            posY + numberH / 7,
            paint
        )
    }
}