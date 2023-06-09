package com.tanks_2d.Units.Tanks;

import static com.tanks_2d.Units.Tanks.Tank.getMy_Command;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.tanks_2d.ClientNetWork.Heading_type;
import com.tanks_2d.ClientNetWork.Network;
import com.tanks_2d.Screens.GamePlayScreen;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class TanksOther { /// много танков )))

    private BitmapFont textFont;

    HashMap<Integer, OpponentsTanks> listOpponents;  /// инт танка

    HashMap<Integer, Float> listSled;
    HashMap<Integer, Vector2> deltaSledVec; // слкды танков
    ArrayList<TempPoint> points; // временные точки

    GamePlayScreen gsp;

    Texture img;
    Texture img_1;

    Texture imgB;
    Texture img_1B;

    Texture img2;
    Texture imgB2;

    Texture b, tower;

    Vector2 temp;
    Vector2 tRotation;

    BotAdmin botAdmin;


    public TanksOther(GamePlayScreen gsp) {
        this.gsp = gsp;

        img = gsp.getAMG().get("trb1.png");
        img2 = gsp.getAMG().get("trb2.png");

        img_1 = gsp.getAMG().get("tr.png");
//////////////
        imgB = gsp.getAMG().get("tbb1.png");
        imgB2 = gsp.getAMG().get("tbb2.png");

        img_1B = gsp.getAMG().get("tb.png");


        textFont = new BitmapFont();
        textFont.setColor(Color.WHITE);
        textFont.setUseIntegerPositions(true);
        this.listOpponents = new HashMap<>();
        this.listSled = new HashMap<>();

        temp = new Vector2(0, 0);
        tRotation = new Vector2(0, 0);
        deltaSledVec = new HashMap<>();


//        for (int i = 0; i < 0; i++) {
//            int nom = MathUtils.random(100, 1000);
//            OpponentsTanks r = new OpponentsTanks(
//                    new Vector2(MathUtils.random(50, gsp.getGameSpace().WITH_LOCATION), MathUtils.random(50, gsp.getGameSpace().HEIHT_LOCATION)),
//                    new Vector2(MathUtils.random(.5f, 1f), MathUtils.random(.5f, 1f)),
//                    MathUtils.random(RED_COMMAND, BLUE_COMMAND), true,
//                    nom, listOpponents, gsp
//            );
//            listOpponents.put(nom, r);
//            listSled.put(nom, .0f);
//        }


    }

    public void clearListOponent() {
        this.listOpponents.clear();
    }

    public void setTankPosition(Network.PleyerPositionNom p, Boolean newFrame) {  /// ЛОГИКА ПЕРЕМЕЩЕНИЯ ТАНКА БЕЗ СИГНАЛА
        if (listOpponents.containsKey(p.nom)) { //Если такой опоннет есть - работаем с ним - нет - сощдаем новый
            OpponentsTanks ot = listOpponents.get(p.nom);
            /////////////

            // есть фрейм - тогда смещаем танк к реальной точке перемещения

            temp.set(p.xp, p.yp);
            tRotation.set(p.xp, p.yp).sub(ot.getPosition());

            float delta = tRotation.angleDeg() - ot.getDirection().angleDeg();
            if (Math.abs(delta) > 10) {
                delta = MathUtils.map(-360, 360, -1, 1, delta);
            }
            ot.getDirection().setAngleRad(delta);
/////////////////////////////////
            if (temp.dst2(ot.getPosition()) > 500) ot.getPosition().set(p.xp, p.yp);
            else
                ot.getPosition().add(temp.sub(ot.getPosition().cpy()).scl(Gdx.graphics.getDeltaTime() * 10));
////////////////////////////////
            float rotation = tRotation.set(p.xp, p.yp).cpy().sub(ot.getPosition()).angleDeg();

            if (temp.set(p.xp, p.yp).sub(ot.getPosition().cpy()).scl(10).len2() > 50)
                ot.move = true;
            else ot.move = false;
            // System.out.println("!!!! " + ot.getDirection() + rotation );
            ot.getDirection().setAngleDeg(rotation);
            ot.getDirection_tower().setAngleDeg(p.roy_tower);
            if (ot.move)
                listSled.put(p.nom, listSled.get(p.nom) + Gdx.graphics.getDeltaTime()); /// СЛЕДЫ
        } else {
            try {
                //  createOponent(p.xp, p.yp, p.nom, p.roy_tower); // создаем новый !!!!!!!
            } catch (NullPointerException e) {

            }
        }
    }

    /////////////
    public int createOponent(float x, float y, int nomer, float rotation) {
        OpponentsTanks r = new OpponentsTanks(new Vector2(x, y), new Vector2(1, 0), 0, true, nomer, listOpponents, gsp);
        listOpponents.put(nomer, r);
        listSled.put(nomer, .0f);
        return nomer;
    }


    public OpponentsTanks getRandomPlayer() {
        try {
            OpponentsTanks ot;
            do {
                ot = getTankForID((int) listOpponents.keySet().toArray()[MathUtils.random(listOpponents.size())]);
            } while (ot.getPosition().x < 0);

            //   if(getRandomPlayer().getPosition().x < -100) getRandomPlayer();
            // System.out.println(ot.getPosition() + "!!!!!-___!!!!!!!");
            return ot;

        } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException | NullPointerException e) {
            e.printStackTrace();
            return null;
        }

    }

    public void deathAllPlayers() {
        for (Map.Entry<Integer, OpponentsTanks> tanks : listOpponents.entrySet())
            if (MathUtils.randomBoolean()) tanks.getValue().getPosition().set(-1000, -1000);
    }

    public void delPlayer(int id) {
        this.listOpponents.remove(id);
    }


    private boolean inCanMove(int x, int y) {
        gsp.getGameSpace().checkMapBorders(x, y);
        return true;
    }

    private void updateColor(OpponentsTanks t, float dt) {
        if (t.getCommand() != 0 && t.color < 1) t.color = t.color + dt;
    }

    public void createFuelTank(int id) { // создание пустой балвани
        listOpponents.put(id, new OpponentsTanks(new Vector2(-8000, -8000), new Vector2(0, 0), 0, true, id, listOpponents, gsp));

    }


    public void count_living() { // посчитать живых играко и всего сколько
        if (MathUtils.randomBoolean()) return;
        OpponentsTanks t;
        int r = 0;
        int b = 0;

        int rl = 0;
        int bl = 0;
        if (getMy_Command().equals(Heading_type.RED_COMMAND)) {
            r++;
            if (gsp.getTank().isLive()) rl++;
        } else if (getMy_Command().equals(Heading_type.BLUE_COMMAND)) {
            b++;
            if (gsp.getTank().isLive()) bl++;
        }
        for (Map.Entry<Integer, OpponentsTanks> tank : this.listOpponents.entrySet()) {
            t = tank.getValue();
            if (t.getCommand().equals(Heading_type.RED_COMMAND)) {
                r++;
                if (t.isLive()) rl++;
            } else if (t.getCommand().equals(Heading_type.BLUE_COMMAND)) {
                b++;
                if (t.isLive()) bl++;
            }
        }

        gsp.setLive_red_command(rl);
        gsp.setLive_blue_command(bl);
    }


    private void print_nickname(SpriteBatch sb, OpponentsTanks t) { // печатает ник -если нет то отправляет запрос
        if (t.getNikPlayer() != null) {
            if (!gsp.getTank().isLive())
                textFont.draw(sb, t.getNikPlayer() + " (" + t.hp + ")", t.getPosition().x - t.getNikPlayer().length() * 4 - 16, t.getPosition().y + 50);
            else
                textFont.draw(sb, t.getNikPlayer(), t.getPosition().x - t.getNikPlayer().length() * 5, t.getPosition().y + 50);
        }
        try {
            if (t.getNikPlayer().equals(Heading_type.DEFULT_NAME) && (MathUtils.randomBoolean(0.1f))) {
                gsp.getMainGame().getMainClient().getNetworkPacketStock().toSendMyParPlayer(t.getNomder());

            }
        } catch (NullPointerException e) {
            // e.printStackTrace();
        }


    }

    public void randerOtherTanks(SpriteBatch sb) {
        count_living();  // посчитать живых играко и всего сколько
        OpponentsTanks t;
        //   System.out.println("--------");
        for (Map.Entry<Integer, OpponentsTanks> tank : this.listOpponents.entrySet()) {
            t = tank.getValue();

            if (!tank.getValue().isLive()) continue;
            gsp.pc.generatorSmoke(tank.getValue().hp, t.getPosition().x, t.getPosition().y);
            updateColor(t, Gdx.graphics.getDeltaTime());
            print_nickname(gsp.getBatch(), t); // печатает ник - если нет то отправляет запрос
            // t.update(Gdx.graphics.getDeltaTime());


            if (t.getCommand() == Heading_type.RED_COMMAND) {
                if (!t.move) b = img;
                else if (MathUtils.sin(gsp.getTimeInGame() * 50) >= 0) b = img;
                else b = img2;
                tower = img_1;
            } else {
                // b = imgB;
                if (!t.move) b = imgB;
                else if (MathUtils.sin(gsp.getTimeInGame() * 50) >= 0) b = imgB;
                else b = imgB2;
                tower = img_1B;
            }

            sb.setColor(0, 0, 0, MathUtils.clamp(t.getColor(), 0, .5f));
            sb.draw(b,
                    t.getPosition().x - 17, t.getPosition().y - 17,
                    20, 20,
                    40, 40,
                    1, 1,
                    t.getDirection().angleDeg() + 180,
                    0, 0,
                    img.getWidth(), img.getHeight(),
                    true, false);
            sb.setColor(t.getColor(), t.getColor(), t.getColor(), t.getColor());
            sb.draw(b,
                    t.getPosition().x - 20, t.getPosition().y - 20,
                    20, 20,
                    40, 40,
                    1, 1,
                    t.getDirection().angleDeg() + 180,
                    0, 0,
                    img.getWidth(), img.getHeight(),
                    true, false);

            sb.setColor(0, 0, 0, MathUtils.clamp(t.getColor(), 0, .5f));
            sb.draw(tower,
                    t.getPosition().x - 16, t.getPosition().y - 16,
                    16, 16,
                    40, 40,
                    1, 1,
                    t.getDirection_tower().angleDeg() + 180,
                    0, 0,
                    img.getWidth(), img.getHeight(),
                    false, false);

            sb.setColor(t.getColor(), t.getColor(), t.getColor(), t.getColor());
            sb.draw(tower,
                    t.getPosition().x - 20, t.getPosition().y - 20,
                    20, 20,
                    40, 40,
                    1, 1,
                    t.getDirection_tower().angleDeg() + 180,
                    0, 0,
                    img.getWidth(), img.getHeight(),
                    false, false);

            addSled(t);

        }
        sb.setColor(1, 1, 1, 1);
    }

    //
    public void send_all_layer_live_100_hp() {
        for (Map.Entry<Integer, OpponentsTanks> tank : this.listOpponents.entrySet()) {
            OpponentsTanks t = tank.getValue();
            t.setLive(100);
        }
    }

    private void addSled(OpponentsTanks t) {
        try {
            float dst_sled = Vector2.dst2(t.getPosition().x, t.getPosition().y, deltaSledVec.get(t.nomder).x, deltaSledVec.get(t.nomder).y);
            if (dst_sled > 210) {
                deltaSledVec.put(t.getNomder(), t.getPosition().cpy());
                gsp.getGameSpace().addSled(t.getPosition().x, t.getPosition().y, t.getDirection().angleDeg());
            }
        } catch (NullPointerException e) {
            deltaSledVec.put(t.nomder, new Vector2(t.getPosition().x, t.getPosition().y));
        }
        //   checkParam(t);
    }

