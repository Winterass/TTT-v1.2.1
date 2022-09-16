package net.server.games;

import net.server.games.commands.SetupCommand;
import net.server.games.commands.StartCommand;
import net.server.games.gamestates.GameState;
import net.server.games.gamestates.GamestateManager;
import net.server.games.listeners.HubListener;
import net.server.games.listeners.PlayerLobbyConnectionListener;
import net.server.games.listeners.VotingListener;
import net.server.games.voting.Map;
import net.server.games.voting.Voting;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;


public final class Main extends JavaPlugin {

    private GamestateManager gamestateManager;
    private ArrayList<Player> players;
    private ArrayList<Map> maps;
    private Voting voting;

    public static final String Prefix = "§7[§cTTT§7] §r",
                                NO_PERMISSION = Prefix + "§cDazu hast du keine Rechte";

    @Override
    public void onEnable() {
        gamestateManager = new GamestateManager(this);
        players = new ArrayList<>();

        gamestateManager.setGameState(GameState.LOBBY_STATE);

        init(Bukkit.getPluginManager());

    }

    private void init(PluginManager pluginManager) {
        initVoting();

        getCommand("setup").setExecutor(new SetupCommand(this));
        getCommand("start").setExecutor(new StartCommand(this));

        pluginManager.registerEvents(new PlayerLobbyConnectionListener(this), this);
        pluginManager.registerEvents(new VotingListener(this), this);
        pluginManager.registerEvents(new HubListener(), this);
    }

    private void initVoting() {
        maps = new ArrayList<>();
        for(String current : getConfig().getConfigurationSection("Arenas").getKeys(false)) {
            Map map = new Map(this, current);
            if(map.playable()) {
                maps.add(map);
            } else {
                Bukkit.getConsoleSender().sendMessage("§cDie Map §4" + map.getName() + "§c ist noch nicht fertig eingerichtet.");
            }
        }
        if(maps.size() >= Voting.MAP_AMOUNT) {
            voting = new Voting(this, maps);
        } else {
            Bukkit.getConsoleSender().sendMessage("§cFür das Voting müssen mindestens §4" + Voting.MAP_AMOUNT + "§c Maps eingerichtet sein");
            voting = null;
        }

    }

    @Override
    public void onDisable() {

    }

    public GamestateManager getGamestateManager() {
        return gamestateManager;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public Voting getVoting() {
        return voting;
    }

    public ArrayList<Map> getMaps() {
        return maps;
    }

    /* @Override
    public void onEnable() {
        PluginManager manager = Bukkit.getPluginManager();
        getCommand("setspawn").setExecutor(new SetSpawnCommand());
        getCommand("setplayerspawn").setExecutor(new SetPlayerSpawns());
        manager.registerEvents(new JoinListener(), this);
        cfg = new Config("warps.yml" , getDataFolder());
        if(!cfg.contains("spawn")){
            cfg.set("spawn" , Bukkit.getWorld("world").getSpawnLocation());
            cfg.save();
        }
        GamestateManager gamestateManager = GamestateManager.getInstance();
        gamestateManager.setGamestate(new LobbyState());

    }*/



}
