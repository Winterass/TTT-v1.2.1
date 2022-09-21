package net.server.games.listeners;

import net.server.games.Main;
import net.server.games.gamestates.IngameState;
import net.server.games.role.Role;
import net.server.games.role.RoleManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class GameProgressListener implements Listener {

    private Main plugin;
    private RoleManager roleManager;

    public GameProgressListener(Main plugin) {
        this.plugin = plugin;
        roleManager = plugin.getRoleManager();
    }

    @EventHandler
    public void handlePlayerDamage(EntityDamageByEntityEvent event) {
        if(!(plugin.getGamestateManager().getCurrentGameState() instanceof IngameState)) return;
        if(!(event.getDamager() instanceof Player)) return;
        if(!(event.getEntity() instanceof Player)) return;
        Player damager = (Player) event.getDamager(), victim = (Player) event.getEntity();
        Role damagerRole = roleManager.getPlayerRole(damager), victimRole = roleManager.getPlayerRole(victim);

        if((damagerRole == Role.INNOCENT || damagerRole == Role.DETECTIVE) && victimRole == Role.DETECTIVE) {
            damager.sendMessage(Main.Prefix + "§cAchtung! Du hast einen Detective angegriffen!");
        }
        if(damagerRole == Role.TRAITOR && victimRole == Role.TRAITOR) {
            event.setDamage(0);
        }
    }

    @EventHandler
    public void handlePlayerDeath(PlayerDeathEvent event) {
        if(!(plugin.getGamestateManager().getCurrentGameState() instanceof  IngameState)) return;
        IngameState ingameState = (IngameState) plugin.getGamestateManager().getCurrentGameState();
        Player victim = event.getEntity();
        if(victim.getKiller() != null) {
            Player killer = victim.getKiller();
            Role killerRole = roleManager.getPlayerRole(killer), victimRole = roleManager.getPlayerRole(victim);

            switch (killerRole) {
                case TRAITOR:
                    if(victimRole == Role.TRAITOR) {
                        killer.sendMessage(Main.Prefix + "§cDu hast einen Traitor-Kollegen umgebracht!");
                    } else {
                        killer.sendMessage(Main.Prefix + "§b du hast einen " + victimRole.getChatColor() + victimRole.getName() + " §bgetötet.");
                    }
                    break;
                case INNOCENT: case DETECTIVE:
                    if(victimRole == Role.TRAITOR) {
                        killer.sendMessage(Main.Prefix + "§aDu hast einen §cTraitor §aerwischt!");
                    } else if (victimRole == Role.INNOCENT) {
                        killer.sendMessage(Main.Prefix + "§cDu hast einen §aInnocent §cermordet!");
                    } else if (victimRole == Role.DETECTIVE) {
                        killer.sendMessage(Main.Prefix + "§cDu hast einen §2Detective §cermordet!");
                    }
                    break;

                default:
                    break;
            }

            victim.sendMessage(Main.Prefix + "§cDu wurdest vom " + killerRole.getChatColor() + killerRole.getName()
                    + "§c " + killer.getName() + "umgebracht");
            if(victimRole == Role.TRAITOR) {
                plugin.getRoleManager().getTraitorPlayers().remove(victim.getName());
            }
            plugin.getPlayers().remove(victim);

            ingameState.checkGameEnding();
        } else {
            victim.sendMessage(Main.Prefix + "§cDu bist gestorben.");

            if(plugin.getRoleManager().getPlayerRole(victim) == Role.TRAITOR) {
                plugin.getRoleManager().getTraitorPlayers().remove(victim.getName());
            }
            plugin.getPlayers().remove(victim);

            ingameState.checkGameEnding();
        }
        event.setDeathMessage(Main.Prefix + "§7Ein Spieler ist §6gestorben§7.");
    }

    @EventHandler
    public void handlePlayerQuit(PlayerQuitEvent event) {
        if(!(plugin.getGamestateManager().getCurrentGameState() instanceof IngameState)) return;
        Player player = event.getPlayer();
        if(plugin.getPlayers().contains(player)) {
            IngameState ingameState = (IngameState) plugin.getGamestateManager().getCurrentGameState();
            plugin.getPlayers().remove(player);
            event.setQuitMessage("§7[§c-§7] " + player.getDisplayName() + "§7 hat das Spiel Verlassen");

            ingameState.checkGameEnding();
        }
    }

}
