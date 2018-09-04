package com.mattymatty.observer;

import java.util.Map;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

public class BotObserver {
    public static void main(String[] arguments) throws Exception
    {

        testEnv();

        String[] tokens = System.getenv("BOT_TOKENS").replaceAll("\\[(.*)\\]","$1").split(",");

        for ( String token : tokens ) {
            JDA api = new JDABuilder(AccountType.BOT).setToken(token).buildBlocking();
            long id = api.getSelfUser().getIdLong();
            Global.botList.put(id,api);
            System.out.println(String.format("Bot %s logged in", api.getSelfUser().getName()));
        }

        System.out.println("All bots logged in\r\nStarting listener");

        Thread listener = new Thread(new BotListener(),"Listener Thread");

        listener.start();
    }

    private static void testEnv() throws Exception {
        Map<String, String> env = System.getenv();

        String var = env.get("BOT_TOKENS");
        if (var == null || var.isEmpty())
            throw new Exception("Missing environment variable: BOT_TOKENS");

        if (!var.matches("\\[[\\w\\.]+(,[\\w\\.]+)*\\]"))
            throw new Exception("Wrong syntax in environment variable: BOT_TOKENS");

        var = env.get("NOTIFY_USER");
        if (var == null || var.isEmpty())
            throw new Exception("Missing environment variable: NOTIFY_USER");

        var = env.get("LISTENER_PORT");
        if (var == null || var.isEmpty())
            throw new Exception("Missing environment variable: LISTENER_PORT");
    }

}
