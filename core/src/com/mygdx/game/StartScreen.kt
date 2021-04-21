package com.mygdx.game

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport

class StartScreen(private val game: Game) : Screen {
    private val viewport: Viewport
    private val stage: Stage

    //TODO: Mapa.
    private val mapRenderer: OrthogonalTiledMapRenderer? = null
    var map: TiledMap? = null
    override fun show() {}
    override fun render(delta: Float) {
        if (Gdx.input.justTouched()) {
            game.screen = PlayScreen(game as Small)
            dispose()
        }
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        stage.draw()
    }

    override fun resize(width: Int, height: Int) {}
    override fun pause() {}
    override fun resume() {}
    override fun hide() {}
    override fun dispose() {
        stage.dispose()
    }

    init {
        viewport = FitViewport(1000f, 600f, OrthographicCamera())
        stage = Stage(viewport, (game as Small).spriteBatch)
        val img = Image(Texture("start_screen.png"))
        val font = LabelStyle(BitmapFont(), Color.WHITE)
        val table = Table()
        table.center()
        table.setFillParent(true)
        val label = Label("Play", font)
        table.add(label).expandX()
        stage.addActor(table)
        stage.addActor(img)
    }
}