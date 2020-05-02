package Mathematician.NPCSyncBungee;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.plugin.Plugin;

public class NPCSyncBungeeMain extends Plugin {

    public static NPCSyncBungeeMain plugin;

    @Override
    public void onEnable() {
        if(plugin == null){
            plugin = this;
        }
        this.getProxy().registerChannel("npcsync:channel");
        BungeeCord.getInstance().getPluginManager().registerListener(this, new NPCSyncDataReceiver());
    }

    @Override
    public void onDisable() {
        getLogger().info( "Plugin disabled!" );
    }

}
