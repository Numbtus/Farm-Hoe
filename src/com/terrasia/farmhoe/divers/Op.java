package com.terrasia.farmhoe.divers;

import com.terrasia.farmhoe.Main;

import java.util.function.UnaryOperator;

public class Op implements UnaryOperator<String> {

    public String apply(String str) {


        str = str.replace("%keyChance%", "" + Main.getInstance().getConfig().getDouble("keyDropChancePerLevel"));
        str = str.replace("&", "§");
        return str;
    }

}