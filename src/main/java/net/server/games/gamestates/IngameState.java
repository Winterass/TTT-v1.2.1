package net.server.games.gamestates;

import net.server.games.Main;
import net.server.games.countdowns.RoleCountcown;
import net.server.games.role.Role;
import net.server.games.voting.Map;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class IngameState extends GameState{

    private Main plugin;
    private Map map;
    private ArrayList<Player> players;
    private RoleCountcown roleCountcown;

    private Role winnigRole;

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
            current.setHealth(20);
            current.setFoodLevel(20);
            current.getInventory().clear();
            current.setGameMode(GameMode.SURVIVAL);
        }

        roleCountcown.start();
    }

    public void checkGameEnding() {
        if(plugin.getRoleManager().getTraitorPlayers().size() <= 0) {
            winnigRole = Role.INNOCENT;
            plugin.getGamestateManager().setGameState(ENDING_STATE);
        } else if (plugin.getRoleManager().getTraitorPlayers().size() == plugin.getPlayers().size()) {
            winnigRole = Role.TRAITOR;
            plugin.getGamestateManager().setGameState(ENDING_STATE);
        }
    }

    @Override
    public void stop() {
        for(Player player : Bukkit.getOnlinePlayers()) {
            player.sendTitle(" §7Das Spiel ist aus!" ,  "§7Die Sieger sind die: " + winnigRole.getChatColor() + winnigRole.getName());
        }
        Bukkit.broadcastMessage(Main.Prefix + "§7Das Spiel ist aus!");
        Bukkit.broadcastMessage(Main.Prefix +  "§7Die Sieger sind die: " + winnigRole.getChatColor() + winnigRole.getName());
    }

}
