package com.example.pong_group.Services


import android.util.Log
import android.view.SurfaceHolder
import com.example.pong_group.Model.GameViewBreakout
import com.example.pong_group.Services.GameSettings.curCanvas


class GameThread(
    private val surfaceHolder: SurfaceHolder,
    private val gameView: GameViewBreakout
) : Thread() {

    private val targetFPS = 60
    var running: Boolean = false

    init {
        name = "GameThread"
    }

    override fun run() {
        var startTime: Long
        val targetTime = (1000 / targetFPS).toLong()
        var cLocked = false

        while (running) {
            startTime = System.nanoTime()
            try {
                curCanvas = surfaceHolder.lockCanvas()?.also {
                    cLocked = true
                    synchronized(surfaceHolder) {
                        if (gameView.pause) {
                            running = false
                        }
                        gameView.update()
                        gameView.draw(it)
                    }
                    gameView.checkEndOfTheGame()

                    surfaceHolder.unlockCanvasAndPost(it)

                    cLocked = false

                }!!
            } catch (ignored: Exception) {

                Log.d("ERROR", "$ignored")
                if (!cLocked) {
                    curCanvas = this.surfaceHolder.lockCanvas();
                    cLocked = true;
                }
                if (cLocked) {
                    surfaceHolder.unlockCanvasAndPost(curCanvas);

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

