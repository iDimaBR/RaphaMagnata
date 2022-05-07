package com.github.idimabr.raphamagnata.tasks;

import com.github.idimabr.raphamagnata.RaphaMagnata;
import com.github.idimabr.raphamagnata.hook.EconomyHook;
import com.github.idimabr.raphamagnata.managers.MagnataManager;
import com.github.idimabr.raphamagnata.utils.NumberUtil;
import com.github.idimabr.raphamagnata.utils.Title;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class MagnataCheckTask extends BukkitRunnable {

    private final EconomyHook economyHook;
    private final MagnataManager manager;

    public MagnataCheckTask(MagnataManager manager, EconomyHook economyHook) {
        this.manager = manager;
        this.economyHook = economyHook;
    }

    @Override
    public void run() {
        RaphaMagnata instance = RaphaMagnata.getInstance();
        FileConfiguration config = instance.getConfig();

        if (Bukkit.getOnlinePlayers().isEmpty()) return;

        Map<String, Double> top = manager.getTop(0, config.getInt("NumberOfTops"));
        manager.getRichs().clear();

        int position = 0;
        for (Map.Entry<String, Double> entry : top.entrySet()) {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(entry.getKey());
            if (offlinePlayer == null || !offlinePlayer.hasPlayedBefore()) continue;

            if (position == 0) {
                if (config.getString("magnata").equalsIgnoreCase(entry.getKey())) {
                    position++;
                    manager.setMagnata(entry.getKey());
                    continue;
                }

                String magnata = entry.getKey();
                manager.setMagnata(entry.getKey());
                if (offlinePlayer.isOnline())
                    for (int i = 0; i < 10; i++) {
                        Bukkit.getScheduler().runTaskLater(instance, () -> offlinePlayer.getPlayer().getWorld().spawnEntity(offlinePlayer.getPlayer().getLocation(), EntityType.FIREWORK), 7 * i);
                    }

                double balance = economyHook.getBalance(offlinePlayer);
                if (config.getBoolean("MessagesChat"))
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        for (String string : config.getStringList("Messages.MagnataNew")) {
                            player.sendMessage(
                                    string.replace("&", "§")
                                            .replace("%money%", NumberUtil.getFormat(balance) + "")
                                            .replace("%player%", magnata)
                            );
                        }
                    }

                Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.valueOf(config.getString("Sounds.New")), 1, 1));

                if (config.getBoolean("MessagesTitle"))
                    Bukkit.getOnlinePlayers().forEach(player -> Title.sendTitle(player, 20, 35, 20, config.getString("Messages.TitleMagnataNew")
                            .replace("&", "§")
                            .replace("%money%", NumberUtil.getFormat(economyHook.getBalance(offlinePlayer)) + "")
                            .replace("%player%", magnata), config.getString("Messages.SubTitleMagnataNew")
                            .replace("&", "§")
                            .replace("%money%", NumberUtil.getFormat(balance) + "")
                            .replace("%player%", magnata))
                    );
                config.set("magnata", entry.getKey());
            } else {
                if (manager.getMagnata() != null && manager.getMagnata().equalsIgnoreCase(entry.getKey())) {
                    return;
                }
                manager.getRichs().add(entry.getKey());
            }
            position++;
        }

        config.set("richs", manager.getRichs());
        (instance).saveConfig();
    }
}