package net.server.games.commands;

import net.server.games.Main;
import net.server.games.gamestates.LobbyState;
import net.server.games.listeners.GameProtectionListener;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BuildCommand implements CommandExecutor {

    private Main plugin;

    public BuildCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            if(player.hasPermission("ttt.build")) {
                if(args.length == 0) {
                    if(plugin.getGamestateManager().getCurrentGameState() instanceof LobbyState) {
                        GameProtectionListener gameProtectionListener = plugin.getGameProtectionListener();
                        if(!gameProtectionListener.getBuildModePlayers().contains(player.getName())) {
                            gameProtectionListener.getBuildModePlayers().add(player.getName());
                            player.sendMessage(Main.Prefix + "§aDu bist nun im §6Baumodus§a!");
                        } else {
                            gameProtectionListener.getBuildModePlayers().remove(player.getName());
                            player.sendMessage(Main.Prefix + "§7Du bist nun nicht mehr im §6Baumodus§7.");
                        }
                    } else {
                        player.sendMessage(Main.Prefix + "§cDu kannst nur im §6Lobby-State §cin den Baumodus.");
                    }
                } else {
                    player.sendMessage(Main.Prefix + "§cBitte benutze §6/build§c!");
                }
            } else {
                player.sendMessage(Main.NO_PERMISSION);
            }
        }
        return false;
    }

}
