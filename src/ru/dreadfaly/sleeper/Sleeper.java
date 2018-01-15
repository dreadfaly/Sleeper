package ru.dreadfaly.sleeper;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.util.HashMap;

public class Sleeper extends JavaPlugin implements Listener{

    private int sleep_players;
    HashMap<Player, Integer> attempts = new HashMap<Player, Integer>();

    public void onEnable() {
        File config = new File(getDataFolder(), "config.yml");
        if(!config.exists()) {
            getLogger().info("Creating new file config...");
            getConfig().options().copyDefaults(true);
            saveDefaultConfig();
        }
        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getOnlinePlayers();
    }

    @EventHandler
    public void SleepOn (PlayerBedEnterEvent event) {
        sleep_players++;
        Player player = event.getPlayer();
        attempts.putIfAbsent(player, 0);
        attempts.put(player, attempts.get(player) + 1);
        if(attempts.get(player) >= 4) {
            player.sendMessage(this.getConfig().getString("flood").replace("&", "§"));
        } else {
            for (Player players : Bukkit.getOnlinePlayers()) {
                players.sendMessage(this.getConfig().getString("message_sleep").replace("&", "§").replace("{player}", player.getDisplayName()).replace("{sleep}", Integer.toString(sleep_players)).replace("{online}", Integer.toString(Bukkit.getOnlinePlayers().size())));
            }
        }
    }

    @EventHandler
    public void SleepOff (PlayerBedLeaveEvent event) {
        Player player = event.getPlayer();
        sleep_players--;
    }

    @EventHandler
    public void IfFlood (PlayerQuitEvent event) {
        Player player = event.getPlayer();
        attempts.put(player, 0);
    }
}
