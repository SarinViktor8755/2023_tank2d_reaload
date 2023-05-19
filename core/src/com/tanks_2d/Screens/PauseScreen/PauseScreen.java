package com.tanks_2d.Screens.PauseScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.tanks_2d.AudioEngine.AudioEngine;
import com.tanks_2d.ClientNetWork.MainClient;
import com.tanks_2d.MainGame;
import com.tanks_2d.Units.ListPlayers;
import com.tanks_2d.Units.Tanks.OpponentsTanks;

import java.util.ArrayList;

public class PauseScreen implements Screen {
    MainGame mainGame;

    private SpriteBatch batch;

    private StretchViewport viewport;
    private OrthographicCamera camera;
    private MainClient mainClient;

    private AudioEngine audioEngine;// игравой движок

    private static String game_statistics_players = ""; // строка статистики играков для паузы

    /////////////////////////////////////
    private float timeInScreen;
    private float final_time;
    Texture f;
    Texture f_bw;
    Texture tb;

    TableResult tableResult;

    private BitmapFont textFont;
//    private Label labelHP;
//    final private Stage stage;

    private static ArrayList<DataPlyerStatistic> dataPlyerStatistics = new ArrayList<>();

    public PauseScreen(MainGame mainGame, float timeInScreen) {
        //System.out.println("PAUSE ");
        audioEngine = mainGame.audioEngine;
        this.batch = new SpriteBatch();
        this.mainGame = mainGame;
        final_time = timeInScreen;

        //viewport = new StretchViewport(MainGame.WIDTH_SCREEN, MainGame.HEIGHT_SCREEN, camera);
        //viewport.apply();
        camera = new OrthographicCamera();
        viewport = new StretchViewport(MainGame.WIDTH_SCREEN, MainGame.HEIGHT_SCREEN, camera);
        viewport.apply();
        camera.position.set(camera.viewportWidth, camera.viewportHeight, 0);
        camera.update();
        this.mainClient = mainGame.getMainClient();
        f = mainGame.getAMG().get("pause_screen/bg.png", Texture.class);
        f_bw = mainGame.getAMG().get("pause_screen/bg_bw.png", Texture.class);
        tb = mainGame.getAMG().get("treck_bar.png", Texture.class);
        this.timeInScreen = timeInScreen;
        audioEngine.playMusicPaseMenu();
//
//        BitmapFont font = mainGame.getAssetManager().get("fonts/font.fnt", BitmapFont.class);
//        Label.LabelStyle style = new Label.LabelStyle(font, Color.WHITE);
//        puseTextLabel = new Label("PAUSE_GAME",style);
//        font = new BitmapFont(); //or use alex answer to use custom font
        ///////////////////////////////
        textFont = mainGame.getAMG().get("fonts/font.fnt", BitmapFont.class);
        textFont.getData().scale(0.06f);

        // Label.LabelStyle style = new Label.LabelStyle(textFont, Color.WHITE);

//
//        labelHP = new Label("HP:", style);
//        labelHP.setScale(8);

//        stage = new Stage(viewport, batch);
//        stage.addActor(labelHP);
        tableResult = new TableResult(mainGame, viewport, batch);
    }


    @Override
    public void show() {
        mainGame.audioEngine.playMusicPaseMenu();

    }

