package net.server.games.role;

import net.server.games.Main;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class RoleManager {

    private Main plugin;
    private HashMap<String, Role> playerRoles;
    private ArrayList<Player> players;

    private int traitors, detectives, innocents;

    public RoleManager(Main plugin) {
        this.plugin = plugin;
        playerRoles = new HashMap<>();
        players = plugin.getPlayers();
    }

    public void calculateRoles() {
        int playerSize = playerRoles.size();

        traitors = (int) Math.round(Math.log(playerSize) * 1.2);
        detectives = (int) Math.round(Math.log(playerSize) * 0.75);
        innocents = playerSize - traitors - detectives;

        System.out.println("Traitor: " + traitors);
        System.out.println("Detectives: " + traitors);
        System.out.println("Innocents: " + traitors);

        Collections.shuffle(plugin.getPlayers());

        int counter = 0;
        for(int i = counter; i < traitors; i++) {
            playerRoles.put(players.get(i).getName(), Role.TRAITOR);
            counter += traitors;
        }
        for(int i = counter; i < detectives; i++) {
            playerRoles.put(players.get(i).getName(), Role.DETECTIVE);
            counter += detectives;
        }
        for(int i = counter; i < innocents; i++) {
            playerRoles.put(players.get(i).getName(), Role.INNOCENT);
        }
    }

    public Role getPlayerRole(Player player) {
        return playerRoles.get(player.getName());
    }
}
