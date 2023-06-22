package main.java.com;

import static com.tanks_2d.ClientNetWork.Network.register;

import static main.java.com.Units.ListPlayer.StatisticMath.playerStatistics;

import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.tanks_2d.ClientNetWork.Heading_type;
import com.tanks_2d.ClientNetWork.Network;
import com.tanks_2d.Locations.MapsList;
import com.tanks_2d.Screens.PauseScreen.PauseScreen;


import java.io.IOException;
import java.util.Date;


import main.java.com.Bots.IndexBot;
import main.java.com.MatchOrganization.IndexMath;
import main.java.com.Units.ListPlayer.ListPlayers;
import main.java.com.Units.ListPlayer.Player;
import main.java.com.Units.ListPlayer.PlayerStatistics;
import main.java.com.Units.SpaceMap.IndexMap;

public class GameServer {

    Server server;
    MainGame mainGame;
    IndexBot indexBot; // количество играков - по нему боты орентируюься сколько их нужно = для автобаласа
    //   private VoiceChatServer relay;


    public static boolean break_in_the_game;

    static long previousStepTime; // шаг для дельты
    public ListPlayers lp = new ListPlayers(this);


    public GameServer(String[] args, ServerLauncher serverLauncher) throws IOException {
        GameServer.break_in_the_game = false;

        int bufferSize = 100000000; // Recommened value.

        server = new Server(bufferSize, bufferSize);
        register(server);
        server.bind(Network.tcpPort, Network.udpPort);
        server.start();
        previousStepTime = System.currentTimeMillis();
///////////
        //   relay = new VoiceChatServer(server.getKryo());
///////////////
        mainGame = new MainGame(this, GameServer.getCountBot(args));
        server.addListener(new Listener() {

                               @Override
                               public void disconnected(Connection connection) {
                                   try {

                                       Player p = lp.getPlayerForId(connection.getID());
                                       if (p == null) return;
                                       lp.disconect(p.getId());
//                                       lp.getPlayerForId(connection.getID()).setStatus(Heading_type.DISCONECT_PLAYER);
//                                       lp.getPlayerForId(connection.getID()).setPosition(Player.DISCONECT_SYS_LAYER);
                                       // getLp().getPlayers().remove(connection.getID());
                                   } catch (NullPointerException e) {
                                       e.printStackTrace();
                                   }

                                   send_DISCONECT_PLAYER(connection.getID());
                               }

                               @Override
                               public void connected(Connection connection) {

                                   // send_MAP_PARAMETOR(connection.getID());


                               }


                               @Override
                               public void received(Connection connection, Object object) {

                                   //       relay.relayVoice(connection, object, server);
                                   ///      System.out.println(server.getConnections().length +"    -------------");
                                   if (object instanceof Network.PleyerPosition) {
                                       try {
                                           Network.PleyerPosition pp = (Network.PleyerPosition) object;
                                           Player p = lp.getPlayerForId(connection.getID());
                                           // System.out.println("position " + pp.xp);
                                           if (p == null) return;
                                           //  lp.sendToAllPlayerPosition(connection.getID(), (Network.PleyerPosition) object);
                                           p.setPosition(pp.xp, pp.yp);
                                           // d 92 стоке ошибка потом что нет игрока и постоянно срабаывает эксепшен 
                                           p.setRotTower(pp.roy_tower);

                                           return;
                                       } catch (NullPointerException e) {
                                           System.out.println("------------------ _______________________");
                                           System.out.println(connection.getID());
                                           System.out.println(lp);
                                           e.printStackTrace();
                                           return;
                                       }
                                   }

                                   if (object instanceof Network.StockMessOut) {// полученеи сообщения
                                       Network.StockMessOut sm = (Network.StockMessOut) object;
                                       System.out.println(sm);
                                       RouterMassege.routeSM(sm, connection.getID(), getMainGame().gameServer);
                                   }

                                   if (object instanceof Network.GivePlayerParameters) { //ПЕРЕДКЛАТЬ
                                       Network.GivePlayerParameters gp = (Network.GivePlayerParameters) object;
                                       if (lp.there_is_player(gp.nomerPlayer)) {
                                           send_PARAMETERS_PLAYER(lp.getPlayerForId(gp.nomerPlayer), connection.getID());
                                       } else send_DISCONECT_PLAYER(connection.getID());

                                   }


                                   if (object instanceof Network.RegisterUser) { //ПЕРЕДКЛАТЬ
                                       Network.RegisterUser rp = (Network.RegisterUser) object;
                                       //  String nikname = rp.nik;
                                       String tokken = rp.tokken;
                                       // int comand = rp.command;
                                       /////////////////////// тут регистрация пользователя _ это после конекта или плсде старта
                                       Player b_player = ListPlayers.getBasket().get(tokken);
                                       if (b_player == null) {
                                           Player p = new Player(connection.getID(), 1, tokken);
                                           getLp().addPlayer(p);
                                           ListPlayers.getBasket().put(p.getTokken(), p);
                                           System.out.println("KORZINA_ADD");
                                           //    p.setNikName(nikname);
                                       } else {
                                           System.out.println("KORZINA_BASKET");
                                           b_player.setId(connection.getID());
                                           // ListPlayers.getListPlayers().put(id_coonect,b_player);
                                           getLp().addPlayer(b_player);
                                           // b_player.setNikName(nikname);
                                           send_PARAMETERS_PLAYER(b_player);
                                       }

                                   }
//                                       //System.out.println(connection.getID() + " ::GivePlayerParameters" + (Network.GivePlayerParameters) object);
//                                       Network.GivePlayerParameters gpp = (Network.GivePlayerParameters) object;
//
//                                       lp.getPlayerForId(connection.getID()).setNikName(gpp.nik);
//                                       System.out.println("!!!GivePlayerParameters::  "+ connection.getID()+ "  -- " + gpp.nomerPlayer );
//
//                                       Player p = mainGame.gameServer.getLp().getPlayerForId(gpp.nomerPlayer);
//
//
//                                       if (p.getNikName() != null)
//                                           mainGame.gameServer.send_PARAMETERS_PLAYER(p, connection.getID(), gpp.nomerPlayer);
//                                   }


                               }
                           }
        );
        this.indexBot = new IndexBot(this, GameServer.getCountBot(args));
    }

