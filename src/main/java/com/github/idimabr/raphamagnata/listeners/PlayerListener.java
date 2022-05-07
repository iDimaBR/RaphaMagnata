package com.github.idimabr.raphamagnata.listeners;

import br.com.devpaulo.legendchat.api.events.ChatMessageEvent;
import com.github.idimabr.raphamagnata.RaphaMagnata;
import com.github.idimabr.raphamagnata.managers.MagnataManager;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    private final MagnataManager magnataManager;

    public PlayerListener(MagnataManager magnataManager) {
        this.magnataManager = magnataManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        FileConfiguration config = RaphaMagnata.getInstance().getConfig();
        if (e.getPlayer().getName().equals(magnataManager.getMagnata())) {
            Bukkit.getServer().broadcastMessage(config.getString("Messages.MagnataEnter").replace("%nl%", "\n").replace("&", "§").replace("%player%", e.getPlayer().getName()));
            
            Sound sound = Sound.valueOf(config.getString("Sounds.Enter"));
            Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), sound, 1, 1));
        }
    }

    @EventHandler
    public void OnQuit(PlayerQuitEvent e) {
        FileConfiguration config = RaphaMagnata.getInstance().getConfig();
        if (e.getPlayer().getName().equals(magnataManager.getMagnata())) {
            Bukkit.getServer().broadcastMessage(config.getString("Messages.MagnataQuit").replace("%nl%", "\n").replace("&", "§").replace("%player%", e.getPlayer().getName()));

            Sound sound = Sound.valueOf(config.getString("Sounds.Quit"));
            Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), sound, 1, 1));
        }
    }

    @EventHandler
    public void onChat(ChatMessageEvent e) {
        FileConfiguration config = RaphaMagnata.getInstance().getConfig();
        if (e.getTags().contains("magnata") && e.getSender().getName().equals(magnataManager.getMagnata())) {
            e.setTagValue("magnata", config.getString("MagnataTag").replace("&", "§"));
        } else if (e.getTags().contains("rich") && config.getStringList("richs").contains(e.getSender().getName())) {
            e.setTagValue("rich", config.getString("RichTag").replace("&", "§"));
        }
    }
}
