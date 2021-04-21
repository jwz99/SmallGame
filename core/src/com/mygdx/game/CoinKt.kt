package com.mygdx.game

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.physics.box2d.*
import java.util.*

class Coin(
//TODO: Swiat fizyki 2D.
        private val world: World) {

    //TODO: Ciało fizyczne obiektu 2D.
    lateinit var body: Body
    //TODO: Obrazek.
    private lateinit var sprite: Sprite

    //TODO: Obiekt ma być renderowany.
    private var render = false
    private fun CreateCoin() {
        //TODO: 1. Utworzenie obiektu do generowania randomowych pozycji coinów.
        val rand = Random()

        //TODO: 2. Nałożenie BOX2D na postać.
        val bodyDef = BodyDef()
        bodyDef.position[rand.nextInt(8000).toFloat()] = rand.nextInt(200) + 100.toFloat()
        bodyDef.type = BodyDef.BodyType.KinematicBody
        body = world.createBody(bodyDef)
        val fixtureDef = FixtureDef()
        fixtureDef.isSensor = true
        val shape = CircleShape()
        shape.radius = 15f
        fixtureDef.shape = shape
        body.createFixture(fixtureDef)

        //TODO: 2. Nadawanie tekstury.
        sprite = Sprite(Texture("Coin.png"))
        sprite.setOrigin(sprite.width / 2, sprite.height / 2)

        //TODO: 3. Ustawienie danych ciała.
        body.setUserData(this)
        //TODO: 4. Ustawienie, żeby obiekt był rysowany.
        render = true
    }

    fun Destroy() {
        //TODO: Zniszczenie obiektu.
        render = false
        world.step(1 / 60f, 2, 3)
        world.destroyBody(body)
    }

    fun Draw(batch: SpriteBatch?) {
        //TODO: Rysowanie obiektu, jeżeli ma być narysowany.
        if (render) {
            sprite.rotation = sprite.rotation + rand.nextFloat()
            sprite.setPosition(body.position.x - sprite.width / 2, body.position.y - sprite.height / 2)
            sprite.draw(batch)
        }
    }

    companion object {
        //TODO: Randomowe pozycje.
        private val rand = Random()
    }

    init {
        //TODO: 1. Uzyskanie referencji do świata, do którego będzie należała moneta.
        //TODO: 2. Utworzenie monety.
        CreateCoin()
    }
}