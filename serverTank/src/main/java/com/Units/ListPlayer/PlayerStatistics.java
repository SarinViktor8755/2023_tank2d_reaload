package main.java.com.Units.ListPlayer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PlayerStatistics {
    static HashMap<String, Player_statistics> list_static = new HashMap<>();


//    public void addPlayer(int id_player, Player player){
//        if(list_static.containsKey(id_player)) return;
//        Player_statistics ps = new Player_statistics(player.nikName);
//
//
//
//
//        list_static.put(id_player,ps);
//    }

    public Player_statistics getPlayer(int id_player){
        String token = ListPlayers.getListPlayers().get(id_player).getTokken();
        if(list_static.containsKey(token)) return list_static.get(token);
        Player_statistics ps = new Player_statistics();
        ps.name = ListPlayers.getListPlayers().get(id_player).getNikName();
        list_static.put(token,ps);

        return ps;
    }

    ////////////////////////
    public void addDeath(int id_player){ // добавить смерть
        getPlayer(id_player);
        list_static.get(id_player).death++;
    }

    public void add_damage_done_in_hp(int id_player, int damage){ // добавить смерть
        Player_statistics ps = getPlayer(id_player);
        ps.damage_done_in_hp += damage;
        ps.name = ListPlayers.getListPlayers().get(id_player).getNikName();
    }

    public void addFrag(int id_player){ // добавить фраг
        Player_statistics ps = getPlayer(id_player);
        ps.fargs++;
        ps.name = ListPlayers.getListPlayers().get(id_player).getNikName();
    }

    public void set_nikname(int id_player,String nik_name){ // записать имя
        Player_statistics ps = getPlayer(id_player);
        ps.name = nik_name;
    }
    /////////////////////////

    public String getStatistigString(){
        int i = 0;

        Iterator<Map.Entry<String, Player_statistics>> itr = list_static.entrySet().iterator();
        while (itr.hasNext()) {
            i++;
            Map.Entry<String, Player_statistics> tank = itr.next();
            System.out.println(i + tank.getValue().name +"  "+tank.getValue().fargs + " d " + tank.getValue().death + " d " + tank.getValue().damage_done_in_hp);
            System.out.println();
        }
        return list_static.toString();
    }

    // передача для клиентов для экрана паузы
    public String generating_string_clients(){
        StringBuilder resuld_bilder = new StringBuilder();
        Iterator<Map.Entry<String, Player_statistics>> itr = list_static.entrySet().iterator();
        while (itr.hasNext()) {
            resuld_bilder.append("<p>::" + itr.next().getValue().toStringForClient());
        }

        return resuld_bilder.toString();
    }

    static public void clearListStatic(){
        PlayerStatistics.list_static.clear();
    }

}

