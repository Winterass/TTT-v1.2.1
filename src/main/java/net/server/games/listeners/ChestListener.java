package net.server.games.listeners;

import net.server.games.Main;
import net.server.games.gamestates.IngameState;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ChestListener implements Listener {

    private Main plugin;
    private ItemStack woodenSword, stoneSword, ironSword, bow, arrows;

    public ChestListener(Main plugin) {
        this.plugin = plugin;

        woodenSword = new ItemStack(Material.WOODEN_SWORD);
        stoneSword = new ItemStack(Material.STONE_SWORD);
        ironSword = new ItemStack(Material.IRON_SWORD);
        bow = new ItemStack(Material.BOW);
        arrows = new ItemStack(Material.ARROW, 32);

    }

    @EventHandler
    public void handleChestClick(PlayerInteractEvent event) {
        if(event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if(event.getClickedBlock().getType() != Material.CHEST) return;
        event.setCancelled(true);
        if(!(plugin.getGamestateManager().getCurrentGameState() instanceof IngameState)) return;

        Player player = event.getPlayer();
        if(!player.getInventory().contains(woodenSword)) {
            openChest(woodenSword, event.getClickedBlock(), player);
        } else if (!player.getInventory().contains(bow)) {
            openChest(bow, event.getClickedBlock(), player);
            player.getInventory().addItem(arrows);
        } else if (!player.getInventory().contains(stoneSword)) {
            openChest(stoneSword, event.getClickedBlock(), player);
        }
    }

    @EventHandler
    public void handleIronChestClick(PlayerInteractEvent event) {
        if(event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if(event.getClickedBlock().getType() != Material.ENDER_CHEST) return;
        event.setCancelled(true);
        if(!(plugin.getGamestateManager().getCurrentGameState() instanceof IngameState)) return;

        IngameState ingameState = (IngameState) plugin.getGamestateManager().getCurrentGameState();
        Player player = event.getPlayer();
        if(!ingameState.isInGrace()) {
            openChest(ironSword, event.getClickedBlock(), player);
        } else {
            player.sendMessage(Main.Prefix + "§cDiese Kiste kannst du erst nach der Schutzzeit öffnen!");
        }
    }

    private void openChest(ItemStack item, Block block, Player player) {
        player.getInventory().addItem(item);
        block.setType(Material.AIR);
    }

}
