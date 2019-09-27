package fr.jq.games.gui;

import com.avaje.ebean.validation.NotNull;
import fr.jq.games.Main;
import fr.jq.games.parties.Pom;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


import java.util.ArrayList;

public class MacPlayGui implements InventoryHolder {

    private Inventory inv;
    private Main main;

    public MacPlayGui(){
        this.main = Main.main;
        this.inv = Bukkit.createInventory(this, 27, Main.main.getConfig().getString("inventoryNames.macPlay"));
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return inv;
    }

    // You can call this whenever you want to put the items in
    public void initializeItems() {

        inv.setItem(0, createGuiItem(Material.DIAMOND, 01,"1000", "Parier 1000$ (1k$)"));
        inv.setItem(1, createGuiItem(Material.DIAMOND, 01,"10000", "Parier 10k$"));
        inv.setItem(2, createGuiItem(Material.DIAMOND, 01,"20000", "Parier 20k$"));
        inv.setItem(3, createGuiItem(Material.DIAMOND, 01,"30000", "Parier 30k$"));
        inv.setItem(4, createGuiItem(Material.DIAMOND, 01,"40000", "Parier 40k$"));
        inv.setItem(5, createGuiItem(Material.DIAMOND, 01,"50000", "Parier 50k$"));
        inv.setItem(6, createGuiItem(Material.DIAMOND, 01,"60000", "Parier 60k$"));
        inv.setItem(7, createGuiItem(Material.DIAMOND, 01,"70000", "Parier 70k$"));
        inv.setItem(8, createGuiItem(Material.DIAMOND, 01,"85000", "Parier 85k$"));


        inv.setItem(9, createGuiItem(Material.DIAMOND_BLOCK, 01,"100000","Parier 100k$"));
        inv.setItem(10, createGuiItem(Material.DIAMOND_BLOCK, 01,"200000","Parier 200k$"));
        inv.setItem(11, createGuiItem(Material.DIAMOND_BLOCK, 01,"300000","Parier 300k$"));
        inv.setItem(12, createGuiItem(Material.DIAMOND_BLOCK, 01,"400000","Parier 400k$"));
        inv.setItem(13, createGuiItem(Material.DIAMOND_BLOCK, 01,"500000","Parier 500k$"));
        inv.setItem(14, createGuiItem(Material.DIAMOND_BLOCK, 01,"600000","Parier 600k$"));
        inv.setItem(15, createGuiItem(Material.DIAMOND_BLOCK, 01,"700000","Parier 700k$"));
        inv.setItem(16, createGuiItem(Material.DIAMOND_BLOCK, 01,"850000","Parier 850k$"));
        inv.setItem(17, createGuiItem(Material.DIAMOND_BLOCK, 01,"1000000","Parier 1000k$ (1 000 000 $)"));

        inv.setItem(26, createGuiItem(Material.BARRIER, 01,"§4Quitter"));
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
