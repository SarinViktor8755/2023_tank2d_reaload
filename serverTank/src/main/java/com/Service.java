package main.java.com;


import com.badlogic.gdx.math.MathUtils;
import com.tanks_2d.ClientNetWork.Heading_type;

public class Service {
    public static boolean invertBoolean(boolean v) {
        if (v) v = false;
        else v = true;
        return v;
    }

    public static boolean invertBooleanRandom(boolean v, float chance) {
        if (v && MathUtils.randomBoolean(chance)) v = false;
        else v = true;
        return v;
    }


    public static String getComandFoeNomber(int n) {
        if (Heading_type.BLUE_COMMAND == n) return "BLUE";
        if (Heading_type.RED_COMMAND == n) return "RED";
        return "Nan";
    }


}