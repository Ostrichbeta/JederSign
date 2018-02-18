package com.jederSign;
import com.jederSign.Commands.signExecutor;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    @Override
    public void onEnable(){
        getLogger().info( ChatColor.AQUA + "JederSign插件正在載入" );
        this.getCommand( "sign" ).setExecutor( new signExecutor( this ) );
    }
}
