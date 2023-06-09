package com.tanks_2d.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.tanks_2d.AudioEngine.AudioEngine;
import com.tanks_2d.ClientNetWork.Heading_type;
import com.tanks_2d.ClientNetWork.MainClient;
import com.tanks_2d.ClientNetWork.RouterSM;
import com.tanks_2d.Locations.GameSpace;
import com.tanks_2d.MainGame;
import com.tanks_2d.Units.NikName;
import com.tanks_2d.Units.Tanks.Tank;
import com.tanks_2d.Shaders.ShaderFilm;
import com.tanks_2d.Shaders.Shaders;

import java.io.IOException;


public class MenuScreen implements Screen {

    // ShaderFilm shaderFilm;
    private MainGame mainGame;

    private SpriteBatch batch;
    private StretchViewport viewport;
    private OrthographicCamera camera;
    private MainClient mainClient;

    private Texture wallpaper;
    private Texture wallpaper1;
    private Texture logo;
    private Texture disconnect;

    private float timeInScreen;
    private float timerStartGame; // переменная для анимации

    private boolean startgameMP; // флаг старта мультиплеерной игры
    private boolean startgameSP; // флаг старта диночной игры

    public Label statusConnetct;



//    public Label singelGame;

    //////////////
    private Stage stageMenu;
    private Skin skinMenu;
    private Skin textEditer;

    TextButton textButton;
    CheckBox checkBoxMusic;
    CheckBox checkBoxSound;
    //  TextButton singelGame;

    private boolean button_start_click;


    FloatArray dummyArray = new FloatArray();
    String limit = "";

    private float red_alha;


    public MenuScreen(final MainGame mainGame) {
        mainGame.getAMG().loadedAseets();
        mainGame.audioEngine = new AudioEngine(mainGame);

        red_alha = 0;

        button_start_click = false;

        this.mainGame = mainGame;
        timeInScreen = 0;
        timerStartGame = 0;
        startgameMP = false;

        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new StretchViewport(MainGame.WIDTH_SCREEN, MainGame.HEIGHT_SCREEN, camera);

        viewport.apply();

        // viewport.apply(true);

        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();

        mainClient = mainGame.getMainClient();
        stageMenu = new Stage(viewport);

        wallpaper = mainGame.getAMG().get("menuAsset/wallpaper.png", Texture.class);
        wallpaper1 = mainGame.getAMG().get("menuAsset/wallpaper1.png", Texture.class);
        logo = mainGame.getAMG().get("menuAsset/logo.png", Texture.class);
        disconnect = mainGame.getAMG().get("menuAsset/disconct.png", Texture.class);

        stageMenu = new Stage(viewport);

        skinMenu = mainGame.getAMG().get("skin/comic-ui.json");
        textEditer = mainGame.getAMG().get("skin/uiskin.json");
        //textEditer.getColor("").set(1,1,1,1);

        final TextField textField = new TextField(limit, textEditer);
      //  textField.setColor(1,1,1,1);

        textField.setMaxLength(20);
        textField.setWidth(280);
        textField.setPosition(20, 250);
        textField.setText(NikName.getNikName());

        statusConnetct = new Label("", textEditer);
        statusConnetct.setPosition(160, 120);
        statusConnetct.setFontScale(1.2f);


        textButton = new TextButton("Play Game", skinMenu);
        checkBoxMusic = new CheckBox("  Music ", skinMenu);
        checkBoxSound = new CheckBox("  Sound", skinMenu);

        textButton.setPosition(300, 50);
/////////////////////////////////////////////
        checkBoxMusic.setPosition(350, 180);
        checkBoxMusic.setChecked(true);
        checkBoxSound.setPosition(350, 240);
        checkBoxSound.setChecked(true);

        textButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                mainGame.getMainClient().getNetworkPacketStock().toSendMyTokkenAndNikName();
           //     if (Tank.getMy_Command() == -1) Tank.setMy_Command(Tank.generateCommand());
            //    mainGame.getMainClient().getNetworkPacketStock().toSendButtonStartClick();
//                if(!mainGame.getMainClient().isConnect()) return false; else {
//                    try {
//                        mainGame.getMainClient().getClient().reconnect();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }

                if (!button_start_click) {
                    mainGame.audioEngine.pley_pip();
                    if (!mainClient.getClient().isConnected()) {
                        try {
                            mainClient.getClient().reconnect();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            return false;
                        }
                    }
                    NikName.setNikName(textField.getText());
                    MainGame.nik_name = textField.getText();
                    mainGame.audioEngine.rady_for_action();


                    mainClient.getNetworkPacketStock().toSendButtonStartClick();
                    button_start_click = true;


                  //  mainGame.getMainClient().getNetworkPacketStock().toSendMyNik();

                     mainGame.getMainClient().getNetworkPacketStock().toSendMyTokkenAndNikName();

                }
                startgameMP = true;

                //System.out.println(textField.getText());
                if (!mainClient.getClient().isConnected()) {
                    try {
                        mainClient.getClient().reconnect();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return false;
                }
                //  mainGame.assetsManagerGame.loadAllAsseGame();


                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (!mainGame.getMainClient().getClient().isConnected()) return;

                //System.out.println("StartGameDown");
                if (!mainClient.getClient().isConnected()) return;
                if (!button_start_click) {
                    //  mainClient.getNetworkPacketStock().toSendButtonStartClick();
                    button_start_click = true;

                }
                if (!mainClient.getClient().isConnected()) return;
                startgameMP = true;


            }
        });

