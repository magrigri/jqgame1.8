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
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;

public class PomQueueGui implements InventoryHolder {


    private Inventory inv;
    private Main main;

    public PomQueueGui(Main main){
        this.inv = Bukkit.createInventory(this, 54, main.getConfig().getString("inventoryNames.pomQueue"));
        this.main = main;
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return inv;
    }

    // You can call this whenever you want to put the items in
    public void initializeItems() {


        for(Player player : Pom.getQueue()){

            if(Pom.isPlaying(player) == false){
                ItemStack playerhead = new ItemStack(Material.SKULL_ITEM, 1);
                SkullMeta playerheadmeta = (SkullMeta) playerhead.getItemMeta();
                playerheadmeta.setOwner(player.getName());
                playerheadmeta.setDisplayName(player.getName());
                ArrayList<String> lores = new ArrayList<String>();
                lores.add(Pom.getWager(player).toString() + "$");
                playerheadmeta.setLore(lores);
                playerhead.setItemMeta(playerheadmeta);
                inv.addItem(playerhead);
            }
        }
        inv.setItem(53, createGuiItem(Material.BARRIER, 01,"§4Quitter"));
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
