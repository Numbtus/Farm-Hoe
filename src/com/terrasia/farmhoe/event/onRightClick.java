package com.terrasia.farmhoe.event;

import com.terrasia.farmhoe.ItemManager;
import com.terrasia.farmhoe.Main;
import com.terrasia.farmhoe.divers.Op;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class onRightClick implements Listener {


    private Main index;

    public onRightClick(Main main) {
        this.index = main;
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {

        Player player = event.getPlayer();
        ItemStack i = player.getItemInHand();
        final File sfile = new File(index.getDataFolder(), "data/storage.yml");
        final YamlConfiguration storFile = YamlConfiguration.loadConfiguration(sfile);
        String key = "Players." + player.getName() + ".";
        String prefix = ChatColor.translateAlternateColorCodes('&', index.getConfig().getString("HoePrefix"));
        if (event.hasItem()) {
            if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (player.getItemInHand().getItemMeta().getDisplayName().equals(ItemManager.Hoe.getItemMeta().getDisplayName())) {
                    boolean upgradeStatus = index.getConfig().getBoolean("upgradeSystem");
                    if (!upgradeStatus) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatColor.translateAlternateColorCodes('&', prefix + index.getConfig().getString("upgradeDisable")) ));
                    } else {
                        String inventoryName = ChatColor.translateAlternateColorCodes('&', index.getConfig().getString("hoeInventoryName"));
                        int invSize = index.getConfig().getInt("inventorySize");
                        Inventory upgradeInv = Bukkit.createInventory(null, invSize, inventoryName);
                        int keyUpgradeLevel = 0;
                        keyUpgradeLevel = storFile.getInt(key + "keyfinderlevel");

                        int price = index.getUpgradePrice(player, "key");

                        String priceLore = "error";
                        double multiplier = index.getConfig().getDouble("keyDropChancePerLevel");
                        String taux = ChatColor.translateAlternateColorCodes('&', "&a&lTaux actuel: &e&l" + keyUpgradeLevel * multiplier + "&6&l%");
                        if (price != 999) {
                            String langm = index.getLang().getString("upgradePrice");
                            langm = langm.replace("%price%", "" +price);
                            priceLore = langm;
                        } else {
                            priceLore = index.getLang().getString("maxLevelReached");
                        }
                        List<String> KeyChanceLore = index.getLang().getStringList("keyChanceLore");
                        List<String> newList = new ArrayList<String>();
                        newList.addAll(KeyChanceLore);
                        newList.add(taux);
                        newList.add(ChatColor.translateAlternateColorCodes('&',priceLore));

                        newList.replaceAll(new Op());


                        ItemStack keyChance = new ItemStack(Material.TRIPWIRE_HOOK, 1);
                        if (keyUpgradeLevel > 0) {
                            keyChance.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
                        }
                        ItemMeta keyChanceM = keyChance.getItemMeta();
                        if (keyUpgradeLevel > 0) {
                            keyChanceM.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                        }
                        String KeyUpgradeName = index.getLang().getString("keyUpgradeName").replace("%level%", "" + keyUpgradeLevel + "");
                        keyChanceM.setDisplayName(ChatColor.translateAlternateColorCodes('&', KeyUpgradeName));
                        keyChanceM.setLore(newList);

                        keyChance.setItemMeta(keyChanceM);
                        upgradeInv.setItem(4, keyChance);


                        player.openInventory(upgradeInv);
                    }
                }
            }
        }
    }
}
