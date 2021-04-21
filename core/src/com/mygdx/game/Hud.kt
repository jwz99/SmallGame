package com.mygdx.game

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport

class Hud(sb: SpriteBatch) : Disposable {
    lateinit var stage: Stage
    private val viewport: Viewport
    private var timeCount = 0f
    private var worldTimer = 0
    private val countdownLabel: Label
    private val textLabel1: Label

    // timing
    fun update(dt: Float) {
        timeCount += dt
        if (timeCount >= 1) {
            worldTimer++
            countdownLabel.setText(java.lang.String.format("%03d", worldTimer))
            timeCount = 0f
        }
    }

    override fun dispose() {
        stage.dispose()
    }

        private var score = 0
        private lateinit var scoreLabel: Label

        fun addScore(value: Int) {
            score += value
            scoreLabel.setText(java.lang.String.format("%06d", score))
        }

    init {
        //setup the HUD viewport using a new camera seperate from camera
        //define the stage using that viewport and our games spritebatch
        viewport = FitViewport(400f, 200f, OrthographicCamera())
        stage = Stage(viewport, sb)
        val table = Table()
        //Top-Align table
        table.top()
        //make the table fill the entire stage
        table.setFillParent(true)
        //define labels
        countdownLabel = Label(java.lang.String.format("%06d", worldTimer), LabelStyle(BitmapFont(), Color.WHITE))
        textLabel1 = Label("Scores", LabelStyle(BitmapFont(), Color.WHITE))
        scoreLabel = Label(java.lang.String.format("%06d", score), LabelStyle(BitmapFont(), Color.WHITE))
        //add our labels to the table
        table.add(textLabel1).expandX().padTop(10f)
        table.defaults()
        table.add(countdownLabel).expandX()
        //add a second row to the table
        table.row()
        table.add(scoreLabel).expandX()
        //add the table to the stage
        stage.addActor(table)
    }
}

