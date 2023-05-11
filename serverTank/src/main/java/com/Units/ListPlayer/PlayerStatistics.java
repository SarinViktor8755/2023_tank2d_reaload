package main.java.com.Units.ListPlayer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PlayerStatistics {
    static HashMap<Integer, player_Static> list_static = new HashMap<Integer, player_Static>();

    public void addPlayer(int id_player, Player player){
        player_Static ps = new player_Static(player.nikName);
        list_static.put(id_player,ps);
    }

    public void addPlayer(int id_player){
        player_Static ps = new player_Static();
        list_static.put(id_player,ps);
    }

    ////////////////////////
    public void addDeath(int id_player){ // добавить смерть
        if(!list_static.containsKey(id_player)) addPlayer(id_player);
        list_static.get(id_player).death++;
    }

    public void addFrag(int id_player){ // добавить фраг
        if(!list_static.containsKey(id_player)) addPlayer(id_player);
        list_static.get(id_player).fargs++;
    }

    public void set_nikname(int id_player,String nik_name){ // записать имя
        if(!list_static.containsKey(id_player)) addPlayer(id_player);
        list_static.get(id_player).name = nik_name;
    }
    /////////////////////////

    public String getStatistigString(){
        int i = 0;

        Iterator<Map.Entry<Integer, player_Static>> itr = list_static.entrySet().iterator();
        while (itr.hasNext()) {
            i++;
            Map.Entry<Integer, player_Static> tank = itr.next();
            System.out.println(i + tank.getValue().toString());
            System.out.println();
        }
        return list_static.toString();
    }







    public class player_Static {
        public player_Static(String name) {
            fargs = 0;
            death = 0;
            name = name;

        }

        public player_Static() {
            fargs = 0;
            death = 0;
            name = null;
        }

        public int fargs; // фраги игрока
        public int death;  // смерти игрока

        String name;

    }




}

