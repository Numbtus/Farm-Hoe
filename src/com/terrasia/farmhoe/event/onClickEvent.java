package com.terrasia.farmhoe.event;

import com.terrasia.farmhoe.Main;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.io.File;
import java.io.IOException;

public class onClickEvent implements Listener {

    private Main index;

    public onClickEvent(Main main) {
        this.index = main;
    }



    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {

            if (event.getInventory().getName().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&',Main.getInstance().getConfig().getString("hoeInventoryName")))) {
                event.setCancelled(true);
                if (event.getSlot() == 4) {
                    if (Main.getInstance().setupEconomy()) {
                        Player player = (Player) event.getWhoClicked();
                        final File sfile = new File(index.getDataFolder(), "data/storage.yml");
                        final YamlConfiguration storFile = YamlConfiguration.loadConfiguration(sfile);
                        String key = "Players." + player.getName() + ".";
                        int level =  storFile.getInt(key + "keyfinderlevel");
                        int maxlevel = Main.getInstance().getConfig().getInt("keyUpgradeMaxlvl");
                        if (level >= maxlevel) {
                            player.closeInventory();
                            String message = ChatColor.translateAlternateColorCodes('&', Main.getInstance().getLang().getString("maxLevelReached"));
                            player.sendMessage(message);

                        }else {
                           int price =  Main.getInstance().getUpgradePrice(player, "key");
                           double bal = Main.getInstance().economy.getBalance(player);

                           if (price > bal) {
                               String message = ChatColor.translateAlternateColorCodes('&', Main.getInstance().getLang().getString("notEnoughMoney"));
                               player.closeInventory();
                               player.sendMessage(message);

                           }else
                           {
                               player.closeInventory();
                               Main.getInstance().economy.withdrawPlayer(player, price);
                               int newlevel = level +1;
                               String message = ChatColor.translateAlternateColorCodes('&', Main.getInstance().getLang().getString("confirmUpgrade"));
                                message = message.replace("%upgradeName%", ChatColor.translateAlternateColorCodes('&',Main.getInstance().getLang().getString("keyUpgradeName")));
                               message = message.replace("%level%", ""+ newlevel + "");

                               storFile.set(key + "keyfinderlevel", newlevel);

                               try {
                                   storFile.save(sfile);
                               } catch (IOException e) {
                                   e.printStackTrace();
                               }
                               player.sendMessage(message);


                           }




                        }






                    }
                }
            }
        }


    }


}
