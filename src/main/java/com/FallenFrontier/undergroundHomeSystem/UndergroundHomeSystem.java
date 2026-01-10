package com.FallenFrontier.undergroundHomeSystem;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;

public final class UndergroundHomeSystem extends JavaPlugin {

    private final HashMap<String, World> playerWorlds = new HashMap<>();

    private File centralFolder;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        String folderPath = getConfig().getString("folder-location");
        centralFolder = new File(folderPath);
        if (!centralFolder.exists()) {
            centralFolder.mkdirs();
        }
        getLogger().info("UndergroundHome enabled! Worlds folder: " + centralFolder.getAbsolutePath());
    }

    @Override
    public void onDisable() {
        getLogger().info("UndergroundTest disabled!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) return false;

        if (command.getName().equalsIgnoreCase("home")) {
            World world = playerWorlds.get(player.getName());

            if (world == null) {
                try {
                    // Folder for this player's underground world
                    File worldFolder = new File(centralFolder, "underground_" + player.getName());
                    if (!worldFolder.exists()) {
                        worldFolder.mkdirs();
                    }

                    // Create the world
                    WorldCreator creator = new WorldCreator(worldFolder.getPath());
                    creator.environment(World.Environment.NORMAL);
                    creator.generateStructures(false); // optional
                    world = Bukkit.createWorld(creator);

                    // Store reference in memory
                    playerWorlds.put(player.getName(), world);


                } catch (Exception e) {
                    player.sendMessage("§cError creating your underground world! Contact an admin.");
                    e.printStackTrace();
                    return true;
                }
            }

            // Teleport player to spawn
            player.teleport(world.getSpawnLocation());
            player.sendMessage("§aWelcome to your underground home!");

            return true;
        }

        return false;
    }

}
