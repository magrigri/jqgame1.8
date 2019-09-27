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

public class GamesGui implements InventoryHolder {

    private Inventory inv;
    private Main main;

    public GamesGui(Main main){
        this.inv = Bukkit.createInventory(this, 9, main.getConfig().getString("inventoryNames.choseAGame"));
        this.main = main;
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return inv;
    }

    // You can call this whenever you want to put the items in
    public void initializeItems() {
        inv.setItem(8, createGuiItem(Material.PAPER, 1, "§bStats", "§aVoir ses stats de jeu", "§4/game stats <player>"));
        inv.setItem(1, createGuiItem(Material.ARROW, 2,
                "§bPOM - Plus Ou Moins", "§aJoue contre un autre joueur",
                "§aet tente de trouver le plus", "§arapidement possible le ",
                "§achiffre aléatoire",
                "",
                "§4/game pom §2<ta mise> pour créer une partie",
                "",
                "Clique ici pour accéder à la fil d'attente"
        ));
        inv.setItem(0, createGuiItem(Material.GOLD_INGOT, 01,"§bMachines à sous",
                "§aMise une somme, tu auras une chance sur 9 de remporter: ",
                "- "+ Integer.toString(main.getConfig().getInt("Mac.win1Multiple")) +"x tes gains",
                "- "+ Integer.toString(main.getConfig().getInt("Mac.win2Multiple")) +"x tes gains",
                "- Ne rien perdre, ni rien gagner"
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
