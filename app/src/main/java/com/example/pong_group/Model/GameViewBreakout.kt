package com.example.pong_group.Model

import android.content.Context
import android.content.res.TypedArray
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.util.Log
import android.view.*
import com.example.pong_group.Controller.App
import android.widget.Button
import com.example.pong_group.R
import com.example.pong_group.Services.GameSettings
import com.example.pong_group.Services.GameThread

import android.widget.TextView

import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import com.example.pong_group.Services.SharedBreakout


class GameViewBreakout(context: Context) : SurfaceView(context), SurfaceHolder.Callback {

    private var thread: GameThread
    private var player: PaddleBreakout
    private var paddlePosY = 0f
    private var ball: BallBreakout
    private var rngColor = Paint()

    private lateinit var restartButton: SurfaceViewButton
    var pause = false

    private val colorArray: TypedArray
    private val classic = GameSettings.classicBreakout
    var everyOther = false
    var level = 1
    var gridPosX: Float = 0f
    var gridPosY: Float = 0f
    var gridSqueezeX: Float = 0f
    var gridStartY: Float = 120f
    var gridSpacingX: Float = 0f
    var gridSpacingY: Float = 0f
    var brickH: Float = 50f
    var brickW: Float = 0f

    companion object {
        var canvasBreakout = Canvas()
        var totalCountOfBricks = 0
        var isGameFinished = false
        var outOfLives = false
        var lives = 3
        var breakReady = true
    }

    init {
        if (classic) {
            SharedBreakout.brickCountX = 14
            SharedBreakout.brickCountY = 8
            gridSpacingX = 5f
            gridSpacingY = 5f
            brickH = 20f
            colorArray = App.instance.resources.obtainTypedArray(R.array.breakout_bricks_classic)
        } else {
            colorArray = App.instance.resources.obtainTypedArray(R.array.breakout_bricks)
            SharedBreakout.brickCountY = (when (level) {
                1 -> {
                    6
                }
                2 -> {
                    9
                }
                else -> {
                    0
                }
            })
        }
        holder.addCallback(this)

        gridPosX = gridSqueezeX
        gridPosY = gridStartY

        player = PaddleBreakout()
        ball = BallBreakout()
        changeColors()

        thread = GameThread(holder, this)

        SharedBreakout.gameSetUpBreakout()
    }



    private fun setup() {
        gridPosX = gridSqueezeX
        gridPosY = gridStartY
        totalCountOfBricks = SharedBreakout.brickCountX*SharedBreakout.brickCountY
        paddlePosY = GameSettings.screenHeight / 7.2f
        player.posY = paddlePosY
        ball.centerBall(player.posX, player.posY)

        brickW =
            ((GameSettings.screenWidth - gridSqueezeX * 2) / SharedBreakout.brickCountX) - gridSpacingX
        var colorNumber = 1
        var pointBase = SharedBreakout.brickCountY
        if (level == 2 || classic)
            colorNumber = 0
        if (classic) {
            pointBase = 7
        }

        SharedBreakout.bricks.clear()

        for (i in 0 until (SharedBreakout.brickCountX * SharedBreakout.brickCountY)) {
            //set position
            var newBrick = Brick(brickW, brickH, gridPosX, gridPosY, pointBase, i)

            //set color
            val colorInt = colorArray.getColor(colorNumber, 0)
            newBrick.paint.color = colorInt

            //change position for next brick
            gridPosX += (gridSpacingX + brickW)
            if (gridPosX >= (gridSpacingX + brickW) * SharedBreakout.brickCountX + gridSqueezeX) {
                gridPosX = gridSqueezeX
                gridPosY += (gridSpacingY + brickH)
                colorNumber++
                if (classic){
                    everyOther = if (everyOther){
                        pointBase -= 2
                        false
                    } else
                        true
                }
                else
                    pointBase--
            }
            SharedBreakout.bricks.add(newBrick)
        }
    }

    fun update() {
        //player.update()
        ball.update(player)
        SharedBreakout.bricks.forEach {
            it.update(ball)
        }
        if (ball.changeColor)
            changeColors()
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        canvas.also {
            it.drawColor(Color.BLACK)
            player.draw()
            if (!outOfLives)
                ball.draw()
            SharedBreakout.bricks.forEach { brick ->
                brick.draw()
            }


        }
    }

