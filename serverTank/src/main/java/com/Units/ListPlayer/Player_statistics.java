package main.java.com.Units.ListPlayer;

public class Player_statistics implements Comparable<Player_statistics> {
    public int fargs; // фраги игрока
    public int death;  // смерти игрока
    String name;  // nik игрока
    public int damage_done_in_hp;  // нанесенный урон в хп

    String tokken;  // nik игрока

    public int score; // очки игрока

    public Player_statistics(String name) {
        this.fargs = 0;
        this.death = 0;
        this.name = name;
        this.damage_done_in_hp = 0;
        this.score = 0;

    }

    public Player_statistics() {
        this.fargs = 0;
        this.death = 0;
        this.name = null;
        this.damage_done_in_hp = 0;
        this.score = 0;


    }


    @Override
    public String toString() {
        return "Player_statistics{" +
                "fargs=" + fargs +
                ", death=" + death +
                ", name='" + name + '\'' +
                '}';
    }

    public String toStringForClient() {
//        final int length_name = 12;
//        String name_delta = name;
//        if (name.length() >= length_name) name_delta = name.substring(0, length_name);
        return tokken + "<_<nn " + fargs + " " + death + " " + damage_done_in_hp + " " + count_the_player_game_points();
    }

    private int count_the_player_game_points() {
        this.score = (fargs * 500) + (damage_done_in_hp) - (death * 200);
        if(score < 0) this.score = 0;
        return score;
    }

    @Override
    public int compareTo(Player_statistics o) {
        if (this.count_the_player_game_points() >= o.count_the_player_game_points()) return -1;
        else return 1;
    }


}