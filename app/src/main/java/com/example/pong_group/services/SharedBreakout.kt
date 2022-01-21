package com.example.pong_group.services

import com.example.pong_group.controller.prefs
import com.example.pong_group.model.Brick
import kotlin.properties.Delegates


//singleton class for manage breakout games and share game settings through the game
object SharedBreakout {
    var bricks = mutableListOf<Brick>()
    var brickCountX: Int = 15
    var brickCountY: Int = 0
    var highScoreBroken = false

    var ballSpeedStart = 10f

    //settings for classic game breakout
    var ballSpeed = ballSpeedStart
    private const val speedIncrease = 4f
    var hits = 0
    private var orangeHit = false
    private var redHit = false
    var upperWallHit = false
    var maxSpeedAchieved = false

    //non-classic version
    var brickCounts = mutableListOf<Int>()
    var lowestBrick by Delegates.notNull<Int>()
    var totalRows by Delegates.notNull<Int>()


    //setting up bricks for the game depend on settings
    fun gameSetUpBreakout() {
        //brickCounts.clear()
        for (i in 0 until brickCountY)
            if (i < brickCounts.size)
                brickCounts[i] = brickCountX
            else
                brickCounts.add(brickCountX)
        lowestBrick = brickCountY - 1
        totalRows = brickCountY
    }


    //check bricks around for fixing bug when ball can destroy two brick at once
    fun checkSurroundings(nr: Int) {
        var onEdge: Boolean

        //right of
        if (nr + 1 < brickCountX * brickCountY) {
            onEdge = checkEdge(nr + 1, true)
            if (!onEdge) {
                bricks[nr + 1].exL = true
            }
        }

        //left of
        if (nr - 1 >= 0) {
            onEdge = checkEdge(nr - 1, false)
            if (!onEdge) {
                bricks[nr - 1].exR = true
            }
        }

        //above
        if (nr - brickCountX >= 0) {
            bricks[nr - brickCountX].exB = true
        }

        //below
        if (nr + brickCountX < brickCountX * brickCountY) {
            bricks[nr + brickCountX].exT = true
        }
    }

    private fun checkEdge(nr: Int, right: Boolean): Boolean {
        if (right) {
            for (i in 0..brickCountX * brickCountY step brickCountX) {
                if (nr == i)
                    return true
            }
        } else {
            for (i in brickCountX - 1..brickCountX * brickCountY step brickCountX) {
                if (nr == i)
                    return true
            }
        }
        return false
    }

    //adding score if brick was broken
    fun addScore(pointBase: Int) {
        if (prefs.isClassicInterface || prefs.isInfiniteLevels)
            GameSettings.scoreBreakout += pointBase
        else {
            GameSettings.scoreBreakout += pointBase
            if (pointBase + lowestBrick != totalRows)
                GameSettings.scoreBreakout += lowestBrick * brickCounts[lowestBrick]
            brickCounts[pointBase]--
            while (brickCounts[lowestBrick] == 0 && lowestBrick > 0)
                lowestBrick--
        }
        GameSettings.updateScoreBreakout()
    }

    //updating speed during the game
    fun updateSpeedClassic(brickColor: Int) {
        if (hits <= 12)
            hits++
        if (hits == 4 || hits == 12)
            ballSpeed += speedIncrease
        if (brickColor == 5 && !orangeHit) {
            ballSpeed += speedIncrease
            orangeHit = true
        }
        if (brickColor == 7 && !redHit) {
            ballSpeed += speedIncrease
            redHit = true
        }
        if (ballSpeed >= ballSpeedStart + speedIncrease * 4) {
            ballSpeed = ballSpeedStart + speedIncrease * 4
            maxSpeedAchieved = true
        }
    }
}