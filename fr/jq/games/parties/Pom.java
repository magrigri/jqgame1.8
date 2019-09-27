package fr.jq.games.parties;

import fr.jq.games.Main;
import fr.jq.games.gui.PomPlayingGui;
import fr.jq.games.lib.Stats;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.*;

public class Pom {

    public static ArrayList<Player> queue = new ArrayList<Player>();

    public static HashMap<Player, Integer> wager = new HashMap<Player, Integer>();

    public static HashMap<Player, Integer> randoms = new HashMap<Player, Integer>();

    public static HashMap<Player, Integer> deadcase = new HashMap<Player, Integer>();

    public static HashMap<Player, Player> playing = new HashMap<Player, Player>();

    public static ArrayList<Player> getQueue(){
        return queue;
    }

    public static Integer getWager(Player p){
        if(!wager.containsKey(p)){
            return 0;
        }
        return wager.get(p);
    }

    public static Player getBetter(Player joiner){
        if(isPlaying(joiner)){
            for(Map.Entry<Player, Player> entry : Pom.playing.entrySet()){
                if(entry.getValue().equals(joiner)){
                    return entry.getKey();
                }
            }
        }
        return null;
    }

    public static Player getJoiner(Player better){
        return playing.get(better);
    }

    public static boolean isInQueue(Player p){
        for (Player player : queue) {
            if (player.equals(p)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isPlaying(Player p){
        if(playing.containsKey(p) || playing.containsValue(p)){
            return true;
        }
        return false;
    }

    public static void joinQueue(Player p, Integer money){

        double playerBalance = Main.main.getEconomy().getBalance(p);

        if(isInQueue(p)){
            p.sendMessage(Main.getPrefix() + "Tu es déjà dans la file.");
            return;
        }
        if( Double.valueOf(money) > playerBalance){
            p.sendMessage(Main.getPrefix() + "§c/!\\ §2Tu essayes de miser une somme supérieur à ce que tu as sur compte compte en banque : "
                    + Main.main.getEconomy().format(playerBalance) + "$");
            return;
        }

        queue.add(p);
        wager.put(p, money);
        Random r = new Random();
        randoms.put(p, r.nextInt((35 - 1) + 1) + 1);
        int tempDeadCase;
        do{
            tempDeadCase = r.nextInt((35 - 1) + 1) + 1;
            if(tempDeadCase != randoms.get(p)) deadcase.put(p, r.nextInt((35 - 1) + 1) + 1);
        }while(tempDeadCase == randoms.get(p));

        p.sendMessage(Main.getPrefix() + "Vous avez été ajouté à la file ! Un joueur peut vous affronter avec la commande §c/game pom");
        p.sendMessage(Main.getPrefix() + "Annulez votre pari avec la commande §c/game pom clear");

    }

    public static void removeFromQueue(Player p){
        if(!isInQueue(p)){
            p.sendMessage(Main.getPrefix() + "Tu n'es pas dans la file.");
            return;
        }
        queue.remove(p);
        wager.remove(p);
        randoms.remove(p);
        deadcase.remove(p);
        p.sendMessage(Main.getPrefix() + "Tu as été retiré de la file");
    }

    public static void playVersus(Player inQueue, Player joining){

        if( Double.valueOf(Pom.getWager(inQueue)) > Main.main.getEconomy().getBalance(inQueue) ){
            inQueue.sendMessage(Main.getPrefix() + "§c/!\\ §2Tu essayes de miser une somme superieur à ce que tu as sur compte compte en banque : " + Main.main.getEconomy().format(Main.main.getEconomy().getBalance(inQueue)) + "$");
            joining.sendMessage(Main.getPrefix() + "§c/!\\ §2"+inQueue.getName()+" n'a plus assez d'argent pour jouer. Partie annulée");
            removeFromQueue(inQueue);
            return;
        }

        if( Double.valueOf(Pom.getWager(inQueue)) > Main.main.getEconomy().getBalance(joining) ){
            joining.sendMessage(Main.getPrefix() + "§c/!\\ §2Tu essayes de miser une somme supérieur à ce que tu as sur compte compte en banque : " + Main.main.getEconomy().format(Main.main.getEconomy().getBalance(joining)) + "$");
            joining.closeInventory();
            return;
        }

        if(!isInQueue(inQueue)){
            joining.sendMessage(Main.getPrefix() + inQueue + " n'est plus dans la file d'attente.");
            return;
        }

        if(isPlaying(inQueue)){
            joining.sendMessage(Main.getPrefix() + inQueue + "est déjà en jeu");
        }

        if(isInQueue(joining)){
            joining.sendMessage(Main.getPrefix() + "Tu es dans la file. Tu dois d'abord annuler ton pari avant de jouer contre quelqu'un. /game pom clear");
            return;
        }

        playing.put(inQueue, joining);

        inQueue.closeInventory();
        joining.closeInventory();

        PomPlayingGui pomPlayingGui1 = new PomPlayingGui();
        pomPlayingGui1.initializeItems();
        pomPlayingGui1.openInventory(inQueue);

        PomPlayingGui pomPlayingGui2 = new PomPlayingGui();
        pomPlayingGui2.initializeItems();
        pomPlayingGui2.openInventory(joining);
    }

    public static void win(Player better, Player joiner, Player winner, String... msgToSend){

        Double bet = Double.valueOf(getWager(better));
        Double betterMoney = Main.main.getEconomy().getBalance(better);
        Double joinerMoney = Main.main.getEconomy().getBalance(joiner);

        if(bet > betterMoney){
            better.sendMessage( Main.getPrefix() + "Vous n'avez plus assez d'argent, le jeu est annulé.");
            joiner.sendMessage(  Main.getPrefix() + "Le joueur adverse n'a plus assez d'argent, le jeu est annulé.");
            return;
        }

        if(bet > joinerMoney){
            better.sendMessage(Main.getPrefix() + "Le joueur adverse n'a plus assez d'argent, le jeu est annulé.");
            joiner.sendMessage( Main.getPrefix() + "Vous n'avez plus assez d'argent, le jeu est annulé." );
            return;
        }

        for(String str : msgToSend){
            better.sendMessage(Main.getPrefix() + str);
            joiner.sendMessage(Main.getPrefix() + str);
        }

        if(winner.equals(better)){

            better.sendMessage(Main.getPrefix() + "Bravo, tu gagnes " + getWager(better) + "$" );
            joiner.sendMessage(Main.getPrefix() + "Désolé, tu perds " + getWager(better) + "$");

            Stats.pomAddLose(joiner.getUniqueId().toString(), 0 - getWager(better));
            Stats.pomAddWin(better.getUniqueId().toString(), getWager(better));
            better.playSound(better.getLocation(), Sound.FIREWORK_LAUNCH, 1f, 1f);
            Main.main.getEconomy().depositPlayer(better, bet);
            Main.main.getEconomy().withdrawPlayer(joiner, bet );
        }

        else if(winner.equals(joiner)){
            joiner.sendMessage(Main.getPrefix() + "Bravo, tu gagnes " + getWager(better) + "$" );
            better.sendMessage(Main.getPrefix() + "Désolé, tu perds " + getWager(better) + "$");

            Stats.pomAddLose(better.getUniqueId().toString(), 0 - getWager(better));
            Stats.pomAddWin(joiner.getUniqueId().toString(), getWager(better));
            joiner.playSound(better.getLocation(), Sound.ANVIL_BREAK, 1f, 1f);
            Main.main.getEconomy().depositPlayer(joiner, bet);
            Main.main.getEconomy().withdrawPlayer(better, bet);
        }

        removeFromQueue(better);
        playing.remove(better);
        better.closeInventory();
        joiner.closeInventory();
    }


}
