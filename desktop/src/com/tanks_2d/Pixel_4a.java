package com.tanks_2d;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class Pixel_4a {
    public static void main (String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setForegroundFPS(60);
        config.useVsync(false);
        config.setTitle("TanksPVP_2d");
        config.setWindowedMode(2340/3 ,1080/3);
        new Lwjgl3Application(new MainGame(1), config);
    }
}
