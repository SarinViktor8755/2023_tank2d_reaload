package com.tanks_2d.Locations;

import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;

public class MapsList {
    ArrayList<String> mapsList;

    public MapsList() {
        this.mapsList = new ArrayList<>();
        mapsList.add("field");
        mapsList.add("desert");
        mapsList.add("wasteland");
        mapsList.add("fourth");

    }

    public static String getMapForServer() {
        return getMapForServer("");
    }

    public static String getMapForServer(String except_map) {
        ArrayList<String> ml;
        String map;
        ml = new ArrayList<String>();
        ml.add("field");
        ml.add("desert");
        ml.add("wasteland");
        ml.add("fourth");
        for (; ; ) {
            map = ml.get(MathUtils.random(ml.size() - 1));
            if (!map.equals(except_map)) return map;
        }
    }


}
