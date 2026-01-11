package com.FallenFrontier.undergroundHomeSystem;

import com.FallenFrontier.undergroundHomeSystem.commands.HomeCommand;
import com.FallenFrontier.undergroundHomeSystem.world.HousingWorldManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class UndergroundHomeSystem extends JavaPlugin {

    private HousingWorldManager worldManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        worldManager = new HousingWorldManager(this);

        getCommand("home").setExecutor(new HomeCommand(worldManager));

        getLogger().info("UndergroundHomeSystem enabled");
    }

    public HousingWorldManager getWorldManager() {
        return worldManager;
    }
}
