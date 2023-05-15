package main.java.com.Units.ListPlayer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PlayerStatistics {
    static HashMap<Integer, Player_statistics> list_static = new HashMap<Integer, Player_statistics>();
    static String result_for_client;
    public void addPlayer(int id_player, Player player){
        if(list_static.containsKey(id_player)) return;
        Player_statistics ps = new Player_statistics(player.nikName);




        list_static.put(id_player,ps);
    }

    public void addPlayer(int id_player){
        if(list_static.containsKey(id_player)) return;
        Player_statistics ps = new Player_statistics();
        list_static.put(id_player,ps);

        ListPlayers.getListPlayers();
    }

    ////////////////////////
    public void addDeath(int id_player){ // добавить смерть
        addPlayer(id_player);
        list_static.get(id_player).death++;
    }

    public void add_damage_done_in_hp(int id_player, int damage){ // добавить смерть
        addPlayer(id_player);
        list_static.get(id_player).damage_done_in_hp += damage;
    }

    public void addFrag(int id_player){ // добавить фраг
        addPlayer(id_player);
        list_static.get(id_player).fargs++;
    }

    public void set_nikname(int id_player,String nik_name){ // записать имя
        addPlayer(id_player);
        list_static.get(id_player).name = nik_name;
    }
    /////////////////////////

    public String getStatistigString(){
        int i = 0;

        Iterator<Map.Entry<Integer, Player_statistics>> itr = list_static.entrySet().iterator();
        while (itr.hasNext()) {
            i++;
            Map.Entry<Integer, Player_statistics> tank = itr.next();
            System.out.println(i + tank.getValue().name +"  "+tank.getValue().fargs + " d " + tank.getValue().death + " d " + tank.getValue().damage_done_in_hp);
            System.out.println();
        }
        return list_static.toString();
    }

    // передача для клиентов для экрана паузы
    public String generating_string_clients(){
        StringBuilder resuld_bilder = new StringBuilder();
        Iterator<Map.Entry<Integer, Player_statistics>> itr = list_static.entrySet().iterator();
        while (itr.hasNext()) {
            resuld_bilder.append("<p>::" + itr.next().getValue().toStringForClient());
        }

        return resuld_bilder.toString();
    }

    static public void clearListStatic(){
        PlayerStatistics.list_static.clear();
    }

}

