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
    //private lateinit var ball1: Ball
    private var ballA = mutableListOf<Ball>()
    val ballCount = 6
    var mHolder: SurfaceHolder? = holder
    var screenWidth: Float = 0f
    var screenHeight: Float = 0f

    init {
        mHolder?.addCallback(this)
        player = Paddle(this.context, screenWidth, screenHeight)

    }

    fun setup() {
        for (i in 0 until ballCount) {
            var newBall = Ball(this.context, screenWidth, screenHeight)
            newBall.dirX = ((0..100).random())/100f
            newBall.dirY = ((0..100).random())/100f
            newBall.paint.color = context.resources.getColor(R.color.white)
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
        //ball1.update(player.posX, player.posY, player.width, player.height)
        ballA.forEach{
            it.update(player.posX, player.posY, player.width, player.height)
        }
    }

    fun draw() {
        canvas = mHolder!!.lockCanvas()
        canvas.drawColor(Color.BLACK)
        player.draw(canvas)
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
        val x = event?.x
        if (x != null) {
            player.posX = x
        }
        return true
    }
}