    public Server getServer() {
        return server;
    }


    public void sendSHELL_RUPTURE(float x, float y, int nom, int author) {
        Network.StockMessOut stockMessOut = new Network.StockMessOut();
        stockMessOut.tip = Heading_type.SHELL_RUPTURE;
        stockMessOut.p1 = x;
        stockMessOut.p2 = y;
        stockMessOut.p3 = nom;
        stockMessOut.p4 = author;
        //stockMessOut.textM = mainGame.gameServer.getLp().getPlayerForId()
        System.out.println("-->> " + stockMessOut);
        this.sendToAllTCP_in_game(stockMessOut);

    }

//    public void send_add_frag(int n) {
//        Network.Frag f = new Network.Frag();
//        this.server.sendToUDP(n, f);
//
//    }

    public void send_PARAMETERS_PLAYER(int HP, int comant, String nikName, int forIdPlayer, int aboutPlayer) {
        Network.StockMessOut stockMessOut = new Network.StockMessOut();
        stockMessOut.tip = Heading_type.PARAMETERS_PLAYER;
        //  System.out.println(nikName);
        stockMessOut.p1 = aboutPlayer; // ХП
        stockMessOut.p2 = getCoomandforPlayer(aboutPlayer);// КОМАНДА
        stockMessOut.p3 = HP; // номер игрока
        stockMessOut.p4 = HP; // номер игрока
        stockMessOut.textM = nikName; // ник нейм
        System.out.println("-->> " + stockMessOut);
        this.server.sendToTCP(forIdPlayer, stockMessOut);

        //      System.out.println(nikName + ">>>>");
    }


    public void send_RESPOUN_PLAYER(int id, float x, float y) {
        Network.StockMessOut stockMessOut = new Network.StockMessOut();
        stockMessOut.tip = Heading_type.RESPOWN_TANK_PLAYER;
        stockMessOut.p1 = x; // позиция респауна
        stockMessOut.p2 = y; // позиция респауна
        stockMessOut.p3 = id; /// ид игрока

        System.out.println("-->> " + stockMessOut);
        /// комада игрока - отом исправить мсена команды
        this.server.sendToTCP(id, stockMessOut);
    }

    public void change_team(int id) {
        /////////////
        int comand;
        if (MathUtils.randomBoolean()) comand = Heading_type.RED_COMMAND;
        else comand = Heading_type.BLUE_COMMAND;
        lp.getPlayerForId(id).setCommand(comand);
        //   stockMessOut.p4 = comand;
        //////////////////Смена команды но надо еще мсообщить всем о смене )))
    }

