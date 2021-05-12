package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;

import java.util.ArrayList;


public class PlayScreen implements Screen
{
    //TODO: Kamera.
    private OrthographicCamera camera;

    //TODO: Menadzer zasobów.
    private AssetManager assetManager;

    //TODO: Mapa.
    private OrthogonalTiledMapRenderer mapRenderer;
    private TiledMap map;

    //TODO: Swiat fizyki 2D.
    private World world;

    private Small small;

    //TODO: Postacie w grze.

    //TODO: a) gracz

    private Player player;

    //TODO: b) monety:

    private ArrayList<Coin> coins;
    //TODO: c) wynik:
    public static int points;
    //TODO: hud
    private Hud hud;
    public PlayScreen(Small small)
    {
        //TODO: 1. Dostanie referencji do rysownika sprite'ów.
        this.small=small;
        //TODO: 2. Utworzenie kamery.
        camera=new OrthographicCamera(500,320);
        camera.position.y=160;
        camera.position.x=300;
        //TODO: 3. Utworzenie menadzera zasobow oraz zalaczenie mapy.
        assetManager=new AssetManager();
        assetManager.setLoader(TiledMap.class,new TmxMapLoader());
        assetManager.load("game_world.tmx",TiledMap.class);
        assetManager.finishLoading();
        //TODO: 3. Uzyskanie mapy oraz utworzenie rysownika mapy.
        map=assetManager.get("game_world.tmx",TiledMap.class);
        mapRenderer=new OrthogonalTiledMapRenderer(map);
        mapRenderer.setView(camera);
        //TODO: 4. Utworzenie świata fizyki 2D.
        world=new World(new Vector2(0,-65.87f),true);
        //TODO: 4. Nałożenie BOX2D na warstwę "ground" mapy.
        BodyDef bodyDef=new BodyDef();
        PolygonShape shape=new PolygonShape();
        FixtureDef fixtureDef=new FixtureDef();
        Body body;
        for(MapObject object:map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class))
        {
            Rectangle rect=((RectangleMapObject)object).getRectangle();
            bodyDef.type=BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rect.getX()+rect.getWidth()/2),(rect.getY()+rect.getHeight()/2));

            body=world.createBody(bodyDef);
            body.setUserData("Ground");

            shape.setAsBox(rect.getWidth()/2,rect.getHeight()/2);
            fixtureDef.shape=shape;
            fixtureDef.friction=0;
            body.createFixture(fixtureDef);
        }
        //TODO: 5. Utworzenie gracza.
        player=new Player(world);
        hud = new Hud(small.spriteBatch);
        //TODO: 6. Dodanie monet do gry.
        coins=new ArrayList<Coin>(100);
        for(int i=0;i<100;i++)
        {
            coins.add(new Coin(world));
        }

        //TODO: 7. Utworzenie reakcji na kolizję obiektów.
        world.setContactListener(new ContactListener()
        {
            @Override
            public void beginContact(Contact contact)
            {
                try {
                    //TODO: 1. Reakcja na kolizję gracza z monetą.
                    Player_Coin_Contact(contact);
                    //TODO: 2. Reakcja na kolizje gracza z podłożem.
                    Player_Ground_Contact(contact);
                }catch (Exception e)
                {

                }
            }

            private void Player_Ground_Contact(Contact contact)
            {
                //TODO: Jeżeli gracz dotknie podłogi to pozwól na skok.
                Body A = contact.getFixtureA().getBody();
                Body B = contact.getFixtureB().getBody();
                Object oA = A.getUserData();
                Object oB = B.getUserData();
                if(oA != null && oB !=null)
                {
                    if (oA.getClass() == Player.class && oB.getClass() == String.class)
                    {
                        Player pl = (Player) oA;
                        pl.jump=false;
                    }
                    else if (oB.getClass() == Player.class && oA.getClass() == String.class)
                    {
                        Player pl = (Player) oB;
                        pl.jump=false;
                    }
                }
            }

            private void Player_Coin_Contact(Contact contact)
            {
                //TODO: Uzyskiwanie informacji na temat kolizji obiektów.
                //TODO: 1. Jeżeli pierwszy to player oraz drugi to moneta, to zniszcz monęte.
                Body A = contact.getFixtureA().getBody();
                Body B = contact.getFixtureB().getBody();
                Object oA = A.getUserData();
                Object oB = B.getUserData();
                if(oA!=null && oB !=null)
                {
                    if (oA.getClass() == Player.class && oB.getClass() == Coin.class)
                    {
                        Coin coin = (Coin) oB;
                        coin.Destroy();
                        points++;
                        System.out.println("Points: "+points);
                        A.applyLinearImpulse(new Vector2(2,0),A.getWorldCenter(),true);
                        hud.addScore(points);
                    }
                    else if (oB.getClass() == Player.class && oA.getClass() == Coin.class)
                    {
                        Coin coin = (Coin) oA;
                        coin.Destroy();
                        points++;
                        System.out.println("Points: "+points);
                        B.applyLinearImpulse(new Vector2(2,0),A.getWorldCenter(),true);
                        hud.addScore(points);
                    }
                }
            }
            @Override
            public void endContact(Contact contact) {

            }
            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }
            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        });

        //TODO: Ustawienie punktów z gry.
        points=0;
    }

    private void DrawMap(float dt)
    {
        //TODO: Narysuj mapę.
        mapRenderer.setView(camera);
        mapRenderer.render();
    }

    private void UpdateCamera(float dt)
    {
        //TODO: Aktualizuj kamerę.
        camera.update();
        camera.position.x=player.body.getPosition().x;
    }

    private void UpdateFigures(float dt)
    {
        //TODO: Akutalizuj postacie gry.
        player.Update();
    }

    private void SetWorldTime()
    {
        //TODO: Ustawiaj parametry świata.
        world.step(1/60f,6,2);
    }

    private void ClearScreen()
    {
        //TODO: Wyczyść ekran.
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    private void DrawSprites(float dt)
    {
        //TODO: Rysowanie sprite'ów.

        //TODO: 1. Ustaw projekcie rysowanika oraz zacznij rysowanie.
        small.spriteBatch.setProjectionMatrix(camera.combined);
        small.spriteBatch.begin();

        //TODO: 2. Narysuj gracza.
        player.Draw(small.spriteBatch);

        //TODO: 3. Narysuj monety
        for(Coin c: coins)
        {
            c.Draw(small.spriteBatch);
        }

        small.spriteBatch.end();
    }


    @Override
    public void render(float delta)
    {
        //TODO: 1. Wczyść poprzednią klatkę.
        ClearScreen();

        //TODO: 2. Aktualizowanie postaci gry.
        UpdateFigures(delta);

        //TODO: 3. Aktualizowanie kamery.
        UpdateCamera(delta);

        //TODO: 4. Rysowanie mapy.
        DrawMap(delta);

        //TODO: 5. Rysowanie sprite'ów.
        DrawSprites(delta);

        //TODO: 6. Rysowanie barier fizycznych obiektów.
        //DrawBox2DRendererDebug(delta);

        //TODO: 7. Ustawianie paramterów świata.
        SetWorldTime();
        small.spriteBatch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
        hud.update(delta);
    }

    @Override
    public void resize(int width, int height)
    {

    }

    @Override
    public void pause()
    {

    }

    @Override
    public void resume()
    {

    }

    @Override
    public void hide()
    {

    }

    @Override
    public void dispose()
    {

    }

    @Override
    public void show()
    {

    }
}

