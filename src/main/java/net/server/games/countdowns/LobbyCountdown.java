package net.server.games.countdowns;

import net.server.games.Main;
import net.server.games.gamestates.GameState;
import net.server.games.gamestates.GamestateManager;
import net.server.games.gamestates.LobbyState;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.server.games.voting.Map;
import net.server.games.voting.Voting;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;

public class LobbyCountdown extends Countdown {

    private static final int COUNTDOWN_TIME = 60, IDLE_TIME = 0;

    private GamestateManager gamestateManager;

    private int seconds;
    private boolean isRunning;
    private int idleID;
    private boolean isIdling;

    public LobbyCountdown(GamestateManager gamestateManager) {
        this.gamestateManager = gamestateManager;
        seconds = COUNTDOWN_TIME;
    }

    @Override
    public void start() {
        isRunning = true;
        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(gamestateManager.getPlugin(), new Runnable() {
            @Override
            public void run() {
                switch (seconds) {
                    case 60: case 50: case 40: case 30: case 20: case 10: case 5: case 4: case 3: case 2: case 1:
                        Bukkit.broadcastMessage(Main.Prefix + "§7Das Spiel startet in §a" + seconds + " Sekunden§7.");

                        if(seconds == 5) {
                            Voting voting = gamestateManager.getPlugin().getVoting();
                            Map winnigMap;
                            if(voting != null) {
                                winnigMap = voting.getWinnerMap();
                            } else {
                                ArrayList<Map> maps = gamestateManager.getPlugin().getMaps();
                                Collections.shuffle(maps);
                                winnigMap = maps.get(0);
                            }
                            for(Player player : Bukkit.getOnlinePlayers()) {
                                player.sendTitle(" §7Sieger des Votings: §6" , winnigMap.getName());
                            }
                        }

                        break;
                    case 0:
                        gamestateManager.setGameState(GameState.INGAME_STATE);
                        break;
                    default:
                        break;
                }
                for(Player player : Bukkit.getOnlinePlayers()){
                    if(seconds > 1){
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§7Das Spiel startet in §a" +
                                seconds +
                                " Sekunden§7."));
                    }else if(seconds == 1){
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§7Das Spiel startet in §aeiner Sekunde§7."));
                    }
                }
                seconds --;

            }

        }, 0, 20);
    }

    @Override
    public void stop() {
        if(isRunning) {
            Bukkit.getScheduler().cancelTask(taskID);
            isRunning = false;
            seconds = COUNTDOWN_TIME;
        }
    }

    public void startIdle() {
        isIdling = true;
        idleID = Bukkit.getScheduler().scheduleSyncRepeatingTask(gamestateManager.getPlugin(), new Runnable() {

            @Override
            public void run() {
                Integer leftplayers = LobbyState.MIN_PLAYERS - gamestateManager.getPlugin().getPlayers().size();
                for(Player player : Bukkit.getOnlinePlayers()){
                    if(leftplayers > 1){
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§cBis zum Spielstart fehlen noch §4" +
                                leftplayers +
                                " §candere Spieler§7."));
                    }else if(leftplayers == 1){
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§cBis zum Spielstart fehlt noch §4" +
                                leftplayers +
                                " §canderer Spieler§7."));
                    }
                }
                /*for(Player player : Bukkit.getOnlinePlayers()){
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(Main.Prefix + "§7Bis zum Spielstart fehlen noch §6" +
                            (LobbyState.MIN_PLAYERS - gamestateManager.getPlugin().getPlayers().size()) +
                            " Spieler§7."));
                }
                for(Player player : Bukkit.getOnlinePlayers()){
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(Main.Prefix + "Warten auf " +
                            (LobbyState.MIN_PLAYERS - gamestateManager.getPlugin().getPlayers().size()) +
                            " andere Spieler§7."));
                }
                Bukkit.broadcastMessage(Main.Prefix + "§7Bis zum Spielstart fehlen noch §6" +
                            (LobbyState.MIN_PLAYERS - gamestateManager.getPlugin().getPlayers().size()) +
                            " Spieler§7.");*/
            }
        }, 0, 20 * IDLE_TIME);
    }

    public void stopIdle() {
        if(isIdling) {
            Bukkit.getScheduler().cancelTask(idleID);
            isIdling = false;
        }
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public boolean isRunning() {
        return isRunning;
    }
}
