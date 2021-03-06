package com.example.pong_group.model

import android.content.Context
import android.graphics.Color
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.example.pong_group.controller.App
import com.example.pong_group.controller.prefs
import com.example.pong_group.R
import com.example.pong_group.services.GameSettings
import com.example.pong_group.services.GameSettings.ballCount
import com.example.pong_group.services.GameSettings.curCanvas
import com.example.pong_group.services.GameSettings.gameOver
import com.example.pong_group.services.GameSettings.screenHeight
import com.example.pong_group.services.GameSettings.screenWidth
import com.example.pong_group.services.GameSounds
import com.example.pong_group.services.GameSounds.playSound
import kotlin.math.sqrt
import com.example.pong_group.services.NumberPrinter
import com.example.pong_group.services.Sounds.*
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
        GameSounds.alternate = false

        mHolder?.addCallback(this)

        player = PaddlePong(false)
        p2cpu = PaddlePong(true)
        ballPong = BallPong()
        GameSettings.curPaint.color = App.instance.resources.getColor(R.color.white, App.instance.theme)
        changeColors()
    }

    /**
     * sets up the game
     */
    private fun setup() {
        basedOnScreenSize()
        ballPong.centerBall()
        ballPong.letGo = true
//        makeBallArray()
    }

    /**
     * creates a thread the game will be run in
     */
    private fun start() {
        running = true
        thread = Thread(this)
        thread?.start()
    }

    /**
     * stops thread when game view is exited
     */
    private fun stop() {
        running = false
        try {
            thread?.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    /**
     * updates all object in play:
     * player movement, winner text and ball movement
     *
     */
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

    /**
     * draws all objects in play
     *
     */
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

    /**
     * draws line in middle of screen
     */
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

    /**
     * draws winner text depending on who won
     */
    private fun drawWinner() {
        if (PaddlePong.playerScore < PaddlePong.cpuScore) {
            if (twoPlayers)
                NumberPrinter.drawP2Wins()
            else
                NumberPrinter.drawCpuWins()
        } else
            NumberPrinter.drawP1Wins()
    }

    /**
     * determines values for object dimensions according to size of screen
     */
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

    /**
     * changes color of paddle and ball if "prefs.isRainbowColor" is true
     */
    private fun changeColors() {
        if (prefs.isRainbowColor) {
            GameSettings.getRandomColorFromArray()
            ballPong.changeColor = false
        }
    }

    /**
     * currently not used
     * makes an array of balls if player wants to play with multiple balls at once
     * not recommended when playing against cpu
     */
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

    /**
     * creates a thread the game will be run in
     */
    override fun surfaceCreated(p0: SurfaceHolder) {
        start()
    }

    /**
     * measures screen dimensions and calls setup()
     */
    override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
        GameSettings.setScreenDimen(p2.toFloat(), p3.toFloat())
        setup()
    }

    /**
     * closes thread when game is exited
     */
    override fun surfaceDestroyed(p0: SurfaceHolder) {
        stop()
    }

    /**
     * calls update() and draw() while thread is active
     */
    override fun run() {
        while (running) {
            update()
            draw()
        }
    }

    /**
     * keeps track of finger inputs from users
     */
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


    /**
     * moves a player to fingers horizontal position
     *
     * if opposing player or cpu scored a point ball will be "held" by paddle until player releases finger from screen
     *
     * @param event MotionEvent movement will be based on
     * @param player the player that will be moved
     */
    private fun movePlayerPaddle(event: MotionEvent?, player: PaddlePong) {
        player.posX = event!!.x
        ballPong.playersReady = true
        if (!player.isCpu && !ballPong.p1Scored && !ballPong.letGo || player.isCpu && ballPong.p1Scored && !ballPong.letGo) {
            if (event.action == MotionEvent.ACTION_UP) {
                playSound(WALL)
                ballPong.letGo = true
//                        ballA.forEach {
//                            if (!it.p1Scored)
//                                it.letGo = true
//                        }
            }
        }
    }

    /**
     * moves cpu towards balls horizontal position
     *
     * if player scored a point paddle will "hold" ball and move to middle of screen before releasing ball
     */
    private fun moveCpuPaddle() {
        if (ballPong.letGo) {
            if (p2cpu.posX <= ballPong.posX - p2cpu.width / 4) //moves right if ball is right of the paddles center (XX = center) [__XX__]
                p2cpu.posX += ballPong.speed / (1..2).random() //the movement speed mirrors the balls speed but randomly slows down to half
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
                playSound(WALL)
                ballPong.letGo = true
//                ballA.forEach {
//                    if (it.p1Scored)
//                        it.letGo = true
//                }
            }
        }
    }
}