    @Override
    public void render(float delta) {
        System.out.println(viewport.getScreenWidth());
        System.out.println(viewport.getWorldWidth());
        System.out.println(viewport.getScreenX());
        System.out.println();
        update();


        //if(MathUtils.randomBoolean(.005f)) MainGame.setFlagChangeScreen((byte) MainGame.STATUS_GAME_GAMEPLAY);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        this.batch.setProjectionMatrix(camera.combined);
        camera.update();

        batch.begin();

        batch.setColor(1, 1, 1, getAlpha());

        //  System.out.println(mainGame.getScreen());
        batch.draw(f, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        batch.setColor(1, 1, 1, 1 - getWith());
        batch.draw(f_bw, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        batch.setColor(1, 1, 1, 1);
        batch.draw(tb, 0, 0, viewport.getWorldWidth() * getWith(), viewport.getWorldHeight() / 25);

        textFont.setColor(1, 1, 1, getAlpha());
        textFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        textFont.setUseIntegerPositions(true);

        textFont.draw(batch, "Player", 30, 310);

        textFont.draw(batch, "F", 230, 310);
        textFont.draw(batch, "D", 290, 310);
        textFont.draw(batch, "dmc", 350, 310);
        textFont.draw(batch, "score", 430, 310);

        DataPlyerStatistic p;
        for (int i = 0; i < PauseScreen.dataPlyerStatistics.size(); i++) {

           // if(MathUtils.randomBoolean()) textFont.setColor(Color.BLUE); else


                textFont.setColor(Color.RED);
            p = PauseScreen.dataPlyerStatistics.get(i);

            textFont.getColor().a =  getAlpha();
            ///textFont.draw(batch, convertStringLeft(PauseScreen.dataPlyerStatistics.get(i).nik, 3) + " " + PauseScreen.dataPlyerStatistics.get(i).frag + "  " + PauseScreen.dataPlyerStatistics.get(i).death + "  " + PauseScreen.dataPlyerStatistics.get(i).damage_caused + " " + PauseScreen.dataPlyerStatistics.get(i).score, 400, 1000 - (60 * i));
            //mainGame.getGamePlayScreen().getTanksOther().getTankForID()
            int y = 283 - (18 * i);

            textFont.draw(batch, (i+1) + ".", 23, y);
            textFont.draw(batch, convertStringLeft(p.nik, 10), 46, y);
            textFont.draw(batch, String.valueOf(p.frag), 230, y);
            textFont.draw(batch, String.valueOf(p.death), 290, y);
            textFont.draw(batch, String.valueOf(p.damage_caused), 350, y);
            textFont.draw(batch, String.valueOf(p.score), 430, y);
        }


        batch.end();

        //  tableResult.rander();

        // stage.draw();
        // System.out.println(timeInScreen);
        //if(timeInScreen < 0) mainGame.goGameForPause();
    }

    private String convertStringCen(String s, int length) {
        String result = s;


        return "";
    }

    private String convertStringLeft(String s, int length) { // установить длинну строки орентация слева
//        try {
        String result = s + "                                                                                           .";
     //   System.out.println(result);
        result = result.substring(0, length);
//        System.out.println(result.length());
//        System.out.println(result);
//        System.out.println();
        return result;
//               } catch (StringIndexOutOfBoundsException e) {
//            e.printStackTrace();
//            return "s";
//        }

    }

    private void update() {
        this.timeInScreen -= Gdx.graphics.getDeltaTime();
        if (timeInScreen < 0) MainGame.setFlagChangeScreen((byte) MainGame.STATUS_GAME_GAMEPLAY);
        mainGame.goGameForPause();

        //   labelHP.setText(PauseScreen.dataPlyerStatistics.get(0).nik + "\n " + PauseScreen.dataPlyerStatistics.get(0).frag+ "  " + PauseScreen.dataPlyerStatistics.get(0).death + "  " + PauseScreen.dataPlyerStatistics.get(0).damage_caused);
    }

    private float getAlpha() {
        float result = 1;
        if (timeInScreen > final_time - 2) {
            result = Interpolation.exp10Out.apply(final_time - 1 - timeInScreen);
        } else if (timeInScreen < 1.3f) {
            result = Interpolation.exp10Out.apply(timeInScreen - 1);
            audioEngine.stopMusicPaseMenu();
        }

        return result;

    }

    private float getWith() {
        float result = final_time - timeInScreen;
        result = result / final_time;
        return Interpolation.fade.apply(result);

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
        batch.dispose();
        mainGame.audioEngine.stopMusicPaseMenu();

    }

    public static String getGame_statistics_players() {
        return game_statistics_players;
    }

    public static void setGame_statistics_players(String game_statistics_players) {
        PauseScreen.game_statistics_players = game_statistics_players;
    }


    public static  void parser_result() {
        PauseScreen.getDataPlyerStatistics().clear();
        String fs = PauseScreen.getGame_statistics_players();
        PauseScreen.getGame_statistics_players();
        //System.out.println(PauseScreen.getDataPlyerStatistics());
        String[] parts = fs.split("<p>::");
        for (int i = 0; i < parts.length; i++) {
            int index = parts[i].indexOf("<_<nn");
            if (index == -1) continue;
            String nik = parts[i].substring(0, index);

            String[] p = parts[i].substring(index).split(" ");
            int frags = Integer.valueOf(p[1]);
            int deth = Integer.valueOf(p[2]);
            int hp_n = Integer.valueOf(p[3]);
            int score = Integer.valueOf(p[4]);
            int id = Integer.valueOf(p[5]);
            



            PauseScreen.getDataPlyerStatistics().add(new DataPlyerStatistic(nik, frags, deth, hp_n, score, id));


        }

    }

    public static ArrayList<DataPlyerStatistic> getDataPlyerStatistics() {
        return dataPlyerStatistics;
    }

    public static void setDataPlyerStatistics(ArrayList<DataPlyerStatistic> dataPlyerStatistics) {
        PauseScreen.dataPlyerStatistics = dataPlyerStatistics;
    }
}