    private fun changeColors() {
        if (!classic){
            rngColor.color = GameSettings.getRandomColorFromArray()
            player.paint = rngColor
            ball.paint = rngColor
            ball.changeColor = false
        }
    }

    override fun surfaceCreated(p0: SurfaceHolder) {
        if (thread.state == Thread.State.TERMINATED) {
            thread = GameThread(holder, this)
        }
        thread.running = true
        thread.start()
    }

    override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
        GameSettings.setScreenDimen(p2.toFloat(), p3.toFloat())
        setup()
    }

    override fun surfaceDestroyed(p0: SurfaceHolder) {
        while (thread.isAlive) {
            try {
                thread.running = false
                thread.join()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event != null) {
            player.posX = event.x
        }

        val x = event?.x
        val y = event?.y

        if (isGameFinished) {
            if (restartButton.btn_rect!!.contains(x!!,y!!)){
                setup()
                isGameFinished = false
                outOfLives = false
                lives = 3
                GameSettings.scoreBreakout = 0
                resumeThread()
            }
        }
        return true
    }


    @RequiresApi(Build.VERSION_CODES.M)
    fun checkEndOfTheGame(){
        if(totalCountOfBricks == 0 || outOfLives) {
            val layout = LinearLayout(App.instance)
            layout.orientation = LinearLayout.VERTICAL
            layout.gravity = Gravity.CENTER

            val GameOverTextView = TextView(App.instance)

            GameOverTextView.visibility = View.VISIBLE
            GameOverTextView.text = "GAME\nOVER"
            GameOverTextView.textSize = 100f
            GameOverTextView.gravity = Gravity.CENTER
            val typeFace = ResourcesCompat.getFont(App.instance, R.font.arcade_classic)
            GameOverTextView.typeface = typeFace
            GameOverTextView.setTextColor(App.instance.resources.getColor(R.color.white, context.theme))

            val measure = GameOverTextView.measure(0,0)
            val gameTextViewHeight = GameOverTextView.measuredHeight


            layout.addView(GameOverTextView)

            layout.measure(canvasBreakout.width, canvasBreakout.height)
            layout.layout(0, 0, canvasBreakout.width, canvasBreakout.height)
            layout.draw(canvasBreakout)

            val icon = BitmapFactory.decodeResource(App.instance.resources, R.drawable.reset)
            restartButton = SurfaceViewButton( icon)
            val restartButtonX = (canvasBreakout.width/2).toFloat()- restartButton.width/2
            val restartButtonY = (canvasBreakout.width/2).toFloat()+gameTextViewHeight + 80f
            restartButton.setPosition(restartButtonX, restartButtonY)
            restartButton.draw(canvasBreakout)
            isGameFinished = true
            pauseThread()
        }
    }

    fun resumeThread() {
        pause = false
        surfaceCreated(holder)
    }

    fun pauseThread() {
        pause = true
    }



}
//for multiple balls (power up?)
//private var ballA = mutableListOf<BallBreakout>()
//val ballCount = 0

// in setup()
//        for (i in 0 until ballCount) {
//            var newBall = BallBreakout()
//            var s = (0..100).random() / 100f
//            newBall.dirX = s
//            newBall.dirY = Math.sqrt((1 - newBall.dirX * newBall.dirX).toDouble()).toFloat()
//            var d = (0..3).random()
//            when (d) {
//                1 -> newBall.dirX = newBall.dirX * -1
//                2 -> newBall.dirY = newBall.dirY * -1
//                3 -> {
//                    newBall.dirY = newBall.dirY * -1
//                    newBall.dirX = newBall.dirX * -1
//                }
//                else -> {}
//            }
//            newBall.paint.color = GameSettings.getRandomColorFromArray()
//            ballA.add(newBall)
//        }

// in update()
//        ballA.forEach {
//            it.update(player.posX, player.posY, player.width, player.posXOld)
//        }

// in draw()
//        ballA.forEach {
//            it.draw(canvas)
//        }
