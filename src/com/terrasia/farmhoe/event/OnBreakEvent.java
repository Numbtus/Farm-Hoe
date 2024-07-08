package com.terrasia.farmhoe.event;

import com.terrasia.farmhoe.ItemManager;
import com.terrasia.farmhoe.Main;
import com.terrasia.farmhoe.commands.CommandFarmHoe;
import net.minecraft.server.v1_8_R3.Item;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class OnBreakEvent implements Listener {

    private Main index;
    private CommandFarmHoe farmHoe;

    public OnBreakEvent(Main main) {
        this.index = main;
    }

    public OnBreakEvent(CommandFarmHoe commandFarmHoe) {
        this.farmHoe = commandFarmHoe;
    }
    @EventHandler(priority = EventPriority.HIGH)
    public void BlockBreak(BlockBreakEvent event) {

        Player player = event.getPlayer();
        ItemStack i = player.getItemInHand();
        final File sfile = new File(index.getDataFolder(), "data/storage.yml");
        final YamlConfiguration storFile = YamlConfiguration.loadConfiguration(sfile);
        String key = "Players." + player.getName() + ".";
        if (event.getBlock().getType() == Material.SUGAR_CANE_BLOCK) {
            if (event.getPlayer().getItemInHand().getType().equals(Material.AIR) || event.getPlayer().getItemInHand() == null) {
                return;
            } else {
                if (event.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals(ItemManager.Hoe.getItemMeta().getDisplayName())) {
                    Block b = event.getBlock().getLocation().getWorld().getBlockAt(event.getBlock().getLocation().getBlockX(), event.getBlock().getLocation().getBlockY() - 1, event.getBlock().getLocation().getBlockZ());
                    Block b2 = event.getBlock().getLocation().getWorld().getBlockAt(event.getBlock().getLocation().getBlockX(), event.getBlock().getLocation().getBlockY() + 1, event.getBlock().getLocation().getBlockZ());

                    if (b.getType() == Material.SUGAR_CANE_BLOCK) {
                        if (b2.getType() == Material.SUGAR_CANE_BLOCK) {
                            b2.setType(Material.AIR);
                            event.getBlock().setType(Material.AIR);
                            player.getInventory().addItem(new ItemStack(Material.SUGAR_CANE, 2));

                            int cane = storFile.getInt(key + "canebroke");
                            cane = cane +2;
                            String desc = ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("hoeLore"));

                            desc = desc.replace("%canebroke%", "" + cane + "");
                            ItemMeta meta = event.getPlayer().getItemInHand().getItemMeta();
                            meta.setLore(Arrays.asList(desc));
                            event.getPlayer().getItemInHand().setItemMeta(meta);

                            storFile.set(key + "canebroke", cane);
                            try {
                                storFile.save(sfile);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        } else {
                            event.getBlock().setType(Material.AIR);
                            player.getInventory().addItem(new ItemStack(Material.SUGAR_CANE, 1));
                            int cane = storFile.getInt(key + "canebroke");
                            cane = cane +1;
                            String desc = ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("hoeLore"));
                            desc = desc.replace("%canebroke%", "" + cane + "");
                            ItemMeta meta = event.getPlayer().getItemInHand().getItemMeta();
                            meta.setLore(Arrays.asList(desc));
                            event.getPlayer().getItemInHand().setItemMeta(meta);
                            storFile.set(key + "canebroke", cane);
                            try {
                                storFile.save(sfile);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        event.getBlock().setType(Material.AIR);
                        event.getBlock().getDrops().clear();


                        // Upgrade key
                        boolean upgradeSystem = index.getConfig().getBoolean("upgradeSystem");
                        if (upgradeSystem) {
                            double rdm1 = (Math.random() * (10000 - 0 + 1) + 0);
                            double result = rdm1 / 100;
                            double chance = index.getUpgradeChance(player, "key");
                            if (result <= chance) {
                                String hoePrefix = index.getConfig().getString("HoePrefix");
                                String cmd = index.getConfig().getString("keyUpgradeCommand");
                                String newCmd = cmd.replace("%player%", player.getName());
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), newCmd);
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', hoePrefix + index.getLang().getString("dropKey")));
                            }
                        }

                    } else {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }
}