package main.java.com;

import static main.java.com.Units.ListPlayer.StatisticMath.playerStatistics;

import com.badlogic.gdx.math.Vector2;
import com.tanks_2d.ClientNetWork.Heading_type;
import com.tanks_2d.ClientNetWork.Network;

import main.java.com.Units.ListPlayer.ListPlayers;
import main.java.com.Units.ListPlayer.Player;

public class RouterMassege {

    public static void routeSM(Network.StockMessOut sm, int id_coonect, GameServer gameServer) {
        //     System.out.println("-->> " + sm);
        if (Heading_type.MY_SHOT == sm.tip) {
            Vector2 velBullet = new Vector2(700, 0).setAngleDeg(sm.p3);
            gameServer.getMainGame().bullets.addBullet(new Vector2(sm.p1, sm.p2), velBullet, (int) sm.p4, id_coonect);
            //gameServer.getServer().sendToAllTCP(sm);
            gameServer.sendToAllTCP_in_game(sm);
            //System.out.println("shooooooooooot");
            return;
        }

        if (Heading_type.MY_NIK == sm.tip) {
            playerStatistics.set_nikname(id_coonect, sm.textM);
            int comand = (int) sm.p1;
            gameServer.getLp().getPlayerForId(id_coonect).setCommand(comand);
            playerStatistics.set_comand(id_coonect, (int) comand);

            //System.out.println(comand + "                 ----------------------");

            // gameServer.getLp().delete_by_toiken(sm.textM);
            // gameServer.getLp().addPlayer(new Player(id_coonect,(int)sm.p1,sm.textM));
            return;
        }


        if (Heading_type.BUTTON_STARTGAME == sm.tip) { // ответ на кнопку нажать СТАРТ
            gameServer.send_MAP_PARAMETOR(id_coonect);
            gameServer.lp.getPlayerForId(id_coonect).setNikName(sm.textM);
            playerStatistics.set_nikname(id_coonect, sm.textM);

            gameServer.lp.getPlayerForId(id_coonect).setCommand((int) sm.p1);
            gameServer.lp.getPlayerForId(id_coonect).setHp(100);
            gameServer.lp.getPlayerForId(id_coonect).setStatus(Heading_type.CLICK_BOTON_START);
            return;
        }
        if (Heading_type.MY_TOKKEN == sm.tip) {
            gameServer.getLp().delete_by_toiken(sm.textM  + " _____________MY_TOKKEN");
            //basket_
            Player b_player = ListPlayers.getBasket().get(sm.textM);
            if (b_player == null){
                Player p = new Player(id_coonect, (int) sm.p1, sm.textM);
                gameServer.getLp().addPlayer(p);
                ListPlayers.getBasket().put(p.getTokken(),p);
            }

            else {
                System.out.println("KORZINA_BASKET");
                b_player.setId(id_coonect);
               // ListPlayers.getListPlayers().put(id_coonect,b_player);
                gameServer.getLp().addPlayer(b_player);
                gameServer.send_PARAMETERS_PLAYER(b_player);
            }


            return;
        }


        if (Heading_type.STATUS_GAME == sm.tip) {
            //   System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!");
            gameServer.lp.getPlayerForId(id_coonect).setCommand((int) sm.p1);
            //  System.out.println(gameServer.lp.getPlayerForId(id_coonect).getCommand() + " ---");
            //  gameServer.getLp().getPlayerForId(id_coonect).setCommand();

            /// ответить время . карта параметры игры
            return;
        }


    }
}
