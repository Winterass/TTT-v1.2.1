package net.server.games.listeners;

import net.server.games.Main;
import net.server.games.countdowns.LobbyCountdown;
import net.server.games.gamestates.LobbyState;
import net.server.games.util.ConfigLocationUtil;
import net.server.games.util.ItemBuilder;
import net.server.games.voting.Voting;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerLobbyConnectionListener implements Listener {

    public static final String VOTING_ITEM_NAME = "§6§lVoting-Menü",
                                HUB_ITEM_NAME = "§6§lHub";

    private Main plugin;
    private ItemStack voteItem;
    private ItemStack hubItem;

    public PlayerLobbyConnectionListener(Main plugin) {
        this.plugin = plugin;
        voteItem = new ItemBuilder(Material.NETHER_STAR).setDisplayName(PlayerLobbyConnectionListener.VOTING_ITEM_NAME).build();
        hubItem = new ItemBuilder(Material.MAGMA_CREAM).setDisplayName(PlayerLobbyConnectionListener.HUB_ITEM_NAME).build();
    }

    @EventHandler
    public void handlePlayerJoin(PlayerJoinEvent event) {
        if(!(plugin.getGamestateManager().getCurrentGameState() instanceof LobbyState)) return;
        Player player = event.getPlayer();
        plugin.getPlayers().add(player);
        event.setJoinMessage("§7[§a+§7] " + player.getDisplayName() + " §7ist dem Spiel beigetreten.");

        player.getInventory().clear();
        player.getInventory().setItem(4, voteItem);
        player.getInventory().setItem(8, hubItem);

        ConfigLocationUtil locationUtil = new ConfigLocationUtil(plugin, "Lobby");
        if(locationUtil.loadLocation() != null) {
            player.teleport(locationUtil.loadLocation());
        }else
            Bukkit.getConsoleSender().sendMessage("§cDie Lobby-Location wurde noch nicht gesetzt!");



        LobbyState lobbyState = (LobbyState) plugin.getGamestateManager().getCurrentGameState();
        LobbyCountdown countdown = lobbyState.getCountdown();
        if(plugin.getPlayers().size() >= LobbyState.MIN_PLAYERS) {
            if(!countdown.isRunning()) {
                countdown.stopIdle();
                countdown.start();
            }
        }

    }

    @EventHandler
    public void handlePlayerQuit(PlayerQuitEvent event){
        if(!(plugin.getGamestateManager().getCurrentGameState() instanceof LobbyState)) return;
        Player player = event.getPlayer();
        plugin.getPlayers().remove(player);
        event.setQuitMessage("§7[§4-§7] " + player.getDisplayName() + " §7hat das Spiel verlassen.");

        LobbyState lobbyState = (LobbyState) plugin.getGamestateManager().getCurrentGameState();
        LobbyCountdown countdown = lobbyState.getCountdown();
        if(plugin.getPlayers().size() < LobbyState.MIN_PLAYERS) {
            if(countdown.isRunning()) {
                countdown.stop();
                countdown.startIdle();
            }
        }

        Voting voting = plugin.getVoting();
        if(voting.getPlayerVotes().containsKey(player.getName())) {
            voting.getVotingMaps()[voting.getPlayerVotes().get(player.getName())].removeVote();
            voting.getPlayerVotes().remove(player.getName());
            voting.initVotingInventory();
        }
    }
}
