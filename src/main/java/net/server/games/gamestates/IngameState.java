package net.server.games.gamestates;

import net.server.games.Main;
import net.server.games.countdowns.RoleCountcown;
import net.server.games.voting.Map;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class IngameState extends GameState{

    private Main plugin;
    private Map map;
    private ArrayList<Player> players;
    private RoleCountcown roleCountcown;

    public IngameState(Main plugin) {
        this.plugin = plugin;
        roleCountcown = new RoleCountcown(plugin);
    }

    @Override
    public void start() {
        Collections.shuffle(plugin.getPlayers());
        players = plugin.getPlayers();

        map = plugin.getVoting().getWinnerMap();
        map.load();
        for(int i = 0; i < players.size(); i++) {
            players.get(i).teleport(map.getSpawnLocations()[i]);
        }
        for(Player current : players) {
            current.getInventory().clear();
        }

        roleCountcown.start();
    }

    @Override
    public void stop() {

    }

}
