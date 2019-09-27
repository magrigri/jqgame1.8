package fr.jq.games.commands;

import fr.jq.games.Main;
import fr.jq.games.gui.GamesGui;
import fr.jq.games.gui.MacPlayGui;
import fr.jq.games.gui.PomQueueGui;
import fr.jq.games.gui.StatsGui;
import fr.jq.games.lib.Stats;
import fr.jq.games.parties.Pom;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.UUID;
import java.util.regex.Pattern;


public class CommandGame implements CommandExecutor {

    private Main main;

    public CommandGame(Main main){
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {



        if(commandSender instanceof Player) {
            Player p = (Player) commandSender;


            if(strings.length == 0){
                GamesGui gamesGui= new GamesGui(main);
                gamesGui.initializeItems();
                gamesGui.openInventory(p);
            }

            else if(strings[0].equalsIgnoreCase("mac") && strings.length == 1 ){
                MacPlayGui macPlayGui = new MacPlayGui();
                macPlayGui.initializeItems();
                macPlayGui.openInventory(p);
            }

            else if(strings[0].equalsIgnoreCase("Pom") && strings.length == 2 ){

                if(strings[1].equalsIgnoreCase("clear")){
                    Pom.removeFromQueue(p);
                    return true;
                }

                if(!Pattern.matches("^[0-9]*$", strings[1])){
                    p.sendMessage(Main.getPrefix() + "Vous devez parier un nombre entier");
                    return false;
                }

                if(Integer.parseInt(strings[1]) < Integer.parseInt(main.getConfig().getString("Pom.minBet"))){
                    p.sendMessage(Main.getPrefix() + "Vous devez parier au moins " + main.getConfig().getString("Pom.minBet") + "$");
                    return false;
                }

                if(Integer.parseInt(strings[1]) > Integer.parseInt(main.getConfig().getString("Pom.maxBet"))){
                    p.sendMessage(Main.getPrefix() + "Vous devez parier au plus " + main.getConfig().getString("Pom.maxBet") + "$");
                    return false;
                }

                Pom.joinQueue(p, Integer.parseInt(strings[1]));


            }

            else if(strings[0].equalsIgnoreCase("Pom") && strings.length == 1 ){
                PomQueueGui pomQueueGui = new PomQueueGui(main);
                pomQueueGui.initializeItems();
                pomQueueGui.openInventory(p);
            }

            else if(strings[0].equalsIgnoreCase("stats") && strings.length == 1){
                StatsGui statsGui = new StatsGui(p.getUniqueId());
                statsGui.initializeItems();
                statsGui.openInventory(p);
            }

            else if(strings[0].equalsIgnoreCase("stats") && strings.length == 2){
                if(Bukkit.getOfflinePlayer(strings[1]).hasPlayedBefore()){
                    UUID wantedPlayer = Bukkit.getOfflinePlayer(strings[1]).getUniqueId();
                    StatsGui statsGui = new StatsGui( wantedPlayer );
                    statsGui.initializeItems();
                    statsGui.openInventory(p);
                }
                else{
                    p.sendMessage(Main.getPrefix() + "Ce joueur n'existe pas");
                }
            }

            else{
                p.sendMessage("§2§l-------" + Main.getPrefix() + "§2§l-------");
                p.sendMessage("");
                p.sendMessage("§4§l • §c/game stats §2- Afficher le menu des jeux");
                p.sendMessage("");
                p.sendMessage("§4§l • §c/game stats §2- Voir ses statistiques de jeu");
                p.sendMessage("§4§l • §c/game stats <joueur> §2- Voir les statistiques de jeu d'un joueur");
                p.sendMessage("");
                p.sendMessage("§4§l • §c/game pom <votre mise> §2- Parier une mise pour le jeu Plus ou Moins");
                p.sendMessage("§4§l • §c/game pom §2- Voir la liste des joueurs en attente d'une partie de Plus Ou Moins");
                p.sendMessage("");
                p.sendMessage("§4§l • §c/game mac §2- Jouer aux machines à sous");
                p.sendMessage("");
                p.sendMessage("§2§l-------" + Main.getPrefix() + "§2§l-------");
            }

        }

        else{
            commandSender.sendMessage(Main.getPrefix() + "Vous devez être un joueur");
            return false;
        }

        return false;
    }
}
