package com.example.pong_group.Model

import android.content.Context
import android.graphics.Color
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.example.pong_group.Controller.App
import com.example.pong_group.Controller.prefs
import com.example.pong_group.R
import com.example.pong_group.Services.GameSettings
import com.example.pong_group.Services.GameSettings.ballCount
import com.example.pong_group.Services.GameSettings.curCanvas
import com.example.pong_group.Services.GameSettings.gameOver
import com.example.pong_group.Services.GameSettings.screenHeight
import com.example.pong_group.Services.GameSettings.screenWidth
import com.example.pong_group.Services.GameSounds
import kotlin.math.sqrt
import com.example.pong_group.Services.NumberPrinter
import kotlin.math.abs

class GameViewPONG(context: Context) : SurfaceView(context), SurfaceHolder.Callback, Runnable {

    private var player: PaddlePong
    private var p2cpu: PaddlePong
    private var ballPong: BallPong
    private var ballA = mutableListOf<BallPong>()
    private var mHolder: SurfaceHolder? = holder

    private val twoPlayers = prefs.isP2Human

    //based on screen size
    private var numberFromEdge = 0f
    private var numberFromMiddle = 0f
    private var paddlePosY = 0f
    private var paddleWidth = 0f
    private var paddleHeight = 0f

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
        basedOnScreenSize()
        ballPong.centerBall()
        ballPong.letGo = true
//        makeBallArray()
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

        if (!gameOver && !twoPlayers) moveCpuPaddle()

        ballPong.update(
            player,
            p2cpu
        )
//        ballA.forEach {
//            it.update(
//                player,
//                p2cpu.posX
//            )
//        }
        if (ballPong.changeColor) changeColors()
    }

    private fun draw() {
        curCanvas = mHolder!!.lockCanvas()
        curCanvas.drawColor(Color.BLACK)
        if (gameOver) drawWinner()
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
        val lineW = (screenWidth + lineSpacing) / amount - lineSpacing
        val thickness = 5f
        for (i in 0 until amount) {
            curCanvas.drawRect(
                lineX,
                screenHeight / 2 - thickness,
                lineX + lineW,
                screenHeight / 2 + thickness,
                GameSettings.curPaint
            )
            lineX += (lineW + lineSpacing)
        }
    }

    private fun drawWinner() {
        if (PaddlePong.playerScore < PaddlePong.cpuScore) {
            if (twoPlayers)
                NumberPrinter.drawP2Wins()
            else
                NumberPrinter.drawCpuWins()
        } else
            NumberPrinter.drawP1Wins()
    }

    private fun basedOnScreenSize() {
        numberFromEdge = screenWidth / 25f
        numberFromMiddle = screenHeight / 25f
        player.scorePositionXL = screenWidth - numberFromEdge - NumberPrinter.numberWL
        player.scorePositionXR =
            screenWidth - numberFromEdge - NumberPrinter.numberWL * 2 - NumberPrinter.numberW
        player.scorePositionY = screenHeight / 2 + numberFromMiddle
        p2cpu.scorePositionXL = numberFromEdge
        p2cpu.scorePositionXR = numberFromEdge + NumberPrinter.numberWL + NumberPrinter.numberW
        p2cpu.scorePositionY = screenHeight / 2 - numberFromMiddle - NumberPrinter.numberH

        paddlePosY = screenHeight / 8f
        player.posY = paddlePosY
        p2cpu.posY = screenHeight - paddlePosY

        paddleWidth = screenWidth / 25f
        player.width = paddleWidth
        p2cpu.width = paddleWidth

        paddleHeight = screenHeight / 100f
        player.height = paddleHeight
        p2cpu.height = paddleHeight

        ballPong.radius = screenHeight / 185f
    }

    private fun changeColors() {
        if (prefs.isRainbowColor) {
            GameSettings.getRandomColorFromArray()
            ballPong.changeColor = false
        }
    }

    private fun makeBallArray() {
        for (i in 0 until ballCount) {
            val newBall = BallPong()
            val s = (0..100).random() / 100f
            newBall.dirX = s
            newBall.dirY = sqrt((1 - newBall.dirX * newBall.dirX))
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
            newBall.letGo = true
            ballA.add(newBall)
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
            if (!twoPlayers) {
                movePlayerPaddle(event, player)
            } else {
                if (event.y > screenHeight / 2) {
                    movePlayerPaddle(event, player)
                }
                if (event.y < screenHeight / 2) {
                    if (!ballPong.playersReady)
                        ballPong.dirNegativeY()
                    movePlayerPaddle(event, p2cpu)
                }
            }
        }
        return true
    }


    private fun movePlayerPaddle(event: MotionEvent?, player: PaddlePong) {
        player.posX = event!!.x
        ballPong.playersReady = true
        if (!player.isCpu && !ballPong.p1Scored && !ballPong.letGo || player.isCpu && ballPong.p1Scored && !ballPong.letGo) {
            if (event.action == MotionEvent.ACTION_UP) {
                GameSounds.playSoundWall()
                ballPong.letGo = true
//                        ballA.forEach {
//                            if (!it.p1Scored)
//                                it.letGo = true
//                        }
            }
        }
    }

    private fun moveCpuPaddle() {
        if (ballPong.letGo) {
            if (p2cpu.posX <= ballPong.posX - p2cpu.width / 4) //moves right if ball is right of the paddles center (XX = center) [__XX__]
                p2cpu.posX += ballPong.speed / (1..2).random() //the movement speed mirrors the balls speed but randomly slows down the half
            else if (p2cpu.posX >= ballPong.posX + p2cpu.width / 4) //moves left if ball is left of the paddles center (XX = center) [__XX__]
                p2cpu.posX -= ballPong.speed / (1..2).random()
        } else {
            if (p2cpu.posX < screenWidth / 2)
                p2cpu.posX += screenWidth / 250f
            if (p2cpu.posX > screenWidth / 2)
                p2cpu.posX -= screenWidth / 250f

            if (ballPong.p1Scored
                && abs(p2cpu.posX - screenWidth / 2) <= 5f
                && abs(ballPong.posX - screenWidth / 2) <= 5f
            ) {
                GameSounds.playSoundWall()
                ballPong.letGo = true
//                ballA.forEach {
//                    if (it.p1Scored)
//                        it.letGo = true
//                }
            }
        }
    }
}
