package com.FallenFrontier.undergroundHomeSystem.world;

import com.FallenFrontier.undergroundHomeSystem.util.WorldCopyUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class HousingWorldManager {

    private final JavaPlugin plugin;
    private final File worldsRoot;
    private final File templateWorld;

    public HousingWorldManager(JavaPlugin plugin) {
        this.plugin = plugin;

        // Base folder for all player worlds inside the container
        this.worldsRoot = new File("/data/underground_worlds");
        if (!worldsRoot.exists()) worldsRoot.mkdirs();

        // Template folder
        this.templateWorld = new File(worldsRoot, "template");

        plugin.getLogger().info("Worlds root: " + worldsRoot.getAbsolutePath());
        plugin.getLogger().info("Template path: " + templateWorld.getAbsolutePath());
    }

    /**
     * Ensures a player world exists by copying the template if needed
     */
    public void teleportToHome(Player player) {
        UUID uuid = player.getUniqueId();
        String playerWorldName = "player_" + uuid;

        // Folder where the player world will live (inside the container)
        File playerWorldFolder = new File(worldsRoot, playerWorldName);

        try {
            // Copy template if missing
            if (!playerWorldFolder.exists()) {
                plugin.getLogger().info("Creating housing world for " + player.getName() +
                        " from template " + templateWorld.getAbsolutePath());

                WorldCopyUtil.copy(templateWorld, playerWorldFolder);
            } else {
                plugin.getLogger().info("Player world already exists: " + playerWorldName);
            }

            // Load world by name (Paper automatically uses world-container folder)
            World world = Bukkit.getWorld(playerWorldName);
            if (world == null) {
                plugin.getLogger().info("Loading world " + playerWorldName);
                world = Bukkit.createWorld(new WorldCreator(playerWorldName));
            }

            if (world == null) {
                throw new IllegalStateException("Failed to load player world: " + playerWorldName);
            }

            // Teleport player
            Location spawn = new Location(world, 8, 80, 2, 150, 0);
            player.teleport(spawn);
            player.sendMessage("§aWelcome to your underground home!");

        } catch (IOException e) {
            player.sendMessage("§cFailed to create your underground home. Contact an admin.");
            plugin.getLogger().severe("Error copying template world for " + player.getName() + ": " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            player.sendMessage("§cFailed to load your underground home. Contact an admin.");
            plugin.getLogger().severe("Error loading housing world for " + player.getName() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}