    public void send_Chang_screen(boolean pause, float time) { // нужно добаить время на сколько уходим на паузу
        Network.StockMessOut stockMessOut = new Network.StockMessOut();
        stockMessOut.tip = Heading_type.CHANGE_THE_SCREEN;
        if (pause) {
            stockMessOut.p1 = Heading_type.PAUSE_GAME;
            ///////////
            PauseScreen.setGame_statistics_players(playerStatistics.generating_string_clients());
            stockMessOut.textM = PauseScreen.getGame_statistics_players();
            System.out.println("-->> " + stockMessOut);
            //PauseScreen.parser_result();
        } else {
            stockMessOut.p1 = Heading_type.PLAY_GAME;
            stockMessOut.textM = "";
            PlayerStatistics.clearListStatic();

            lp.respownAllPlaers();

        }
        stockMessOut.p2 = time;

        // if (stockMessOut.p1 == Heading_type.PLAY_GAME) return;
        ////System.out.println(">>>>>>>>>>>");
        this.server.sendToAllTCP(stockMessOut);

        //
        String map = mainGame.getMapSpace().getMap_math();
        mainGame.setMapSpace(new IndexMap(MapsList.getMapForServer(map))); // создаем новую карту
        System.out.println("-->> " + stockMessOut);
        send_MAP_PARAMETOR();

    }

    public void send_PARAMETERS_PLAYER(Player p, int forIdPlayer, int abautPlayer) {

        send_PARAMETERS_PLAYER(p.getHp(), p.getCommand(), p.getNikName(), forIdPlayer, abautPlayer);
    }

    public void send_PARAMETERS_MATH() { // разослать параметры матча
        Network.StockMessOut stockMessOut = new Network.StockMessOut();
        stockMessOut.tip = Heading_type.PARAMETERS_MATH;

//        stockMessOut.p1 = IndexMath.getBlue_team_score(); //счетсиний команды
//        stockMessOut.p2 = IndexMath.getRed_team_score(); //счетсиний команды

        stockMessOut.p1 = IndexMath.getBlue_team_score_math(); //счетсиний команды
        stockMessOut.p2 = IndexMath.getRed_team_score_math(); //счетсиний команды

        stockMessOut.p3 = getMainGame().getTimeMath(); // осталось времени в матче
        //stockMessOut.p4 = lp.getLive_blue_size_player(); // ОСТАЛОСЬ ЖИВЫХ КРАСНЫХ
        //ПЕРЕНЕСТИ В ПАКЕТ МАТЧА
        // stockMessOut.p3 = IndexMath.getRealTimeMath(); // вернуьт ревльное время матча

//        stockMessOut.p4 = IndexMath.getRealTimeMath(); // ОСТАЛОСЬ ЖИВЫХ СИНИХ
//        stockMessOut.p3 = IndexMath.getRealTimeMath(); // ОСТАЛОСЬ ЖИВЫХ КРАСНЫХ


        //stockMessOut.textM = ///IndexMath. // Номер карты будем делать менедже карт
        System.out.println("-->> " + stockMessOut);
        this.sendToAllTCP_in_game(stockMessOut);


    }

    /////!!!!!!!!!!!!!!!!!!!!!!!!!!
    public void send_PARAMETERS_PLAYER(Player p) { // для всех рассылк апараметров --- этот пакет определяет полнстью характеристики игрока))) !!!!!!!!!!
        Network.StockMessOut stockMessOut = new Network.StockMessOut();
        if (p.getStatus() == Heading_type.DISCONECT_PLAYER) {
            send_DISCONECT_PLAYER(p.getId());
            return;
        }
        /// роерить - может не существует игкрок
        stockMessOut.tip = Heading_type.PARAMETERS_PLAYER;
        stockMessOut.p1 = p.getId(); // id
        stockMessOut.p2 = getCoomandforPlayer(p.getId());// КОМАНДА
        stockMessOut.p3 = p.getHp(); // ХП
        stockMessOut.p4 = p.getCommand(); // номер игрока
        stockMessOut.textM = p.getNikName(); // ник нейм
        System.out.println("-->> " + stockMessOut);
        this.sendToAllTCP_in_game(stockMessOut);
    }

    /////!!!!!!!!!!!!!!!!!!!!!!!!!!
    public void send_PARAMETERS_PLAYER(Player p, int send_forPlayer) { // для всех рассылк апараметров --- этот пакет определяет полнстью характеристики игрока))) !!!!!!!!!!
        Network.StockMessOut stockMessOut = new Network.StockMessOut();
        if (p.getStatus() == Heading_type.DISCONECT_PLAYER) {
            send_DISCONECT_PLAYER(p.getId());
            return;
        }
        /// роерить - может не существует игкрок
        stockMessOut.tip = Heading_type.PARAMETERS_PLAYER;
        stockMessOut.p1 = p.getId(); // id
        stockMessOut.p2 = getCoomandforPlayer(p.getId());// КОМАНДА
        stockMessOut.p3 = p.getHp(); // ХП
        stockMessOut.p4 = p.getCommand(); // номер игрока
        stockMessOut.textM = p.getNikName(); // ник нейм
        System.out.println("-->> " + stockMessOut);
        this.sendToAllTCP_in_game(stockMessOut);
    }

