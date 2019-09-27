package fr.jq.games.gui;

import com.avaje.ebean.validation.NotNull;
import fr.jq.games.Main;
import fr.jq.games.lib.Stats;
import fr.jq.games.parties.Pom;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.UUID;

public class StatsGui implements InventoryHolder {

    private Inventory inv;
    private Main main;
    private UUID playerStats;

    public StatsGui(UUID pStats){
        this.main = Main.main;
        this.playerStats = pStats;
        this.inv = Bukkit.createInventory(this, 9, Main.main.getConfig().getString("inventoryNames.stats"));
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return inv;
    }

    // You can call this whenever you want to put the items in
    public void initializeItems() {
        inv.setItem(8, createGuiItem(Material.BARRIER, 01,"§4Quitter"));

        Player player = Bukkit.getOfflinePlayer(playerStats).getPlayer();
        ItemStack playerhead = new ItemStack(Material.SKULL_ITEM, 1);
        SkullMeta playerheadmeta = (SkullMeta) playerhead.getItemMeta();
        playerheadmeta.setOwner(player.getName());
        playerheadmeta.setDisplayName(player.getName());
        ArrayList<String> lores = new ArrayList<String>();
        playerheadmeta.setLore(lores);
        playerhead.setItemMeta(playerheadmeta);
        inv.addItem(playerhead);

        inv.addItem(createGuiItem(Material.GOLD_INGOT, 1,
                "§bMAC - Machine à sous",
                "§aJackpot : " + Stats.macGetWin2(playerStats.toString()),
                "§aSemi-win : " + Stats.macGetWin1(playerStats.toString()),
                "§aNi perdu, ni gagné : " + Stats.macGetEquality(playerStats.toString()),
                "§aLose : " + Stats.macGetLose(playerStats.toString()),
                "",
                "§aArgent total gagné : " + Stats.macGetTotalMoneyWin(playerStats.toString())
        ));
        inv.addItem(createGuiItem(Material.ARROW, 2,
                "§bPOM - Plus Ou Moins",
                "§aWin : " + Stats.pomGetWin(playerStats.toString()),
                "§aLose : " + Stats.pomGetLose(playerStats.toString()),
                "",
                "§aArgent total gagné : " + Stats.pomGetTotalMoneyWin(playerStats.toString())
        ));

    }

    // Nice little method to create a gui item with a custom name, and description
    private ItemStack createGuiItem(Material material, int amount, String name, String...lore) {
        ItemStack item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        ArrayList<String> metalore = new ArrayList<String>();

        for(String lorecomments : lore) {

            metalore.add(lorecomments);

        }

        meta.setLore(metalore);
        item.setItemMeta(meta);
        return item;
    }

    // You can open the inventory with this
    public void openInventory(Player p) {
        p.openInventory(inv);
    }

}
