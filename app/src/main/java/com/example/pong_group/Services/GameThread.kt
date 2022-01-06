package com.example.pong_group.Services

import android.os.Build
import android.view.SurfaceHolder
import androidx.annotation.RequiresApi
import com.example.pong_group.Model.GameViewBreakout
import com.example.pong_group.Services.GameSettings.curCanvas
//import com.example.pong_group.Model.GameViewBreakout.Companion.canvasBreakout

import java.util.*

class GameThread(
    private val surfaceHolder: SurfaceHolder,
    private val gameView: GameViewBreakout
) : Thread() {

    private val targetFPS = 60
    var running: Boolean = false

    init {
        name = "GameThread"
    }

    @RequiresApi(Build.VERSION_CODES.M)
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

