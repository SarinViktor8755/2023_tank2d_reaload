package com.tanks_2d.Screens.PauseScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.tanks_2d.MainGame;

public class TableResult {
    Table table;

    Stage stage;
    private Skin skinMenu;
    Label nameLabel;

    SpriteBatch spriteBatch;
    StretchViewport stretchViewport;


    public TableResult(MainGame mainGame, StretchViewport viewport, SpriteBatch batch) {
//        this.spriteBatch = batch;
//        this.stretchViewport = viewport;
        table = new Table();
        stage = new Stage(viewport,batch);



        table.setDebug(true);
        skinMenu = mainGame.getAMG().get("skin/uiskin.json");
        nameLabel = new Label("Name:", skinMenu);
        stage.addActor(table);
        generate_table();

    }

    private void generate_table(){
//        Skin skin = new Skin();
//        skin = mainGame.getAMG().get("skin/uiskin.json");
        nameLabel = new Label("primer",skinMenu);
        table.add(nameLabel);

        table.add(nameLabel);
        table.row();
        table.add(nameLabel);
        table.add(nameLabel);
    }


     public void rander(){
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
     }







}
