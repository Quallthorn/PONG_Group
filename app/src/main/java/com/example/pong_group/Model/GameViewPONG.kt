package com.example.pong_group.Model

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.example.pong_group.R
import com.example.pong_group.databinding.ActivityGamePongBinding

class GameViewPONG(context: Context, surface: SurfaceView): SurfaceView(context), SurfaceHolder.Callback, Runnable {

    private var thread: Thread? = null
    private var running = false
    lateinit var canvas: Canvas
    private lateinit var ball1: Ball
    private lateinit var ball2: Ball
    var mHolder: SurfaceHolder? = holder
    var mSurface: SurfaceView = surface

    init{
        mHolder?.addCallback(this)
        setup()
    }

    fun setup(){
        ball1 = Ball(this.context)
//        ball1.posX = 50f
//        ball1.posY = 50f
        ball1.posX = mSurface.width.toFloat() * 0.5f
        ball1.posY = mSurface.height.toFloat() * 0.5f
        ball1.paint.color = context.resources.getColor(R.color.white)
    }

    fun start(){
        running = true
        thread = Thread(this)
        thread?.start()
    }

    fun stop(){
        running = false
        try{
            thread?.join()
        } catch (e: InterruptedException){
            e.printStackTrace()
        }
    }

    fun update(){
        ball1.update(mSurface)
    }

    fun draw(){
        canvas = mHolder!!.lockCanvas()
        canvas.drawColor(Color.BLACK)
        ball1.draw(canvas)
        mHolder!!.unlockCanvasAndPost(canvas)
    }

    override fun surfaceCreated(p0: SurfaceHolder) {
        start()
    }

    override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {

    }

    override fun surfaceDestroyed(p0: SurfaceHolder) {
        stop()
    }

    override fun run() {
        while (running){
            update()
            draw()
        }
    }
}