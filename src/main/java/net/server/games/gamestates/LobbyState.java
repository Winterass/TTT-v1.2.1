package net.server.games.gamestates;

import net.server.games.Main;
import net.server.games.countdowns.LobbyCountdown;
import org.bukkit.Bukkit;


public class LobbyState extends GameState{

    public static final int MIN_PLAYERS = 2,
                            MAX_PLAYERS = 10,
                            MAX_SPAWNS = 16;

    private LobbyCountdown countdown;

    public LobbyState(GamestateManager gamestateManager) {
        countdown = new LobbyCountdown(gamestateManager);
    }

    @Override
    public void start() {
        countdown.startIdle();
    }

    @Override
    public void stop() {
        Bukkit.broadcastMessage(Main.Prefix + "§cAlle Spieler werden teleportiert!");
    }

    public LobbyCountdown getCountdown() {
        return countdown;
    }

    /*@Override
    public void gamephase() {

    }

    @Override
    public void startlobby() {
        Bukkit.getPluginManager().registerEvents(new LobbyStateListeners() , Main.getPlugin());
        startTimer();
    }

    @Override
    public void stoplobby() {
        System.out.println("stoplobby");
        HandlerList.unregisterAll(new LobbyStateListeners());
        for(Player player : Bukkit.getOnlinePlayers()){
            player.teleport(WarpManager.getWarp("spawn"));
        }
    }*/





}
