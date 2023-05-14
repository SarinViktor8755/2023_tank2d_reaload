package com.tanks_2d.Screens.PauseScreen;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.tanks_2d.MainGame;

public class TableResult {
    Table table;

    private Skin skinMenu;
    Label nameLabel = new Label("Name:", skinMenu);


    public TableResult(MainGame mainGame) {
        table = new Table();
        table.setDebug(true);
        skinMenu = mainGame.getAMG().get("skin/uiskin.json");
    }







}
