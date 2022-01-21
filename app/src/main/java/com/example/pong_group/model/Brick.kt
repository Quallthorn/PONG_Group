package com.example.pong_group.model

import android.graphics.Paint
import com.example.pong_group.controller.prefs
import com.example.pong_group.model.GameViewBreakout.Companion.breakReady
import com.example.pong_group.services.GameSettings.curCanvas
import com.example.pong_group.services.GameSounds.playSound
import com.example.pong_group.services.SharedBreakout
import com.example.pong_group.services.Sounds.*
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

class Brick(w: Float, h: Float, x: Float, y: Float, s: Int, n: Int) {
    private val classic = prefs.isClassicInterface
    private var posX = 0f
    private var posY = 0f
    private var width = 0f
    private var height = 0f
    private var pointBase = 0
    private var nr = 0
    private var paint = Paint()
    private var resetPaint = Paint()

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
    private var holdOn = false

    init {
        posX = x
        posY = y
        width = w
        height = h
        pointBase = s
        nr = n
    }

    fun setColor(color: Int){
        paint.color = color
        resetPaint.color = color
    }

    /**
     * checks if ball hits brick
     *
     * if ball hits a brick that cannot be broken yet (none of its sides are exposed)
     * it goes backwards at a slower speed until it hits an eligible brick
     * additionally if ball hits a brick through a corner it likewise goes backwards
     *
     * used by checkSpeed()
     *
     * @param ball ball in play
     */
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
                checkSnuckPassed(ball)
                if (breakReady && !holdOn)
                    ballCollide(ball)
            } else
                holdOn = false
        }

        if (!broken && !breakable)
            updateOvershootDir(ball, true)
        else if (broken)
            updateOvershootDir(ball, false)
        isBreakable()
    }

    /**
     * make sure if the ball slips past two bricks it goes back to one of the two bricks it was supposed to hit
     *
     * .    = ball (direction is up-left)
     * |X|  = brick it hits but is not supposed to (left or top side is exposed so brick is breakable)
     * |_|  = brick it can hit
     *
     * configuration:
     * |X||_|
     * |_| .
     *
     * @param ball ball in play
     */
    private fun checkSnuckPassed(ball: BallBreakout) {
        calculateDelta(ball)
        if (ball.dirX > 0 && ball.dirY > 0 && !exL && !exT) { //right down not exposed left or top
            ball.dirPositiveX()
            ball.dirPositiveY()
            fixSnuckPassed(ball, dL, dT, exLeft = true, exTop = true)
        }
        if (ball.dirX > 0 && ball.dirY < 0 && !exL && !exB) {//right up not exposed left or bottom
            ball.dirPositiveX()
            ball.dirNegativeY()
            fixSnuckPassed(ball, dL, dB, exLeft = true, exTop = false)
        }
        if (ball.dirX < 0 && ball.dirY < 0 && !exR && !exB) {//left up not exposed right or bottom
            ball.dirNegativeX()
            ball.dirNegativeY()
            fixSnuckPassed(ball, dR, dB, exLeft = false, exTop = false)
        }
        if (ball.dirX < 0 && ball.dirY > 0 && !exR && !exT) {//left down not exposed right or top
            ball.dirNegativeX()
            ball.dirPositiveY()
            fixSnuckPassed(ball, dR, dT, exLeft = false, exTop = true)
        }
    }

    /**
     * fixes if ball sneaks past two bricks
     * called by checkSnuckPassed
     *
     * @param ball ball in play
     * @param dLR float for dL or dR
     * @param dTB float for dT or dB
     * @param exLeft boolean for if Left or right side is exposed. true = left, false = right
     * @param exTop boolean for if Top or Bottom side is exposed. true = top, false = bottom
     */
    private fun fixSnuckPassed(ball: BallBreakout, dLR: Float, dTB: Float, exLeft: Boolean, exTop: Boolean){
        holdOn = true
        d = minOf(dLR, dTB)
        if (d == dLR){
            ball.posX = if (exLeft)
                 posX
            else
                posX + width
        }
        else
            ball.posY = if (exTop)
                posY
            else
                posY + height
    }

    /**
     * tells ball if it overshoots so it can retrace its steps
     *
     * @param ball ball in play
     * @param shouldCheck should ball retrace its steps? (or back to normal)
     */
    private fun updateOvershootDir(ball: BallBreakout, shouldCheck: Boolean) {
        if (ball.posX >= posX && ball.posX <= posX + width && ball.posY + ball.radius >= posY && ball.posY + ball.radius <= posY + height //ball bottom edge
            || ball.posX >= posX && ball.posX <= posX + width && ball.posY - ball.radius >= posY && ball.posY - ball.radius <= posY + height //ball top edge
            || ball.posX + ball.radius >= posX && ball.posX + ball.radius <= posX + width && ball.posY >= posY && ball.posY <= posY + height //ball right edge
            || ball.posX - ball.radius >= posX && ball.posX - ball.radius <= posX + width && ball.posY >= posY && ball.posY <= posY + height //ball left edge
        ) {
            ball.checkCollision = shouldCheck
        }
    }

    /**
     * makes the brick breakable if any of the edges are exposed
     */
    private fun isBreakable() {
        if (!breakable) {
            if (exT || exB || exR || exL)
                canBreak()
        }
    }

    /**
     * if ball has snuck passed two bricks, hit brick knows not to break or tell ball to go back to normal
     *
     * @param ball ball in play
     */
    private fun overshot(ball: BallBreakout) {
        ball.checkCollision = true
        holdOn = true
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

    /**
     * calculates delta for all sides on brick
     *
     * @param ball ball in play
     */
    private fun calculateDelta(ball: BallBreakout){
        dT = abs(ball.posY - posY)
        dB = abs(ball.posY - (posY + height))
        dR = abs(ball.posX - (posX + width))
        dL = abs(ball.posX - posX)
    }

    /**
     * checks delta according to exposed side of brick and direction of ball
     *
     * sends ball of in direction according to where it hit the brick
     * sets ball to edge of brick in case it is inside the brick
     *
     * @param ball ball in play
     */
    private fun ballCollide(ball: BallBreakout) {
        playSound(BRICK)
        ball.checkCollision = false
        breakReady = false
        calculateDelta(ball)

        d = if (ball.dirX < 0 && ball.dirY < 0) { //hit-able faces: right & bottom
            bounceDir(exLeft = false, exTop = false)
        } else if (ball.dirX > 0 && ball.dirY < 0) { //hit-able faces: left & bottom
            bounceDir(exLeft = true, exTop = false)
        } else if (ball.dirX < 0 && ball.dirY > 0) { //hit-able faces: right & top
            bounceDir(exLeft = false, exTop = true)
        } else { //hit-able faces: left & top
            bounceDir(exLeft = true, exTop = true)
        }

        when (d) {
            dT -> {
                ball.posY = posY - ball.radius
                ball.dirNegativeY()
            }
            dB -> {
                ball.posY = posY + height + ball.radius
                ball.dirPositiveY()
            }
            dR -> {
                ball.posX = posX + width + ball.radius
                ball.dirPositiveX()
            }
            else -> {
                ball.posX = posX - ball.radius
                ball.dirNegativeX()
            }
        }
        SharedBreakout.checkSurroundings(nr)
        breakBrick()
    }

    private fun bounceDir(exLeft: Boolean, exTop: Boolean) : Float {
        val exLR: Boolean
        val dLR: Float
        val exTB: Boolean
        val dTB: Float
        if (exLeft){
            exLR = exL
            dLR = dL
        }
        else{
            exLR = exR
            dLR = dR
        }
        if (exTop){
            exTB = exT
            dTB = dT
        }
        else{
            exTB = exB
            dTB = dB
        }
        return if (exLR && exTB)
            minOf(dB, dR)
        else if (exLR && !exTB)
            dLR
        else
            dTB
    }

    /**
     * tells canvas to stop render brick
     * adds score to player
     */
    private fun breakBrick() {
        broken = true
        GameViewBreakout.totalCountOfBricks -= 1
        SharedBreakout.addScore(pointBase)

        if (classic && !SharedBreakout.maxSpeedAchieved)
            SharedBreakout.updateSpeedClassic(pointBase)
    }

    /**
     * resets brick
     */
    fun reset() {
        broken = false
        breakable = false
        holdOn = false
        exT = false
        exB = false
        exR = false
        exL = false
        paint.color = resetPaint.color
    }

    /**
     * makes brick breakable
     */
    private fun canBreak() {
        breakable = true
        //paint.color = Color.WHITE
    }
}