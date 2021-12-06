package com.example.pong_group.Model

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView


class GameViewPONG(context: Context) : SurfaceView(context), SurfaceHolder.Callback, Runnable {

    private var thread: Thread? = null
    private var running = false
    lateinit var canvas: Canvas
    private var player: Paddle
    private var CPU: Paddle
    private val playerY = 250f
    //private lateinit var ball1: Ball
    private var ballA = mutableListOf<Ball>()
    val colorArray = context.resources.obtainTypedArray(com.example.pong_group.R.array.breakout)
    val colors = IntArray(colorArray.length())
    val colorCount = colorArray.length()
    val ballCount = 1
    var mHolder: SurfaceHolder? = holder
    var screenWidth: Float = 0f
    var screenHeight: Float = 0f

    init {
        mHolder?.addCallback(this)
        player = Paddle(this.context, screenWidth, screenHeight)
        player.posY = playerY
        CPU = Paddle(this.context, screenWidth,screenHeight)
        CPU.posY = screenHeight - playerY
    }

    fun setup() {
        for (i in 0 until colorCount) {
            colors[i] = colorArray.getColor(i, 0)
        }
        colorArray.recycle()

        for (i in 0 until ballCount) {
            var newBall = Ball(this.context, screenWidth, screenHeight)
            var s = (0..100).random()/100f
            newBall.dirX = s
            newBall.dirY = Math.sqrt((1 - newBall.dirX * newBall.dirX).toDouble()).toFloat()
            var d = (0..3).random()
            //var d = 2
            when(d){
                1 -> newBall.dirX = newBall.dirX * -1
                2 -> newBall.dirY = newBall.dirY * -1
                3 -> {
                    newBall.dirY = newBall.dirY * -1
                    newBall.dirX = newBall.dirX * -1
                }
                else -> {}
            }
            var c = (0 until colorCount).random()
            newBall.paint.color = colors[c]
            ballA.add(newBall)
        }
    }

    fun start() {
        running = true
        thread = Thread(this)
        thread?.start()
    }

    fun stop() {
        running = false
        try {
            thread?.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    fun update() {
        player.update()
        CPU.update()
        if (!ballA.isEmpty()){
            if (CPU.posX <= ballA[0].posX - CPU.width/4)
                CPU.posX += ballA[0].speed / (1..2).random()
            else if (CPU.posX >= ballA[0].posX + CPU.width/4)
                CPU.posX -= ballA[0].speed / (1..2).random()
        }
        //ball1.update(player.posX, player.posY, player.width, player.height)
        ballA.forEach{
            it.update(player.posX, player.posY, player.width, player.height, player.posXOld, CPU.posX)
        }
    }

    fun draw() {
        canvas = mHolder!!.lockCanvas()
        canvas.drawColor(Color.BLACK)
        player.draw(canvas)
        CPU.draw(canvas)
        //ball1.draw(canvas)
        ballA.forEach{
            it.draw(canvas)
        }
        mHolder!!.unlockCanvasAndPost(canvas)
    }

    override fun surfaceCreated(p0: SurfaceHolder) {
        start()
    }

    override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
        screenWidth = p2.toFloat()
        screenHeight = p3.toFloat()
        setup()
        player.screenWidth = screenWidth
        player.screenHeight = screenHeight
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
        if (event != null && event.x >= player.width/2 && event.x <= screenWidth - player.width/2) {
            player.posX = event.x
        }
        return true
    }
}
