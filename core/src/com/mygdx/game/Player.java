package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class Player
{
    //TODO: Swiat fizyki 2D.
    private World world;

    //TODO: Ciało fizyczne obiektu 2D.
    public Body body;

    //TODO: Szybkość poruszania się.
    public float speed;

    //TODO: Obrazek.
    private Sprite sprite;

    //TODO: Obiekt ma być renderowany.
    private boolean render;

    //TODO: Skok.
    public boolean jump=false;

    public Player(World world)
    {
        //TODO: 1. Uzyskanie referencji do świata, do którego będzie należał gracz.
        this.world=world;
        //TODO: 2. Utworzenie gracza.
        CreatePlayer();
    }

    private void CreatePlayer()
    {
        //TODO: 1. Nałożenie BOX2D na postać.
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(300,100);
        bodyDef.type=BodyDef.BodyType.DynamicBody;
        body=world.createBody(bodyDef);

        FixtureDef fixtureDef=new FixtureDef();
        CircleShape shape=new CircleShape();
        shape.setRadius(10);

        //TODO: 2. Ustawianie sprężystości obiektu.
        fixtureDef.shape=shape;
        fixtureDef.density=0.0f;
        fixtureDef.restitution=0.1f;

        body.createFixture(fixtureDef);

        //TODO: 3. Ustawienie prędkości obiektu.
        speed=300f;

        //TODO: 4. Nadawanie tekstury.
        sprite=new Sprite(new Texture("Ball.png"));
        sprite.setOrigin(sprite.getWidth()/2,sprite.getHeight()/2);

        //TODO: 5. Ustawienie danych ciała.
        body.setUserData(this);

        //TODO: 6. Ustawienie, żeby obiekt był rysowany.
        render=true;
    }

    public void Update()
    {
        //TODO: Kieruj graczem ~ kliknięcie/ dotknięcie ekran powoduje skok.
        if(Gdx.input.isTouched())
        {
            if(!jump)
            {
                body.applyLinearImpulse(new Vector2(body.getLinearVelocity().x,1500),body.getWorldCenter(),true);
                jump=true;
            }
        }
        if(body.getLinearVelocity().x<70)
        {
            body.applyLinearImpulse(new Vector2(100,0),body.getWorldCenter(),true);
        }

        //TODO: Usuniemy gracza jeżeli naciśniemy klawisz ESCAPE.
        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE))
        {
            Destroy();
        }
    }

    public void Destroy()
    {
        //TODO: Zniszczenie obiektu.
        world.destroyBody(body);
        render=false;
    }

    public void Draw(SpriteBatch batch)
    {
        //TODO: Rysowanie obiektu, jeżeli ma być narysowany.
        if(render)
        {
            sprite.setRotation(sprite.getRotation()+0.5f);
            sprite.setPosition(body.getPosition().x - sprite.getWidth() / 2, body.getPosition().y - sprite.getHeight() / 2);
            sprite.draw(batch);
        }
    }
}
