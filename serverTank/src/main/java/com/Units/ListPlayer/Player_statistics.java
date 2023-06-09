package main.java.com.Units.ListPlayer;

public class Player_statistics implements Comparable<Player_statistics> {
    public int fargs; // фраги игрока
    public int death;  // смерти игрока
    public String name;  // nik игрока
    public int damage_done_in_hp;  // нанесенный урон в хп
    String tokken;  // nik игрока
    public int score; // очки игрока
    public int command;
    public int id;

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
        this.command = 0;
    }


    @Override
    public String toString() {
        return "Player_statistics{" +
                "fargs=" + fargs +
                ", death=" + death +
                ", name='" + name + '\'' +
                ", damage_done_in_hp=" + damage_done_in_hp +
                ", tokken='" + tokken + '\'' +
                ", score=" + score +
                ", id=" + id +
                '}';
    }

    public String toStringForClient() {
//        final int length_name = 12;
//        String name_delta = name;
//        if (name.length() >= length_name) name_delta = name.substring(0, length_name);
        return name + "<_<nn " + fargs + " " + death + " " + damage_done_in_hp + " " + count_the_player_game_points() + " " + this.id+ " " + this.command;
    }

    private int count_the_player_game_points() {
        this.score = (fargs * 500) + (damage_done_in_hp * 2) - (death * 200); // подсчет очков
        if (score < 0) this.score = 0;
        return score;
    }

    @Override
    public int compareTo(Player_statistics o) {
        int r = 0;
        if (this.count_the_player_game_points() >= o.count_the_player_game_points()) r = -1;
        else r = 1;

        if (this.count_the_player_game_points() == o.count_the_player_game_points())
            if (this.damage_done_in_hp >= o.damage_done_in_hp) r = -1;
            else r = 1;
        return r;
    }

    public void setName(String name) {
        this.name = name;
    }
}