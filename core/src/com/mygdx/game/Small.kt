package com.mygdx.game

import com.badlogic.gdx.Game
import com.badlogic.gdx.graphics.g2d.SpriteBatch

class Small : Game() {
    lateinit var spriteBatch: SpriteBatch
    override fun create() {
        //TODO: Ustawianie sprite batch oraz nowego ekranu.
        spriteBatch = SpriteBatch()
        setScreen(StartScreen(this))
    }
}