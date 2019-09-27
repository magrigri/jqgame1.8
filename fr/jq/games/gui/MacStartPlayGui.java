package fr.jq.games.gui;

import com.avaje.ebean.validation.NotNull;
import fr.jq.games.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class MacStartPlayGui implements InventoryHolder {

    private Inventory inv;
    private Main main;
    public static HashMap<Player, Integer> bets = new HashMap<Player, Integer>();
    public static HashMap<Player, Integer> rand1 = new HashMap<Player, Integer>();
    public static HashMap<Player, Integer> rand9 = new HashMap<Player, Integer>();
    public static HashMap<Player, Integer> rand2 = new HashMap<Player, Integer>();

    public MacStartPlayGui(){
        this.main = Main.main;
        this.inv = Bukkit.createInventory(this, 9, Main.main.getConfig().getString("inventoryNames.macStartPlay"));
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return inv;
    }

    // You can call this whenever you want to put the items in
    public void initializeItems() {

        inv.setItem(0, createGuiItem(Material.BOOK, 01,"Carte 1"));
        inv.setItem(1, createGuiItem(Material.BOOK, 01,"Carte 2"));
        inv.setItem(2, createGuiItem(Material.BOOK, 01,"Carte 3"));
        inv.setItem(3, createGuiItem(Material.BOOK, 01,"Carte 4"));
        inv.setItem(4, createGuiItem(Material.BOOK, 01,"Carte 5"));
        inv.setItem(5, createGuiItem(Material.BOOK, 01,"Carte 6"));
        inv.setItem(6, createGuiItem(Material.BOOK, 01,"Carte 7"));
        inv.setItem(7, createGuiItem(Material.BOOK, 01,"Carte 8"));
        inv.setItem(8, createGuiItem(Material.BOOK, 01,"Carte 8"));


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
        Random random = new Random();
        rand1.put(p, random.nextInt(9));
        rand9.put(p, random.nextInt(9));
        rand2.put(p, random.nextInt(9));
        p.openInventory(inv);
    }

}
