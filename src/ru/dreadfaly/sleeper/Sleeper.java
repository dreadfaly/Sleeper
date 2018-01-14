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
import java.util.HashMap;

public class Sleeper extends JavaPlugin implements Listener{

    HashMap<Player, Long> dont_flood = new HashMap<Player, Long>();
    private long player_sleep;

    public void onEnable() {
        this.getLogger().info("Плагин Sleeper включен. Версия: 0.1");
        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getOnlinePlayers();

    }

    @EventHandler
    public void SleepOn (PlayerBedEnterEvent event) {
        Player player = event.getPlayer();
        player_sleep++;
        if (dont_flood.get(player) == null) {
            dont_flood.put(player, (long) 0);
        }
        long number_sleep = dont_flood.get(player);
        dont_flood.put(player, number_sleep + 1);
        if(number_sleep >=3) {
            player.sendMessage(ChatColor.RED + "Флудите? Если это не так, то перезайдите на сервер!");
        } else {
            for (Player players : Bukkit.getOnlinePlayers()) {
                players.sendMessage(ChatColor.GOLD + "Игрок " + ChatColor.GREEN + player.getName()
                        + ChatColor.GOLD + " ложится спать. В кровати " + ChatColor.GREEN + player_sleep
                        + ChatColor.GOLD + " из " + ChatColor.GREEN + Bukkit.getOnlinePlayers().size()  + ChatColor.GOLD + " игроков");
            }
        }
    }

    @EventHandler
    public void SleepOff (PlayerBedLeaveEvent event) {
        Player player = event.getPlayer();
        player_sleep--;
    }

    @EventHandler
    public void IfFlood (PlayerQuitEvent event) {
        Player player = event.getPlayer();
        dont_flood.put(player, (long) 0);
    }
}
