package com.mattymatty.observer;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.entities.User;

import java.util.HashMap;
import java.util.Map;

public class Global {

    static Map<Long,JDA> botList = new HashMap<>();
    static Map<Long,Thread> threadList = new HashMap<>();


    static void notify(JDA api,String text){
        User notifier = api.getUserById(System.getenv("NOTIFY_USER"));
        if(notifier==null)
            return;
        notifier.openPrivateChannel().queue((PrivateChannel channel) -> channel.sendMessage(text).queue());
    }
}
