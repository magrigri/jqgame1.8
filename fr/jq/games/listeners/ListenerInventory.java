package fr.jq.games.listeners;

import fr.jq.games.Main;
import fr.jq.games.gui.MacPlayGui;
import fr.jq.games.gui.MacStartPlayGui;
import fr.jq.games.gui.PomQueueGui;
import fr.jq.games.gui.StatsGui;
import fr.jq.games.lib.ItemStacksCreateGuiItem;
import fr.jq.games.lib.Stats;
import fr.jq.games.parties.Pom;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class ListenerInventory implements Listener {

    private Main main;

    public ListenerInventory(Main main){
        this.main = main;
    }

    @EventHandler
    public void onInvClose(InventoryCloseEvent e){
        Inventory inv = e.getInventory();
        Player p = (Player) e.getPlayer();
        if(inv.getName().equalsIgnoreCase(main.getConfig().getString("inventoryNames.pomPlaying")) && Pom.isPlaying(p)){
            if(Pom.isInQueue(p)){
                //Bukkit.broadcastMessage(Pom.getJoiner(p).getDisplayName() + " à gg");
                Pom.win(p, Pom.getJoiner(p), Pom.getJoiner(p), p.getDisplayName() + " à perdu en fermant son inventaire.");
            }
            else{
                //Bukkit.broadcastMessage(Pom.getBetter(p).getDisplayName() + " à gg");
                Pom.win(Pom.getBetter(p), p, Pom.getBetter(p), p.getDisplayName() + " à perdu en fermant son inventaire.");
            }
        }

        if(inv.getName().equalsIgnoreCase(main.getConfig().getString("inventoryNames.macStartPlay")) &&
                MacStartPlayGui.rand1.containsKey(p) &&
                MacStartPlayGui.rand9.containsKey(p) &&
                MacStartPlayGui.rand2.containsKey(p) &&
                MacStartPlayGui.bets.containsKey(p)
        ){
            p.sendMessage(Main.getPrefix() + "Pari annulé");
            MacStartPlayGui.bets.remove(p);
            MacStartPlayGui.rand1.remove(p);
            MacStartPlayGui.rand9.remove(p);
            MacStartPlayGui.rand2.remove(p);
            return;
        }

    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent e){

        Player p = (Player) e.getPlayer();

        if(
                MacStartPlayGui.rand1.containsKey(p) &&
                MacStartPlayGui.rand9.containsKey(p) &&
                MacStartPlayGui.rand2.containsKey(p) &&
                MacStartPlayGui.bets.containsKey(p)
        ){
            p.sendMessage(Main.getPrefix() + "Pari annulé");
            MacStartPlayGui.bets.remove(p);
            MacStartPlayGui.rand1.remove(p);
            MacStartPlayGui.rand9.remove(p);
            MacStartPlayGui.rand2.remove(p);
            return;
        }

        if(Pom.isPlaying(p)){
            if(Pom.isInQueue(p)){
                Pom.win(p, Pom.getJoiner(p), Pom.getJoiner(p), p.getDisplayName() + " s'est déconnecté et perd.");
            }
            else{
                Pom.win(Pom.getBetter(p), p, Pom.getBetter(p), p.getDisplayName() + " s'est déconnecté et perd.");
            }
        }

    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {


        Inventory inv = e.getInventory();
        Player p = (Player) e.getWhoClicked();
        ItemStack clickedItem = e.getCurrentItem();


        if (
                inv.getName().equalsIgnoreCase(main.getConfig().getString("inventoryNames.macStartPlay")) ||
                inv.getName().equalsIgnoreCase(main.getConfig().getString("inventoryNames.macPlay")) ||
                inv.getName().equalsIgnoreCase(main.getConfig().getString("inventoryNames.pomPlaying")) ||
                inv.getName().equalsIgnoreCase(main.getConfig().getString("inventoryNames.pomQueue")) ||
                        inv.getName().equalsIgnoreCase(main.getConfig().getString("inventoryNames.stats")) ||
                        inv.getName().equalsIgnoreCase(main.getConfig().getString("inventoryNames.choseAGame"))

        ){
            e.setCancelled(true);
        }

        if (  (clickedItem == null || clickedItem.getType() == Material.AIR) &&
                (
                        inv.getName().equalsIgnoreCase(main.getConfig().getString("inventoryNames.macStartPlay")) ||
                                inv.getName().equalsIgnoreCase(main.getConfig().getString("inventoryNames.macPlay")) ||
                                inv.getName().equalsIgnoreCase(main.getConfig().getString("inventoryNames.pomPlaying")) ||
                                inv.getName().equalsIgnoreCase(main.getConfig().getString("inventoryNames.pomQueue")) ||
                                inv.getName().equalsIgnoreCase(main.getConfig().getString("inventoryNames.stats")) ||
                                inv.getName().equalsIgnoreCase(main.getConfig().getString("inventoryNames.choseAGame"))
                )

        ){
            return;
        }

        if(inv.getName().equalsIgnoreCase(main.getConfig().getString("inventoryNames.choseAGame"))){

            if(e.getRawSlot() == 0) {
                MacPlayGui macPlayGui = new MacPlayGui();
                macPlayGui.initializeItems();
                macPlayGui.openInventory(p);
            }
            if(e.getRawSlot() == 1) {
                p.closeInventory();
                PomQueueGui pomQueueGui = new PomQueueGui(main);
                pomQueueGui.initializeItems();
                pomQueueGui.openInventory(p);
            }
            if (e.getRawSlot() == 8) {
                StatsGui statsGui = new StatsGui(p.getUniqueId());
                statsGui.initializeItems();
                statsGui.openInventory(p);
            }

        }

        else if(inv.getName().equalsIgnoreCase(main.getConfig().getString("inventoryNames.pomQueue"))){
            if (clickedItem.getType() == Material.SKULL_ITEM){
                if(clickedItem.getItemMeta().getDisplayName().equalsIgnoreCase(p.getName())){
                    p.sendMessage(Main.getPrefix() + "Vous ne pouvez pas jouer contre vous même !");
                    return;
                }

                Player better = Bukkit.getOfflinePlayer(clickedItem.getItemMeta().getDisplayName()).getPlayer();
                if(!better.isOnline()){
                    p.sendMessage(Main.getPrefix() + "Ce joueur est hors ligne");
                    return;
                }
                p.closeInventory();

                if( Double.valueOf(Pom.getWager(better)) > Main.main.getEconomy().getBalance(p) ){
                    p.sendMessage(Main.getPrefix() + "§c/!\\ §2Tu essayes de miser une somme supérieur à ce que tu as sur compte compte en banque : " + Main.main.getEconomy().format(Main.main.getEconomy().getBalance(p)) + "$");
                    p.closeInventory();
                    return;
                }

                better.sendTitle("§2Plus §4ou §2moins !", "§2" + p.getName() + " §4VS §2" + better.getName());
                better.sendMessage("§4------ §2Plus ou moins §4------");
                better.sendMessage("§4 /!\\ §cFermer §2votre §cinventaire §2revient à une §cdéfaite.");
                better.sendMessage("§4---------------------------");

                p.sendTitle("§2Plus §4ou §2moins !", "§2" + p.getName() + " §4VS §2" + better.getName());
                p.sendMessage("§4------ §2Plus ou moins §4------");
                p.sendMessage("§4 /!\\ §cFermer §2votre §cinventaire §2revient à une §cdéfaite.");
                p.sendMessage("§4---------------------------");
                Bukkit.getScheduler().runTaskLater(Main.main, () -> {
                    p.sendMessage(Main.getPrefix() + "§23...");
                    better.sendMessage(Main.getPrefix() + "§23...");
                }, 1 * 20);
                Bukkit.getScheduler().runTaskLater(Main.main, () -> {
                    p.sendMessage(Main.getPrefix() + "§22...");
                    better.sendMessage(Main.getPrefix() + "§22...");
                }, 2 * 20);
                Bukkit.getScheduler().runTaskLater(Main.main, () -> {
                    p.sendMessage(Main.getPrefix() + "§21...");
                    better.sendMessage(Main.getPrefix() + "§21...");
                }, 3 * 20);
                Bukkit.getScheduler().runTaskLater(Main.main, () -> Pom.playVersus(better, p), 4 * 20);

            }
            if(clickedItem.getType() == Material.BARRIER){
                p.closeInventory();
            }
        }

        else if(inv.getName().equalsIgnoreCase(main.getConfig().getString("inventoryNames.pomPlaying")) && Pom.isPlaying(p)){

            Player better;
            Player joiner;
            if (Pom.isInQueue(p)) {
                better = p;
                joiner = Pom.getJoiner(better);
            }
            else{
                joiner = p;
                better = Pom.getBetter(joiner);
            }

            if(clickedItem.getType() == Material.BONE){
                if(Pom.isInQueue(p)){
                    Pom.win(better, joiner, joiner, better.getDisplayName() + "à perdu en cliquant sur l'os !");
                }else{
                    Pom.win(better, joiner, better, joiner.getDisplayName() + "à perdu en cliquant sur l'os !");
                }
                return;
            }

            if(clickedItem.getType() == Material.BARRIER){
                if(Pom.isInQueue(p)){
                    Pom.win(better, joiner, joiner, better.getDisplayName() + " a abandonné !");
                }else{
                    Pom.win(better, joiner, better, joiner.getDisplayName() + " a abandonné !");
                }
                return;
            }

            if(clickedItem.getType() == Material.SNOW_BALL){

                int clickedNumber = Integer.parseInt(clickedItem.getItemMeta().getDisplayName());
                int targetNumber = Pom.randoms.get(better);

                if(clickedNumber < targetNumber){
                    inv.setItem(48, new ItemStack(Material.AIR));
                    inv.setItem(50, new ItemStack(Material.AIR));

                    ItemStack itemPlus = new ItemStack(Material.DIAMOND, 10);
                    ArrayList<String> itemPlusLores = new ArrayList<String>();
                    itemPlusLores.add("Le chiffre aléatoire est plus grand ! Retente ta chance.");
                    ItemMeta metaPlus = itemPlus.getItemMeta();
                    metaPlus.setDisplayName("+");
                    metaPlus.setLore(itemPlusLores);
                    itemPlus.setItemMeta(metaPlus);
                    inv.setItem(50, itemPlus);
                    p.updateInventory();
                    p.sendMessage(Main.getPrefix() + "Le chiffre est plus grand ! Retente ta chance.");
                }

                if(clickedNumber > targetNumber){
                    inv.setItem(48, new ItemStack(Material.AIR));
                    inv.setItem(50, new ItemStack(Material.AIR));

                    ItemStack itemMoins = new ItemStack(Material.DIAMOND, -10);
                    ArrayList<String> itemMoinsLores = new ArrayList<String>();
                    itemMoinsLores.add("Le chiffre aléatoire est plus petit ! Retente ta chance.");
                    ItemMeta metaMoins = itemMoins.getItemMeta();
                    metaMoins.setDisplayName("-");
                    metaMoins.setLore(itemMoinsLores);
                    itemMoins.setItemMeta(metaMoins);
                    inv.setItem(48, itemMoins);
                    p.updateInventory();
                    p.sendMessage(Main.getPrefix() + "Le chiffre aléatoire est plus petit ! Retente ta chance.");
                }


                if(clickedNumber == targetNumber){
                    Pom.win(better, joiner, p);
                }
            }
        }

        else if(inv.getName().equalsIgnoreCase(main.getConfig().getString("inventoryNames.macPlay"))){
            if(clickedItem.getType() == Material.DIAMOND || clickedItem.getType() == Material.DIAMOND_BLOCK){
                int bet = Integer.parseInt(clickedItem.getItemMeta().getDisplayName());

                if( Double.valueOf(bet) > Main.main.getEconomy().getBalance(p) ){
                    p.sendMessage(Main.getPrefix() + "§c/!\\ §2Tu essayes de miser une somme superieur à ce que tu as sur compte compte en banque : " + Main.main.getEconomy().format(Main.main.getEconomy().getBalance(p)) + "$");
                    return;
                }

                MacStartPlayGui.bets.put(p, bet);
                MacStartPlayGui macStartPlayGui = new MacStartPlayGui();
                macStartPlayGui.initializeItems();
                macStartPlayGui.openInventory(p);
            }
        }

        else if(inv.getName().equalsIgnoreCase(main.getConfig().getString("inventoryNames.macStartPlay"))
                && MacStartPlayGui.bets.containsKey(p)
                && MacStartPlayGui.rand2.containsKey(p)
                && MacStartPlayGui.rand9.containsKey(p)
                && MacStartPlayGui.rand1.containsKey(p)
        ){

            int clickedItemPosition = e.getRawSlot();

            inv.setItem(0, ItemStacksCreateGuiItem.createGuiItem(Material.BARRIER, 1, "Perdu"));
            inv.setItem(1, ItemStacksCreateGuiItem.createGuiItem(Material.BARRIER, 1, "Perdu"));
            inv.setItem(2, ItemStacksCreateGuiItem.createGuiItem(Material.BARRIER, 1, "Perdu"));
            inv.setItem(3, ItemStacksCreateGuiItem.createGuiItem(Material.BARRIER, 1, "Perdu"));
            inv.setItem(4, ItemStacksCreateGuiItem.createGuiItem(Material.BARRIER, 1, "Perdu"));
            inv.setItem(5, ItemStacksCreateGuiItem.createGuiItem(Material.BARRIER, 1, "Perdu"));
            inv.setItem(6, ItemStacksCreateGuiItem.createGuiItem(Material.BARRIER, 1, "Perdu"));
            inv.setItem(7, ItemStacksCreateGuiItem.createGuiItem(Material.BARRIER, 1, "Perdu"));
            inv.setItem(8, ItemStacksCreateGuiItem.createGuiItem(Material.BARRIER, 1, "Perdu"));

            int rand1 = MacStartPlayGui.rand1.get(p);
            int rand2 = MacStartPlayGui.rand2.get(p);
            int rand9 = MacStartPlayGui.rand9.get(p);

            inv.setItem(MacStartPlayGui.rand1.get(p), ItemStacksCreateGuiItem.createGuiItem(Material.GOLD_INGOT, 1, "Egalité", "Tu ne perds rien, tu ne gagnes rien"));
            inv.setItem(MacStartPlayGui.rand2.get(p), ItemStacksCreateGuiItem.createGuiItem(Material.DIAMOND, 2, "Tu gagnes 2 fois ta mise"));
            inv.setItem(MacStartPlayGui.rand9.get(p), ItemStacksCreateGuiItem.createGuiItem(Material.DIAMOND_BLOCK, 1, "Tu gagnes 9 fois ta mise"));

            p.updateInventory();

            double bet = Double.valueOf(MacStartPlayGui.bets.get(p));

            if(clickedItemPosition == rand9){
                p.sendMessage(Main.getPrefix() + "Vous gagnez "+main.getConfig().getInt("Mac.win2Multiple")+" fois votre mise !");
                Stats.macAddWin2(p.getUniqueId().toString(), MacStartPlayGui.bets.get(p) * main.getConfig().getInt("Mac.win2Multiple"));
                Main.main.getEconomy().depositPlayer(p, bet * Double.valueOf( main.getConfig().getInt("Mac.win2Multiple")));
            }
            else if(clickedItemPosition == rand2){
                p.sendMessage(Main.getPrefix() + "Vous gagnez "+main.getConfig().getInt("Mac.win1Multiple")+" fois votre mise !");
                Stats.macAddWin1(p.getUniqueId().toString(), MacStartPlayGui.bets.get(p) * main.getConfig().getInt("Mac.win1Multiple"));
                Main.main.getEconomy().depositPlayer(p, bet * Double.valueOf( main.getConfig().getInt("Mac.win1Multiple")));
            }
            else if(clickedItemPosition == rand1){
                p.sendMessage(Main.getPrefix() + "Vous ne gagnez rien mais ne perdez rien");
                Stats.macAddequality(p.getUniqueId().toString(), 0);
            }
            else{
                p.sendMessage(Main.getPrefix() + "Vous avez perdu :/");
                Stats.macAddLose(p.getUniqueId().toString(), 0 - MacStartPlayGui.bets.get(p));
                Main.main.getEconomy().withdrawPlayer(p, bet);
            }

            Bukkit.getScheduler().runTaskLater(Main.main, () -> {
                p.closeInventory();
            }, 1 * 20);



            MacStartPlayGui.bets.remove(p);
            MacStartPlayGui.rand1.remove(p);
            MacStartPlayGui.rand9.remove(p);
            MacStartPlayGui.rand2.remove(p);

        }

        else if(inv.getName().equalsIgnoreCase(main.getConfig().getString("inventoryNames.macStartPlay"))){
            return;
        }

    }

}
