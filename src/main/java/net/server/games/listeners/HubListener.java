package net.server.games.listeners;

import net.server.games.Main;
import net.server.games.voting.Voting;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class HubListener implements Listener {

    private Main plugin;


    @EventHandler
    public void handleLobbyCLick(PlayerInteractEvent event) {
        if(!(event.getAction() == Action.LEFT_CLICK_AIR)) return;
        Player player = event.getPlayer();
        ItemStack item = player.getItemInHand();
        if(item.getItemMeta().getDisplayName().equals(PlayerLobbyConnectionListener.HUB_ITEM_NAME)) {
            player.sendMessage("Du Solltest Teleportiert werden");
        }
    }


}
