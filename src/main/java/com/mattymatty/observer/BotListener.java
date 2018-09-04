package com.mattymatty.observer;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.OnlineStatus;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BotListener implements Runnable {

    ExecutorService workQueue = Executors.newSingleThreadExecutor();
    @Override
    public void run(){
        try {
            DatagramSocket serverSocket = new DatagramSocket(Integer.parseInt(System.getenv("LISTENER_PORT")));
            byte[] receiveData = new byte[Long.BYTES];
            while (!Thread.interrupted()) {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, Long.BYTES);
                serverSocket.receive(receivePacket);
                workQueue.submit(()->onPing(bytesToLong(receivePacket.getData())));
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private long bytesToLong(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.put(bytes);
        buffer.flip();//need flip
        return buffer.getLong();
    }

    private void onPing(long id){
        Thread waiter = Global.threadList.get(id);
        if(waiter!=null){
            waiter.interrupt();
            Global.threadList.remove(id);
        }else{
            JDA api = Global.botList.get(id);
            if(api==null)
                return;
            System.out.println(String.format("Bot %s Started",api.getSelfUser().getName()));
            api.getPresence().setStatus(OnlineStatus.ONLINE);
            Global.notify(api,"I'm **ONLINE** now");
        }
        waiter = new Thread(new Waiter(id),String.valueOf(id));
        waiter.start();
        Global.threadList.put(id,waiter);
    }

}
