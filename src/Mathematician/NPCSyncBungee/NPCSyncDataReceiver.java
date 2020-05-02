package Mathematician.NPCSyncBungee;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class NPCSyncDataReceiver implements Listener {

    @EventHandler
    public void onMessageEvent(PluginMessageEvent event) {
        if (!event.getTag().equalsIgnoreCase("npcsync:channel")) return;
        ByteArrayDataInput in = ByteStreams.newDataInput( event.getData() );
        String subChannel = in.readUTF();
        String contents = in.readUTF();
        if(event.getSender() instanceof Server){
            Server sender = (Server) event.getSender();
            String serverName = sender.getInfo().getName();
            if(subChannel.equalsIgnoreCase("request")){
                if(contents.length() == 0) {
                    contents = serverName;
                } else {
                    String[] elements = contents.split(",");
                    if(elements.length > 0) {
                        sendToSpecificServer(subChannel, contents, elements[0]);
                    }
                    return;
                }
            }
            send(subChannel, contents, serverName);
        }
    }

    public static void send(String subChannel, String message, String initialServerSenderName){
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        DataOutputStream output = new DataOutputStream(bytes);
        try{
            output.writeUTF(subChannel);
            output.writeUTF(message);
        }catch(IOException e){
            e.printStackTrace();
        }
        NPCSyncBungeeMain.plugin.getProxy().getServers().values().forEach(server -> {
            if(!server.getName().equalsIgnoreCase(initialServerSenderName)) {
                server.sendData("npcsync:channel", bytes.toByteArray());
            }
        });
    }

    public static void sendToSpecificServer(String subChannel, String message, String initialServerSenderName){
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        DataOutputStream output = new DataOutputStream(bytes);
        try{
            output.writeUTF(subChannel);
            output.writeUTF(message);
        }catch(IOException e){
            e.printStackTrace();
        }
        for(ServerInfo serverInfo : NPCSyncBungeeMain.plugin.getProxy().getServers().values()){
            if(serverInfo.getName().equalsIgnoreCase(initialServerSenderName)){
                serverInfo.sendData("npcsync:channel", bytes.toByteArray());
                break;
            }
        }
    }
}
