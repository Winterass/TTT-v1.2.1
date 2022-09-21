package net.server.games.listeners;

import net.server.games.Main;
import org.bukkit.event.Listener;

import java.util.ArrayList;

public class GameProtectionListener implements Listener {

    private Main plugin;
    private ArrayList<String> buildModePlayers;

    public GameProtectionListener(Main plugin) {
        this.plugin = plugin;
        buildModePlayers = new ArrayList<>();
    }



}
