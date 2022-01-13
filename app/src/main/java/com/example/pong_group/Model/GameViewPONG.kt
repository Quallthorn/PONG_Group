package com.example.pong_group.Model

import android.content.Context
import android.graphics.Color
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.example.pong_group.Controller.App
import com.example.pong_group.R
import com.example.pong_group.Services.GameSettings
import com.example.pong_group.Services.GameSettings.ballCount
import com.example.pong_group.Services.GameSettings.curCanvas
import com.example.pong_group.Services.GameSettings.gameOver
import com.example.pong_group.Services.GameSettings.opponentP2
import com.example.pong_group.Services.GameSettings.screenHeight
import com.example.pong_group.Services.GameSounds
import kotlin.math.sqrt
import com.example.pong_group.Services.NumberPrinter
import kotlin.math.abs

class GameViewPONG(context: Context) : SurfaceView(context), SurfaceHolder.Callback, Runnable {

    private var player: PaddlePong
    private var p2cpu: PaddlePong
    private var paddlePosY = 0f
    private var paddleWidth = 0f
    private var ballPong: BallPong
    private var ballA = mutableListOf<BallPong>()
    private var mHolder: SurfaceHolder? = holder

    private val numberFromEdge = 100f
    private val numberFromMiddle = 100f

    companion object {
        var thread: Thread? = null
        var running = false
    }

    init {
        gameOver = false
        PaddlePong.cpuScore = 0
        PaddlePong.playerScore = 0
        PaddlePong.absoluteScore = 0

        mHolder?.addCallback(this)

        player = PaddlePong(false)
        p2cpu = PaddlePong(true)
        ballPong = BallPong()
        GameSettings.curPaint.color = App.instance.resources.getColor(R.color.white)
        changeColors()
    }

    private fun setup() {
        player.scorePositionXL = GameSettings.screenWidth - numberFromEdge - NumberPrinter.numberWL
        player.scorePositionXR =
            GameSettings.screenWidth - numberFromEdge - NumberPrinter.numberWL * 2 - NumberPrinter.numberW
        player.scorePositionY = GameSettings.screenHeight / 2 + numberFromMiddle
        p2cpu.scorePositionXL = numberFromEdge
        p2cpu.scorePositionXR = numberFromEdge + NumberPrinter.numberWL + NumberPrinter.numberW
        p2cpu.scorePositionY =
            GameSettings.screenHeight / 2 - numberFromMiddle - NumberPrinter.numberH

        paddlePosY = GameSettings.screenHeight / 7.2f
        player.posY = paddlePosY
        p2cpu.posY = GameSettings.screenHeight - paddlePosY

        paddleWidth = GameSettings.screenWidth / 25f
        player.width = paddleWidth
        p2cpu.width = paddleWidth

        ballPong.centerBall()
        ballPong.start = true
        for (i in 0 until ballCount) {
            val newBall = BallPong()
            val s = (0..100).random() / 100f
            newBall.dirX = s
            newBall.dirY = sqrt((1 - newBall.dirX * newBall.dirX))
            //var d = 2
            when ((0..3).random()) {
                1 -> newBall.dirX = newBall.dirX * -1
                2 -> newBall.dirY = newBall.dirY * -1
                3 -> {
                    newBall.dirY = newBall.dirY * -1
                    newBall.dirX = newBall.dirX * -1
                }
                else -> {}
            }
            newBall.paint.color = GameSettings.getRandomColorFromArray()
            newBall.start = true
            ballA.add(newBall)
        }
    }

    private fun start() {
        running = true
        thread = Thread(this)
        thread?.start()
    }

    private fun stop() {
        running = false
        try {
            thread?.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    private fun update() {
        player.update()

        if (!gameOver && !opponentP2){
            if (ballPong.start) {
                if (p2cpu.posX <= ballPong.posX - p2cpu.width / 4)
                    p2cpu.posX += ballPong.speed / (1..2).random()
                else if (p2cpu.posX >= ballPong.posX + p2cpu.width / 4)
                    p2cpu.posX -= ballPong.speed / (1..2).random()
            } else {
                if (p2cpu.posX < GameSettings.screenWidth / 2)
                    p2cpu.posX += GameSettings.screenWidth / 250f
                if (p2cpu.posX > GameSettings.screenWidth / 2)
                    p2cpu.posX -= GameSettings.screenWidth / 250f

                if (ballPong.p1Scored
                    && abs(p2cpu.posX - GameSettings.screenWidth / 2) <= 5f
                    && abs(ballPong.posX - GameSettings.screenWidth / 2) <= 5f
                ){
                    GameSounds.playSound()
                    ballPong.start = true
                    ballA.forEach {
                        if (it.p1Scored)
                            it.start = true
                    }
                }
            }
        }

        ballPong.update(
            player,
            p2cpu.posX
        )
        ballA.forEach {
            it.update(
                player,
                p2cpu.posX
            )
        }
        if (ballPong.changeColor)
            changeColors()
    }

    private fun draw() {
        curCanvas = mHolder!!.lockCanvas()
        curCanvas.drawColor(Color.BLACK)
        if (gameOver){
            if (PaddlePong.playerScore < PaddlePong.cpuScore){
                if (opponentP2)
                    NumberPrinter.drawP2Wins()
                else
                    NumberPrinter.drawCpuWins()
            }
            else
                NumberPrinter.drawP1Wins()
        }
        drawLine()
        player.draw()
        p2cpu.draw()
        ballPong.draw()
        ballA.forEach {
            it.draw()
        }
        mHolder!!.unlockCanvasAndPost(curCanvas)
    }

    private fun drawLine() {
        var lineX = 0f
        val amount = 20
        val lineSpacing = 30
        val lineW = (GameSettings.screenWidth + lineSpacing) / amount - lineSpacing
        val thickness = 5f
        for (i in 0 until amount) {
            curCanvas.drawRect(
                lineX,
                GameSettings.screenHeight / 2 - thickness,
                lineX + lineW,
                GameSettings.screenHeight / 2 + thickness,
                GameSettings.curPaint
            )
            lineX += (lineW + lineSpacing)
        }
    }

    private fun changeColors() {
        if (GameSettings.rainbowColor){
            GameSettings.getRandomColorFromArray()
            ballPong.changeColor = false
        }
    }

    override fun surfaceCreated(p0: SurfaceHolder) {
        start()
    }

    override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
        GameSettings.setScreenDimen(p2.toFloat(), p3.toFloat())
        setup()
    }

    override fun surfaceDestroyed(p0: SurfaceHolder) {
        stop()
    }

    override fun run() {
        while (running) {
            update()
            draw()
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event != null && !gameOver) {
            if (!opponentP2) {
                player.posX = event.x
                if (!ballPong.p1Scored && !ballPong.start) {
                    if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL){
                        GameSounds.playSound()
                        ballPong.start = true
                        ballA.forEach {
                            if (!it.p1Scored)
                                it.start = true
                        }
                    }
                }
            }
            else{
                if (event.y > screenHeight/2){
                    player.posX = event.x

                    if (!ballPong.p1Scored && !ballPong.start) {
                        if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL){
                            GameSounds.playSound()
                            ballPong.start = true
                            ballA.forEach {
                                if (!it.p1Scored)
                                    it.start = true
                            }
                        }
                    }
                }
                else{
                    p2cpu.posX = event.x

                    if (ballPong.p1Scored && !ballPong.start) {
                        if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL){
                            GameSounds.playSound()
                            ballPong.start = true
                            ballA.forEach {
                                if (it.p1Scored)
                                    it.start = true
                            }
                        }
                    }
                }
            }
        }
        return true
    }
}
