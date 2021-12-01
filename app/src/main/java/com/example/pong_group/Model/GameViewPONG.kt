package com.example.pong_group.Model

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.example.pong_group.R

class GameViewPONG(context: Context) : SurfaceView(context), SurfaceHolder.Callback, Runnable {

    private var thread: Thread? = null
    private var running = false
    lateinit var canvas: Canvas
    private lateinit var player: Paddle
    private lateinit var ball1: Ball
    private var ballA = mutableListOf<Ball>()
    var mHolder: SurfaceHolder? = holder
    var screenWidth: Float = 0f
    var screenHeight: Float = 0f

    init {
        mHolder?.addCallback(this)
        setup()
    }

    fun setup() {
        player = Paddle(this.context, screenWidth, screenHeight)
        ball1 = Ball(this.context, screenWidth, screenHeight)
        for (i in 0 until 6) {
            val newBall = Ball(this.context, screenWidth, screenHeight)
            newBall.dirX = ((0..100).random())/100f
            newBall.dirY = ((0..100).random())/100f
            newBall.paint.color = context.resources.getColor(R.color.white)
            ballA.add(newBall)
        }
        player.paint.color = context.resources.getColor(R.color.white)
        ball1.paint.color = context.resources.getColor(R.color.white)
        ball1.dirX = 0.25f
        ball1.dirY = -0.67f
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
        ball1.update(player.posX, player.posY, player.width, player.height)
        for (i in 0 until 6) {
            ballA[i].update(player.posX, player.posY, player.width, player.height)
        }
    }

    fun draw() {
        canvas = mHolder!!.lockCanvas()
        canvas.drawColor(Color.BLACK)
        player.draw(canvas)
        ball1.draw(canvas)
        for (i in 0 until 6) {
            ballA[i].draw(canvas)
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
        val x = event?.x
        if (x != null) {
            player.posX = x
        }
        return true
    }
}