//    public void checkParam(OpponentsTanks t){
//        float dst2 = t.getPosition().dst2(gsp.getMainGame().getGamePlayScreen().getCameraGame().getCamera().position.x,gsp.getMainGame().getGamePlayScreen().getCameraGame().getCamera().position.y);
//        if(t.getNikPlayer()==null) gsp.getMainGame().getMainClient().getNetworkPacketStock().toSendMyParPlayer(t.nomder);
//    }

    public void updateOtherTank(boolean onLine) {
        updateClienOtherTank();

    }

    public void updateClienOtherTank() {

    }

    public void updateLocalTank() {

    }


//    public void routingInMassage(PacketModel m) {
////
////        if (m.getP().tip == Heading_type.MY_NIK) {
////            gsp.getGameSpace().getLighting().getBuletFlash().newFlesh(m.getP().p1, m.getP().p2);
////            return;
////        }
////
    // Heading_type.SHELL_RUPTURE
////        if (m.getP().tip == Heading_type.SHELL_RUPTURE) {
////            System.out.println("BOOOOOOOOM!!!!!!!!!!!  " + m.getP().p1 + "  " + m.getP().p2);
////            //gsp.getBullets().
////            gsp.getBullets().removeBullet(m.getP().p3);
////            gsp.pc.addPasricalDeath_little(m.getP().p1, m.getP().p2, 2.7f);
////            return;
////        }
////
//        if (m.getP().tip == Heading_type.MY_SHOT) {
//            //    System.out.println("Shoot " + m.getP().time_even + " np " + m.getP().nomer_pley);
//            temp.set(m.getP().p1, m.getP().p2);
//            tRotation.set(0, 400);
//            tRotation.setAngleDeg(m.getP().p3); /// навправление
//            // System.out.println(tempr.len());
//
//            gsp.getBullets().addBullet(temp, tRotation.cpy(), m.getP().p4);
//
//            ////////////
//            //  if (m.getP().p5 != gsp.getMainGame().getMainClient().myIdConnect) {
//            gsp.getAudioEngine().pleySoundKickStick();
////                gsp.pc.addPasricalExplosion(.3f, m.getP().p1, m.getP().p2);
////                gsp.pc.addParticalsSmokeOne(m.getP().p1, m.getP().p2);
//            gsp.pc.addPasricalDeath_little(m.getP().p1, m.getP().p2, 2.7f);
//            gsp.getGameSpace().getLighting().getBuletFlash().newFlesh(m.getP().p1, m.getP().p2);
//            //  }
/////////////////////
////
////
////            ////////////
////
////            return;
////        }
////
////
////        if (m.getP().tip == Heading_type.DISCONECT_PLAYER) {
////            System.out.println("Disconect");
////            try {
////                listOpponents.get(m.getP().p1).live = false;
////            } catch (NullPointerException e) {
////            }
////            return;
////        }
////
////
////        if (m.getP().tip == Heading_type.HP_PLAYER) {
////            System.out.println("!!!!!!!!HP!!!!!!!!!!!!!!1");
////            System.out.println("plaer " + m.getP().p1);
////            System.out.println("hp " + m.getP().p2);
////            System.out.println();
////            try {
////                listOpponents.get(m.getP().p1).hp = m.getP().p1;
////            } catch (NullPointerException e) {
////                gsp.getTank().setHp(m.getP().p2);
////            }
////
////
////            return;
////        }
////
////
////        if (m.getP().tip == Heading_type.PARAMETERS_PLAYER) {
////            System.out.println("<<<-- PARAMETERS_PLAYER");
////            System.out.println(m.getP());
////            //if(m.getP().p1 == -1)
////            //gsp.getMainGame().getMainClient().getNetworkPacketStock().toSendMYParameters(gsp.getTank().getHp()); // перекинуть в тану
////            try {
////                OpponentsTanks t = gsp.getTanksOther().listOpponents.get(m.getP().p6);
////                t.hp = m.getP().p1;
////                t.command = MathUtils.random(1, 2);
////                System.out.println(">>coomand :: " + m.getP().p3);
////                //    System.out.println(m.getP().textM + " nik!");
////                System.out.println("==============++++ nik ikniknik " + m.getP().textM);
////                t.setNikPlayer(m.getP().textM);
////                // System.out.println(t);
////            } catch (Exception e) {
////            }
////
////            return;
////        }
//
//
//    }

    public OpponentsTanks getTankForID(int id) {
        //// надо создать танк
        return this.listOpponents.get(id);
    }

    public Vector2 isCollisionsTanks(Vector2 pos) {
        for (Map.Entry<Integer, OpponentsTanks> tank : this.listOpponents.entrySet()) {
            if (!tank.getValue().isLive()) continue;
            if (tank.getValue().isCollisionsTanks(pos))
                return new Vector2().set(pos.cpy().sub(tank.getValue().getPosition()).nor());
        }
        return null;
    }

    public boolean getExists(int id) {
        OpponentsTanks t = this.listOpponents.get(id);
        if (t == null) return false;
        else return true;

    }


}
