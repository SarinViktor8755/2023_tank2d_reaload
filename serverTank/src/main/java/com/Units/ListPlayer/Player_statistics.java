package main.java.com.Units.ListPlayer;

public class Player_statistics {
    public int fargs; // фраги игрока
    public int death;  // смерти игрока
    String name;  // nik игрока

    public int damage_done_in_hp;  // нанесенный урон в хп

    public Player_statistics(String name) {
        fargs = 0;
        death = 0;
        this.name = name;
        damage_done_in_hp = 0;

    }

    public Player_statistics() {
        fargs = 0;
        death = 0;
        name = null;
        damage_done_in_hp = 0;
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
        return name + "<_<nn " + fargs + " " + death + " " + damage_done_in_hp;
    }


}