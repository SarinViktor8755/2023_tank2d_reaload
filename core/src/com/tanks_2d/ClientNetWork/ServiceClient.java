package com.tanks_2d.ClientNetWork;

import com.esotericsoftware.kryonet.Client;

public class ServiceClient {
//    static public void  sendMyNik(String nikName, MainClient client){
//        Network.StockMess pk = new Network.StockMess();
//        pk.tip = Heading_type.MY_NIK;
//        pk.textM = nikName;
//        client.getClient().sendUDP(pk);
//    }
//
//    static public void  sendMyTokken(String tokke, MainClient client){
//        Network.StockMess pk = new Network.StockMess();
//        pk.tip = Heading_type.MY_TOKKEN;
//        pk.textM = tokke;
//        client.getClient().sendUDP(pk);
//    }

    static public void sendMuCoordinat(float x, float y, int anTower, Client client) {
        Network.PleyerPosition pp = new Network.PleyerPosition();
        pp.xp = x;
        pp.yp = y;
        pp.roy_tower = anTower;
        client.sendUDP(pp);
    }


    static public int invert_the_command(int comand) {
        if(comand == Heading_type.RED_COMMAND) return Heading_type.BLUE_COMMAND;
        if(comand == Heading_type.BLUE_COMMAND) return Heading_type.RED_COMMAND;
        return -1;
    }








}
