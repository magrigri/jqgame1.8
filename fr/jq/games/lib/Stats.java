package fr.jq.games.lib;

import fr.jq.games.Main;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Stats {

    private static Main main = Main.main;

    public static HashMap<String, HashMap<String, Integer>> pom = new HashMap<String, HashMap<String, Integer>>();
    public static HashMap<String, HashMap<String, Integer>> mac = new HashMap<String, HashMap<String, Integer>>();

    public static void loadStats(){


        for(String uuid : main.getConfig().getConfigurationSection("Storage.Mac").getKeys(false)){
            HashMap<String, Integer> macHashMap = new HashMap<String, Integer>();
            for(String content : main.getConfig().getConfigurationSection("Storage.Mac."+uuid).getKeys(false)){
                macHashMap.put(content, main.getConfig().getInt("Storage.Mac."+uuid+"."+content));
            }
            mac.put(uuid, macHashMap);
        }


        for(String uuid : main.getConfig().getConfigurationSection("Storage.Pom").getKeys(false)){
            HashMap<String, Integer> pomHashMap = new HashMap<String, Integer>();
            for(String content : main.getConfig().getConfigurationSection("Storage.Pom."+uuid).getKeys(false)){
                pomHashMap.put(content, main.getConfig().getInt("Storage.Pom."+uuid+"."+content));
            }
            pom.put(uuid, pomHashMap);
        }

    }

    public static void saveStats(){
        for(Map.Entry<String, HashMap<String, Integer>> uuid : pom.entrySet()){
            for(Map.Entry<String, Integer> content : uuid.getValue().entrySet()){
                main.getConfig().set("Storage.Pom."+uuid.getKey()+"."+content.getKey(), content.getValue());
            }
        }
        for(Map.Entry<String, HashMap<String, Integer>> uuid : mac.entrySet()){
            for(Map.Entry<String, Integer> content : uuid.getValue().entrySet()){
                main.getConfig().set("Storage.Mac."+uuid.getKey()+"."+content.getKey(), content.getValue());
            }
        }

        main.saveConfig();
    }

    public static int macGetLose(String uuid){
        if(!mac.containsKey(uuid)) return 0;

        if(!mac.get(uuid).containsKey("lose")) return 0;

        return mac.get(uuid).get("lose");
    }

    public static int macGetEquality(String uuid){
        if(!mac.containsKey(uuid)) return 0;

        if(!mac.get(uuid).containsKey("equality")) return 0;

        return mac.get(uuid).get("equality");
    }

    public static int macGetWin1(String uuid){
        if(!mac.containsKey(uuid)) return 0;

        if(!mac.get(uuid).containsKey("win1")) return 0;

        return mac.get(uuid).get("win1");
    }

    public static int macGetWin2(String uuid){
        if(!mac.containsKey(uuid)) return 0;

        if(!mac.get(uuid).containsKey("win2")) return 0;

        return mac.get(uuid).get("win2");
    }

    public static int macGetTotalMoneyWin(String uuid){
        if(!mac.containsKey(uuid)) return 0;

        if(!mac.get(uuid).containsKey("totalMoneyWin")) return 0;

        return mac.get(uuid).get("totalMoneyWin");
    }

    public static void macAddLose(String uuid, int money){

        if(mac.containsKey(uuid)){
            if(mac.get(uuid).containsKey("lose")){
                mac.get(uuid).put("lose", mac.get(uuid).get("lose") + 1);
            }else{
                mac.get(uuid).put("lose", 1);
            }
        }else{
            HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
            hashMap.put("lose", 1);
            hashMap.put("equality", 0);
            hashMap.put("win1", 0);
            hashMap.put("win2", 0);
            hashMap.put("totalMoneyWin", 0);
            mac.put(uuid, hashMap);
        }

        macAddMoney(uuid, money);
    }

    public static void macAddequality(String uuid, int money){
        if(mac.containsKey(uuid)){
            if(mac.get(uuid).containsKey("equality")){
                mac.get(uuid).put("equality", mac.get(uuid).get("equality") + 1);
            }else{
                mac.get(uuid).put("equality", 1);
            }
        }else{
            HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
            hashMap.put("lose", 0);
            hashMap.put("equality", 1);
            hashMap.put("win1", 0);
            hashMap.put("win2", 0);
            hashMap.put("totalMoneyWin", 0);
            mac.put(uuid, hashMap);
        }

        macAddMoney(uuid, money);
    }

    public static void macAddWin1(String uuid, int money){
        if(mac.containsKey(uuid)){
            if(mac.get(uuid).containsKey("win1")){
                mac.get(uuid).put("win1", mac.get(uuid).get("win1") + 1);
            }else{
                mac.get(uuid).put("win1", 1);
            }
        }else{
            HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
            hashMap.put("lose", 0);
            hashMap.put("equality", 0);
            hashMap.put("win1", 1);
            hashMap.put("win2", 0);
            hashMap.put("totalMoneyWin", 0);
            mac.put(uuid, hashMap);
        }

        macAddMoney(uuid, money);
    }

    public static void macAddWin2(String uuid, int money){
        if(mac.containsKey(uuid)){
            if(mac.get(uuid).containsKey("win2")){
                mac.get(uuid).put("win2", mac.get(uuid).get("win2") + 1);
            }else{
                mac.get(uuid).put("win2", 1);
            }
        }else{
            HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
            hashMap.put("lose", 0);
            hashMap.put("equality", 0);
            hashMap.put("win1", 0);
            hashMap.put("win2", 1);
            hashMap.put("totalMoneyWin", 0);
            mac.put(uuid, hashMap);
        }

        macAddMoney(uuid, money);
    }

    private static void  macAddMoney(String uuid, int money){
        if(mac.containsKey(uuid)){
            if(mac.get(uuid).containsKey("totalMoneyWin")){
                mac.get(uuid).put("totalMoneyWin", mac.get(uuid).get("totalMoneyWin") + money);
            }else{
                mac.get(uuid).put("totalMoneyWin", money);
            }
        }else{
            HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
            hashMap.put("lose", 0);
            hashMap.put("equality", 0);
            hashMap.put("win1", 0);
            hashMap.put("win2", 0);
            hashMap.put("totalMoneyWin", money);
            mac.put(uuid, hashMap);
        }
    }

    public static int pomGetLose(String uuid){
        if(!pom.containsKey(uuid)) return 0;

        if(!pom.get(uuid).containsKey("lose")) return 0;

        return pom.get(uuid).get("lose");
    }

    public static int pomGetWin(String uuid){
        if(!pom.containsKey(uuid)) return 0;

        if(!pom.get(uuid).containsKey("win")) return 0;

        return pom.get(uuid).get("win");
    }

    public static int pomGetTotalMoneyWin(String uuid){
        if(!pom.containsKey(uuid)) return 0;

        if(!pom.get(uuid).containsKey("totalMoneyWin")) return 0;

        return pom.get(uuid).get("totalMoneyWin");
    }

    public static void pomAddWin(String uuid, int money){
        if(pom.containsKey(uuid)){
            if(pom.get(uuid).containsKey("win")){
                pom.get(uuid).put("win", pom.get(uuid).get("win") + 1);
            }else{
                pom.get(uuid).put("win", 1);
            }
        }else{
            HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
            hashMap.put("lose", 0);
            hashMap.put("win", 1);
            hashMap.put("totalMoneyWin", 0);
            pom.put(uuid, hashMap);
        }

        pomAddMoney(uuid, money);
    }

    public static void pomAddLose(String uuid, int money){
        if(pom.containsKey(uuid)){
            if(pom.get(uuid).containsKey("lose")){
                pom.get(uuid).put("lose", pom.get(uuid).get("lose") + 1);
            }else{
                pom.get(uuid).put("lose", 1);
            }
        }else{
            HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
            hashMap.put("lose", 1);
            hashMap.put("win", 0);
            hashMap.put("totalMoneyWin", 0);
            pom.put(uuid, hashMap);
        }

        pomAddMoney(uuid, money);
    }

    private static void pomAddMoney(String uuid, int money){
        if(pom.containsKey(uuid)){
            if(pom.get(uuid).containsKey("totalMoneyWin")){
                pom.get(uuid).put("totalMoneyWin", pom.get(uuid).get("totalMoneyWin") + money);
            }else{
                pom.get(uuid).put("totalMoneyWin", money);
            }
        }else{
            HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
            hashMap.put("lose", 0);
            hashMap.put("win", 0);
            hashMap.put("totalMoneyWin", money);
            pom.put(uuid, hashMap);
        }
    }

}
