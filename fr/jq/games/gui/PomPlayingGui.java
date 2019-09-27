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
import java.util.Map;

public class PomPlayingGui implements InventoryHolder {

    private Inventory inv;
    private Main main;

    public PomPlayingGui(){
        this.main = Main.main;
        this.inv = Bukkit.createInventory(this, 54, Main.main.getConfig().getString("inventoryNames.pomPlaying"));
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return inv;
    }

    // You can call this whenever you want to put the items in
    public void initializeItems() {

        for(int i = 1; i < 37; i++){
            inv.addItem(createGuiItem(Material.SNOW_BALL, i, Integer.toString(i)));
        }
        inv.setItem(53, createGuiItem(Material.BARRIER, 01,"§4Abandonner", "Vous perdrez la mise"));

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
        if(Pom.isInQueue(p)){
            inv.setItem(Pom.deadcase.get(p), createGuiItem(Material.BONE, 1, "Case morte", "Si vous cliquez ici, vous perdez"));
        }
        else{
            inv.setItem(Pom.deadcase.get(Pom.getBetter(p)), createGuiItem(Material.BONE, 1, "Case morte", "Si vous cliquez ici, vous perdez"));
        }
        p.openInventory(inv);
    }


}
