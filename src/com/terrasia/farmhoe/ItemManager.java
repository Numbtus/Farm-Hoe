package com.terrasia.farmhoe;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Array;
import java.util.Arrays;

public class ItemManager {



    private Main index;
    public ItemManager(Main main) {
        this.index = main;
    }

    public static ItemStack Hoe;

    public static void init() {
        createHoe();
    }

    public static void createHoe() {


        String desc = ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("hoeLore"));
        desc = desc.replace("%canebroke%", "0");

        ItemStack farmHoe = new ItemStack(Material.DIAMOND_HOE, 1);
        farmHoe.addEnchantment(Enchantment.DURABILITY, 1);
        ItemMeta farmHoeMeta = farmHoe.getItemMeta();
        farmHoeMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("hoeName")));
        farmHoeMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        farmHoeMeta.setLore(Arrays.asList(desc));
        farmHoeMeta.spigot().setUnbreakable(true);
        farmHoe.setItemMeta(farmHoeMeta);

        Hoe = farmHoe;
    }
}
