package com.terrasia.farmhoe;

import com.terrasia.farmhoe.commands.CommandFarmHoe;
import com.terrasia.farmhoe.event.OnBreakEvent;
import com.terrasia.farmhoe.event.onClickEvent;
import com.terrasia.farmhoe.event.onRightClick;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.sql.Connection;

public class Main extends JavaPlugin implements Listener {
    Connection connection;
    String host, database, username, password, tablename, settings_tablename;
    int port;
    FileConfiguration config;
    File cfile;
    File file;
    private File langFile = null;
    private FileConfiguration lang = null;
    private static Main instance;
    public Economy economy = null;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);





       instance = this;
        ItemManager.init();
        config = getConfig();
        // config.options().copyDefaults(true);
        saveDefaultConfig();
        createLang();
        cfile = new File(getDataFolder(), "config.yml");

        getServer().getPluginManager().registerEvents(new onRightClick(this), this);
        getServer().getPluginManager().registerEvents(new onClickEvent(this), this);
        getServer().getPluginManager().registerEvents(new OnBreakEvent(this), this);
        getCommand("fh").setExecutor(new CommandFarmHoe(this));







        //  file = new File(getDataFolder(), "storage.yml");

        //       YamlConfiguration data = YamlConfiguration.loadConfiguration(file);

        //   String test = data.getString("Test");
        System.out.println("[FarmHoe] plugin loaded !");



    }
    public boolean setupEconomy() {
        RegisteredServiceProvider<Economy> eco = getServer().getServicesManager().getRegistration(Economy.class);
        if (eco != null) {
            economy = eco.getProvider();
        }

        return economy != null;
    }
    public FileConfiguration getLang() {
        return this.lang;
    }

    private void createLang() {
        langFile = new File(getDataFolder(), "lang.yml");
        if (!langFile.exists()) {
            langFile.getParentFile().mkdirs();
            saveResource("lang.yml", false);
        }

        lang = new YamlConfiguration();
        try {
            lang.load(langFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }


    public void reloadLang() throws UnsupportedEncodingException {
        if (langFile == null) {
            langFile = new File(getDataFolder(), "customConfig.yml");
        }
        lang = YamlConfiguration.loadConfiguration(langFile);

        // Look for defaults in the jar
        Reader defConfigStream = new InputStreamReader(this.getResource("lang.yml"), "UTF8");
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            lang.setDefaults(defConfig);
        }
    }








    public int getUpgradePrice (Player player, String upgrade) {
        int price  = 0;
        final File sfile = new File(getDataFolder(), "data/storage.yml");
        final YamlConfiguration storFile = YamlConfiguration.loadConfiguration(sfile);
        String key = "Players." + player.getName() + ".";
        if (upgrade == "key") {
            int keyUpgradeLevel = 0;
             keyUpgradeLevel = storFile.getInt(key + "keyfinderlevel");
            int defaultprice = Main.getInstance().getConfig().getInt("keyUpgradePricePerLevel");
            // index.getKeyUpgradeConfig.getInt("level-"+ keyUpgradeLevel + "-price")
            price = (keyUpgradeLevel + 1) * defaultprice;
            int maxLevel = getConfig().getInt("keyUpgradeMaxlvl");
            if (keyUpgradeLevel == maxLevel){
                price = 999;
            }
        }

        return price;
    }






    public double getUpgradeChance (Player player, String upgrade) {
        double chance = 0;
        final File sfile = new File(getDataFolder(), "data/storage.yml");
        final YamlConfiguration storFile = YamlConfiguration.loadConfiguration(sfile);
        String key = "Players." + player.getName() + ".";
        if (upgrade == "key") {
            int keyUpgradeLevel = 0;
            double multiplier = getConfig().getDouble("keyDropChancePerLevel");
            storFile.getInt(key + "keyfinderlevel");
            chance = keyUpgradeLevel * multiplier;
        }

        return chance;
    }







    @Override
    public void onDisable() {

        System.out.println("[FarmHoe] Plugin was unloaded !");
    }

    public static Main getInstance() {
       return instance;
    }

}