    public void send_MAP_PARAMETOR() { // сообщить название карты
        Network.StockMessOut stockMessOut = new Network.StockMessOut();
        stockMessOut.tip = Heading_type.PARAMETERS_MAP;
        //stockMessOut.p1 = IndexMath.;
        stockMessOut.textM = mainGame.mapSpace.getMap_math();
//        if (mainGame.pause_game) stockMessOut.p1 = Heading_type.PAUSE_GAME;
//        else stockMessOut.p1 = Heading_type.PLAY_GAME;
//        if (GameServer.break_in_the_game) stockMessOut.p1 = Heading_type.PAUSE_GAME;
//        else stockMessOut.p1 = Heading_type.PLAY_GAME;
        System.out.println("-->> " + stockMessOut);
        this.server.sendToAllTCP(stockMessOut);


    }

    public void send_MAP_PARAMETOR(int id) { // сообщить название карты для одного
        Network.StockMessOut stockMessOut = new Network.StockMessOut();
        stockMessOut.tip = Heading_type.PARAMETERS_MAP;

        if (GameServer.break_in_the_game) stockMessOut.p1 = Heading_type.PAUSE_GAME;
        else stockMessOut.p1 = Heading_type.PLAY_GAME;

        stockMessOut.textM = mainGame.mapSpace.getMap_math();
        PlayerStatistics.clearListStatic();
        System.out.println("-->> " + stockMessOut);
        this.server.sendToTCP(id, stockMessOut);
        System.out.println("!!!!!!!!!!MAP:::");
    }


//    public void send_MATCH_STATISTICS() { // сообщить о статистики матча после матча
//        Network.StockMessOut stockMessOut = new Network.StockMessOut();
//        stockMessOut.tip = Heading_type.MATCH_STATISTICS;
//        stockMessOut.textM = mainGame.mapSpace.getMap_math();
//        this.server.sendToAllTCP(stockMessOut);
//
//    }

    public void sendToAllTCP_in_game(Object object) { // разослать тем кто в игре
        this.server.sendToAllTCP(object);
//        Connection[] connections = server.getConnections();
//        for (int i = 0, n = connections.length; i < n; i++) {
//            try {
//                Connection connection = connections[i];
//                // int id = connections[i].getID();
//                if (lp.getPlayerForId(connection.getID()).isClickButtonStart())
//                    connection.sendTCP(object);
//            } catch (NullPointerException e) {
//                //  lp.getPlayerForId(connection.getID())
//              //   lp.remove_player(connections[i].getID());
//                e.printStackTrace();
//            }

//            for (int i = 0, n = connections.length; i < n; i++) {
//                try {
//                    Connection connection = connections[i];
//                    // int id = connections[i].getID();
//                    if (lp.getPlayerForId(connection.getID()).isClickButtonStart())
//                        connection.sendTCP(object);
//                } catch (NullPointerException e) {
//                    //  lp.getPlayerForId(connection.getID())
//                    lp.remove_player(connections[i].getID());
//                    e.printStackTrace();
//                }

        // }


    }

    public void send_DISCONECT_PLAYER(int idPlayer) {
        Network.StockMessOut stockMessOut = new Network.StockMessOut();
        stockMessOut.tip = Heading_type.DISCONECT_PLAYER;
        stockMessOut.p1 = idPlayer;
        System.out.println("-->> " + stockMessOut);
        this.sendToAllTCP_in_game(stockMessOut);
    }


    public MainGame getMainGame() {
        return mainGame;
    }


    private int getSizeBot(String args[]) {
        try {
            return Integer.valueOf(args[0]);
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            return ListPlayers.DEFULT_COUNT_BOT;
        }
    }

    public static long getDeltaTime() {
        long time = System.currentTimeMillis();
        long raz = (time - previousStepTime);
        previousStepTime = time;
        return raz;
    }

    public boolean isServerLivePlayer() {
        // System.out.println(server.getConnections().length > 0);

        if (server.getConnections().length > 0) return true;
        else return false;
    }

    public int countLivePlayer() {


        return 1;
        // return server.getConnections().length;
    }

    public IndexBot getIndexBot() {
        return indexBot;
    }

    public ListPlayers getLp() {
        return lp;
    }


    public static String getDate() {
        // Инициализация объекта date
        Date date = new Date();
        // Вывод текущей даты и времени с использованием toString()
        return String.valueOf(date);
    }

    private static int getCountBot(String[] par) {
        int res = 8;
        try {
            res = Integer.parseInt(par[0]);
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return res;
    }

    private int getCoomandforPlayer(int id) {
        return lp.getPlayerForId(id).getCommand();
//        if (MathUtils.randomBoolean()) return Heading_type.BLUE_COMMAND;
//        else return Heading_type.RED_COMMAND;

    }


}

