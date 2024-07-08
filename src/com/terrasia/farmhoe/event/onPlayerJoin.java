package com.terrasia.farmhoe.event;

import com.terrasia.farmhoe.Main;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import java.io.File;
import java.io.IOException;

public class onPlayerJoin implements Listener {

    private Main index;

    public onPlayerJoin(Main main) {
        this.index = main;
    }

    @EventHandler
    public void onConnect(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        final File sfile = new File(index.getDataFolder(), "data/storage.yml");
        final YamlConfiguration storFile = YamlConfiguration.loadConfiguration(sfile);
        String key = "Players." + player.getName() + ".";

        int canebroke = 0;
        int keylevel = 0;
        canebroke = storFile.getInt(key + "canebroke");
        keylevel = storFile.getInt(key + "keyfinderlevel");
        int value = canebroke + keylevel;

        if (value <= 0 ) {
            storFile.set(key + "canebroke", 0);
            storFile.set(key + "keyfinderlevel", 0);
            try {
                storFile.save(sfile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



    }



}