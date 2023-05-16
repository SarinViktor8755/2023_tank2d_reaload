package com.tanks_2d.Screens.PauseScreen;

public class DataPlyerStatistic {
    String nik;
    int frag;
    int death;
    int damage_caused; // нанесенный ущерб
    int score;

    public DataPlyerStatistic(String nik, int frag, int death, int damage_caused,int score) {
        this.nik = nik;
        this.frag = frag;
        this.death = death;
        this.damage_caused = damage_caused;
        this.score = score;
    }

    @Override
    public String toString() {
        return "DataPlyerStatistic{" +
                "nik='" + nik + '\'' +
                ", frag=" + frag +
                ", death=" + death +
                ", damage_caused=" + damage_caused +
                ", score=" + score +
                '}';
    }
}
