package com.github.idimabr.raphamagnata;

import com.github.idimabr.raphamagnata.commands.MagnataCommand;
import com.github.idimabr.raphamagnata.hook.EconomyHook;
import com.github.idimabr.raphamagnata.listeners.PlayerListener;
import com.github.idimabr.raphamagnata.managers.MagnataManager;
import com.github.idimabr.raphamagnata.tasks.MagnataCheckTask;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class RaphaMagnata extends JavaPlugin {

    private final EconomyHook economyHook = new EconomyHook();
    private final MagnataManager manager = new MagnataManager();

    @Override
    public void onLoad() {
        saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        manager.setMagnata(getConfig().getString("magnata", "Nenhum"));
    }

    public void onEnable() {
        economyHook.init();

        PluginCommand magnataCommand = getCommand("magnata");
        if (magnataCommand != null) magnataCommand.setExecutor(new MagnataCommand(manager, economyHook));

        PlayerListener listener = new PlayerListener(manager);
        Bukkit.getPluginManager().registerEvents(listener, this);

        MagnataCheckTask magnataCheckTask = new MagnataCheckTask(manager, economyHook);
        magnataCheckTask.runTaskTimer(this, 20L, 20L * 60 * getConfig().getInt("checkMagnata"));
    }

    public static RaphaMagnata getInstance() {
        return JavaPlugin.getPlugin(RaphaMagnata.class);
    }

    public EconomyHook getEconomyHook() {
        return economyHook;
    }
}
