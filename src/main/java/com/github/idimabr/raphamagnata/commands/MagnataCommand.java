package com.github.idimabr.raphamagnata.commands;

import com.github.idimabr.raphamagnata.RaphaMagnata;
import com.github.idimabr.raphamagnata.hook.EconomyHook;
import com.github.idimabr.raphamagnata.managers.MagnataManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class MagnataCommand implements CommandExecutor {

    private final MagnataManager magnataManager;
    private final EconomyHook economyHook;

    public MagnataCommand(MagnataManager manager, EconomyHook economyHook) {
        this.magnataManager = manager;
        this.economyHook = economyHook;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(!(sender instanceof Player)) return false;

        Player player = (Player) sender;
        FileConfiguration config = RaphaMagnata.getInstance().getConfig();

        for (String string : config.getStringList("Messages.Magnata")) {
            player.sendMessage(
                    string.replace("%player%", magnataManager.getMagnata()).replace("&", "§")
            );
        }

        return false;
    }
}
