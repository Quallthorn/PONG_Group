package com.example.pong_group.Services

import android.view.SurfaceHolder
import com.example.pong_group.Model.GameViewBreakout
import com.example.pong_group.Model.GameViewBreakout.Companion.canvasBreakout

class GameThread(
    private val surfaceHolder: SurfaceHolder,
    private val gameView: GameViewBreakout
) : Thread() {

    private val targetFPS = 60
    var running: Boolean = false

    override fun run() {
        var startTime: Long
        val targetTime = (1000 / targetFPS).toLong()

        while (running) {
            startTime = System.nanoTime()


            try {
                canvasBreakout = surfaceHolder.lockCanvas()?.also {
                    synchronized(surfaceHolder) {
                        gameView.update()
                        gameView.draw(it)
                    }
                    surfaceHolder.unlockCanvasAndPost(it)
                }!!
            } catch (ignored: Exception){

            }

            try {
                sleep(targetTime - (System.nanoTime() - startTime) / 1000000)
            } catch (ignored: Exception) {
            }
        }
    }
}