package com.tanks_2d.Units.Tanks;


import com.tanks_2d.Screens.GamePlayScreen;

public class TanksOtherSP extends TanksOther{
    BotAdmin botAdmin;

    public TanksOtherSP(GamePlayScreen gsp) {
        super(gsp);
        System.out.println("TanksOtherSP");
        botAdmin = new BotAdmin(gsp);
    }

    public void updateClienOtherTank() {
        botAdmin.upDateBotBehaviour();
    }


}
