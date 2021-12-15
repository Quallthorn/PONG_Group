package com.example.pong_group.Services

import android.view.SurfaceHolder
import com.example.pong_group.Model.GameViewBreakout
import com.example.pong_group.Model.GameViewBreakout.Companion.canvasBreakout

class GameThread(
    private val surfaceHolder: SurfaceHolder,
    private val gameView: GameViewBreakout
) : Thread() {

    private val targetFPS = 120
    var running: Boolean = false

    override fun run() {
        var startTime: Long
        val targetTime = (1000 / targetFPS).toLong()
        var cLocked = false

        while (running) {
            startTime = System.nanoTime()
            try {
                canvasBreakout = surfaceHolder.lockCanvas()?.also {
                    cLocked = true
                    synchronized(surfaceHolder) {
                        gameView.update()
                        gameView.draw(it)
                    }
                    surfaceHolder.unlockCanvasAndPost(it)
                    cLocked = false
                }!!
            } catch (ignored: Exception){
                if (!cLocked){
                    canvasBreakout = this.surfaceHolder.lockCanvas();
                    cLocked = true;
                }

                if (cLocked) {
                    surfaceHolder.unlockCanvasAndPost(canvasBreakout);
                    cLocked = false;
                }
            }

            try {
                sleep(targetTime - (System.nanoTime() - startTime) / 1000000)
            } catch (ignored: Exception) {
            }
        }
    }
}