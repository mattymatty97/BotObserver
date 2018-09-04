package com.mattymatty.observer;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.OnlineStatus;

public class Waiter implements Runnable{
    long id;
    JDA api;
    @Override
    public void run() {
        if(api==null)
            return;
        try{
            for(int ctn=1; ctn<=4; ctn++){
                Thread.sleep(15000);
                System.out.println("Bot "+ api.getSelfUser().getName() +" elapsed "+ (ctn*15) + " seconds since last ping");
            }
            System.out.println("Bot "+ api.getSelfUser().getName() +" is DEAD");
            api.getPresence().setStatus(OnlineStatus.OFFLINE);
            Global.threadList.remove(id);
            Global.notify(api,"Warning i'm **OFFLINE**");
        }catch (InterruptedException ignored){}
    }

    public Waiter(long id) {
        this.id = id;
        api = Global.botList.get(id);
    }
}
