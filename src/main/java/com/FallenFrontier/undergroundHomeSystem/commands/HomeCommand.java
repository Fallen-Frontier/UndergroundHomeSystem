package com.FallenFrontier.undergroundHomeSystem.commands;

import com.FallenFrontier.undergroundHomeSystem.world.HousingWorldManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HomeCommand implements CommandExecutor {

    private final HousingWorldManager worldManager;

    public HomeCommand(HousingWorldManager worldManager) {
        this.worldManager = worldManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Players only.");
            return true;
        }

        worldManager.teleportToHome(player);
        return true;
    }
}