//        singelGame.addListener(new InputListener() {
//            @Override
//            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
//                System.out.println("touchDown");
//
//                return true;
//            }
//
//            @Override
//            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
//                System.out.println("touchUp");
//                startgameSP = true;
//                mainGame.assetsManagerGame.loadAllAsseGame();
//                NikName.setNikName(textField.getText());
//
//            }
//        });


//        singelGame.setX(350);
//        singelGame.setY(120);

        stageMenu.addActor(textButton);
        stageMenu.addActor(textField);
        stageMenu.addActor(checkBoxMusic);
        stageMenu.addActor(checkBoxSound);

        //  stageMenu.addActor(singelGame);
        stageMenu.addActor(statusConnetct);

        Gdx.input.setInputProcessor(stageMenu);
        mainGame.audioEngine.playMusicPaseMenu();
    }

    @Override
    public void show() {
        mainGame.audioEngine.stopSoundOfTracks();
        mainGame.audioEngine.playMusicPaseMenu();
        //      shaderFilm = new ShaderFilm();
        //Shaders s = new Shaders(batch);

    }

    @Override
    public void render(float delta) {
        // new Shaders(batch);
        //    System.out.println(RouterSM.map_math);
//        System.out.println(viewport.getWorldHeight());
//        System.out.println(viewport.getScreenHeight());
//        System.out.println();
        //   mainGame.audioEngine.stopSoundOfTracks();


//        shaderFilm.setGrayScaleExtraAmount(MathUtils.random(0,.5f));
//        shaderFilm.start(.2f);
//        batch.setShader(shaderFilm);

        check_screen_flag();
        upDateScreen();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        this.camera.update();
        this.batch.setProjectionMatrix(camera.combined);
        this.batch.begin();

//        System.out.println(timeInScreen);
//        System.out.println(viewport.getScreenX());
//        System.out.println(viewport.getScreenY());
//        System.out.println(camera.position);
//       // System.out.println(viewport.getScreenY());
//        System.out.println(0 + ((MathUtils.sin(timeInScreen) + 1) / 2) * 20);
//        System.out.println();


        batch.setColor(1 - timerStartGame, 1 - timerStartGame, 1 - timerStartGame, 1);


        //batch.draw(wallpaper1,0,0,camera.viewportWidth, camera.viewportHeight,1,2,(int)camera.viewportWidth,(int) camera.viewportHeight,false,false);
        //System.out.println((MathUtils.sin(timeInScreen) + 1)/2);

        batch.setColor(1 - timerStartGame, 1 - timerStartGame - red_alha, 1 - red_alha, 1);
        batch.draw(wallpaper1, viewport.getScreenX(), viewport.getScreenY() + ((MathUtils.sin(timeInScreen) + 1) / 2) * 20);
        batch.setColor(1 - timerStartGame, 1 - timerStartGame, 1, 1);
        batch.setColor(1 - timerStartGame, 1 - timerStartGame - red_alha/2, 1 - red_alha/2, 1);
        batch.draw(wallpaper, viewport.getScreenX(), viewport.getScreenY() - ((Interpolation.bounce.apply((MathUtils.sin(timeInScreen) + 1) / 2) * 10)));


        batch.setColor(1, 1, 1, 1);
        if (!mainClient.getClient().isConnected()) {
            //batch.setColor(1 - timerStartGame, 1 - timerStartGame, 1,(MathUtils.sin(timeInScreen) +.5f));
            batch.draw(disconnect, viewport.getScreenX() + 50, 60 + viewport.getScreenY() + ((MathUtils.sin(timeInScreen) + 1) / 2) * 20, 150, 150);
        }

        for (int i = 0; i < 7; i++) {
            batch.setColor(1, 1 - (.2f * i), 1, 1 - (.2f * i));
            batch.draw(logo, viewport.getScreenX(), viewport.getScreenY() + 14 + ((MathUtils.cos((timeInScreen * 3) - (7 * i)) + 1) / 2) * 20);

        }
        batch.setColor(1, 1, 1, 1);
        batch.draw(logo, viewport.getScreenX(), viewport.getScreenY() + 14 + ((MathUtils.cos(timeInScreen * 3) + 1) / 2) * 20);

        this.batch.end();
        stageMenu.draw();
        stageMenu.getRoot().setColor(1, 1, 1, 1 - timerStartGame);
    }

    private void check_screen_flag() {
        byte screen = MainGame.getFlagChangeScreen();
        if (screen == Heading_type.PAUSE_GAME) mainGame.goMenuForPause();

    }

    private void upDateScreen() {
        red_alpha_update();

        mainClient.checkConnect(Heading_type.IN_MENU); // проверяет на коннект переподключется

        // System.out.println("!!!" + mainClient.getClient().isConnected() + " ___ isOnLine" + mainGame.getMainClient().isOnLine());
        // mainGame.getMainClient().updateAlphaW();
        mainGame.updateClien();
        // statusConnetct.setText(mainClient.getClient().isConnected() +"");
        this.timeInScreen = Gdx.graphics.getDeltaTime() + this.timeInScreen;
        if (mainClient.getClient().isConnected()) {
            //   System.out.println(MathUtils.sin(Gdx.graphics.getDeltaTime()));
            // statusConnetct.setText("Ping :" + mainClient.getPing());
            statusConnetct.setColor(0, 0, 0, 1);
            textButton.setText("Play Game");

            MainGame.nik_name = NikName.getNikName();

            statusConnetct.setText("");
        } else {
            statusConnetct.setText("Server:disconnect");
            statusConnetct.setColor(Color.RED);
            // textButton.setText("Connect");
        }

        if (startgameMP || startgameSP) {
            // System.out.println(1-timerStartGame);
            mainGame.audioEngine.update_volme_pause(1 - timerStartGame);
            timerStartGame += Gdx.graphics.getDeltaTime(); // задержка во воремени для анимации
        }

        if (timerStartGame > 1) {
            if (RouterSM.map_math == null) {
               // mainGame.getMainClient().getNetworkPacketStock().toSendButtonStartClick();
               // timerStartGame = 0;
                return;
            }

            mainGame.audioEngine.stopMusicPaseMenu();

          //  startgameMP = true;
            if (startgameMP) mainGame.startGameMPley();
            if (startgameSP) mainGame.startGameSPley();


        }
        // System.out.println(timerStartGame);
        // if (timeInScreen > 3.2f) mainGame.startGamePley();
        // kefMashtab = Interpolation.bounceOut.apply(MathUtils.sin(timeInScreen) + 1);
        //    System.out.println(kefMashtab);
        //System.out.println(mainClient.getClient().isConnected());
    }

    @Override
    public void resize(int width, int height) {
        this.viewport.update(width, height, true);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        try {
            batch.dispose();
            stageMenu.dispose();
            wallpaper.dispose();
            logo.dispose();
        } catch (IllegalArgumentException e) {
        }
    }

    public void setStartgameMP(boolean startgame) {
        this.startgameMP = startgame;
    }

    private void red_alpha_update() {
        this.red_alha -= Gdx.graphics.getDeltaTime() / 3;
        this.red_alha =MathUtils.clamp(red_alha,0,1);
        if(!mainGame.getMainClient().getClient().isConnected())red_alha =1;
    }

    public void aplha_add(){
        if(red_alha > 0 ) return;
        red_alha += .3f;
    }

    public void setStartgameSP(boolean startgame) {
        this.startgameSP = startgame;
    }


}
