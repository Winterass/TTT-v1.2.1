package net.server.games.commands;

import net.server.games.Main;
import net.server.games.gamestates.LobbyState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StartCommand implements CommandExecutor {

    private static final int START_SECONDS = 5;

    private Main plugin;

    public StartCommand(Main plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            if(player.hasPermission("ttt.start")) {
                if(args.length == 0) {
                    if(plugin.getGamestateManager().getCurrentGameState() instanceof LobbyState) {
                        LobbyState lobbyState = (LobbyState) plugin.getGamestateManager().getCurrentGameState();
                        if(lobbyState.getCountdown().isRunning() && (lobbyState.getCountdown().getSeconds() > START_SECONDS)) {
                            lobbyState.getCountdown().setSeconds(START_SECONDS);
                            player.sendMessage(Main.Prefix + "§aDer Spielstart wurde beschleunigt.");
                        } else
                            player.sendMessage(Main.Prefix + "§cEs sind nicht genug Spieler Online.");
                    }else
                        player.sendMessage(Main.Prefix + "§cDas Spiel ist bereits gestartet2.");
                } else
                    player.sendMessage(Main.Prefix + "§cBitte benutze §6/start§c!");
            } else
                player.sendMessage(Main.NO_PERMISSION);
        }
        return false;
    }
}
