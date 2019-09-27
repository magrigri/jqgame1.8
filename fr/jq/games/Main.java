package fr.jq.games;

import fr.jq.games.commands.CommandGame;
import fr.jq.games.lib.Stats;
import fr.jq.games.listeners.ListenerInventory;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;


public class Main extends JavaPlugin {

    private Economy econ;
    public static Main main;

    @Override
    public void onEnable() {

        main = this;

        saveDefaultConfig();

        if (!setupEconomy()) {
            this.getLogger().severe("[jqgames] Disabled due to no Vault dependency found!");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        System.out.println("[jqgames] Enabling fr.jq.games");
        Stats.loadStats();
        getServer().getPluginManager().registerEvents(new ListenerInventory(this), this);
        getCommand("jqgame").setExecutor(new CommandGame(this));

    }

    @Override
    public void onDisable() {
        Stats.saveStats();
        System.out.println("[jqgames] Saving players stats fr.jq.games");
    }

    public static String getPrefix(){
        return Main.main.getConfig().getString("prefix").replace("&", "§");
    }


    private boolean setupEconomy() {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            return false;
        }

        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public Economy getEconomy() {
        return econ;
    }

}
