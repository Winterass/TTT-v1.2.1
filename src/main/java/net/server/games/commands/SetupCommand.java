package net.server.games.commands;

import net.server.games.Main;
import net.server.games.gamestates.LobbyState;
import net.server.games.util.ConfigLocationUtil;
import net.server.games.voting.Map;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetupCommand implements CommandExecutor {

    private Main plugin;

    public SetupCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;
            if(player.hasPermission("ttt.setup")) {
                if(args.length == 0) {
                    player.sendMessage(Main.Prefix + "§cBitte nutze §6/setup <LOBBY>");
                } else {
                    if(args[0].equalsIgnoreCase("lobby")) {
                        if(args.length == 1) {
                            new ConfigLocationUtil(plugin, player.getLocation(), "Lobby").saveLocation();
                            player.sendMessage(Main.Prefix + "§aDie Lobby wurde neu gesetzt.");
                        } else
                            player.sendMessage(Main.Prefix + "§cBitte benutze §6/setup lobby §c!");

                    } else if(args[0].equalsIgnoreCase("create")) {
                        if(args.length == 3) {
                            Map map = new Map(plugin, args[1]);
                            if(!map.exists()) {
                                map.create(args[2]);
                                player.sendMessage(Main.Prefix + "§aDie Map §6" + map.getName() + " §awurde erstellt!");
                            } else
                                player.sendMessage(Main.Prefix + "§cDiese Map existiert bereits.");
                        } else
                            player.sendMessage(Main.Prefix + "§cBitte benutze §6/setup create <NAME> <ERBAUER>§c!");

                    } else if(args[0].equalsIgnoreCase("set")) {
                        if(args.length == 3) {
                            Map map = new Map(plugin, args[1]);
                            if(map.exists()) {
                                try {
                                    int spawnNumber = Integer.parseInt(args[2]);
                                    if(spawnNumber > 0 && spawnNumber <= LobbyState.MAX_PLAYERS) {
                                        map.setSpawnLocation(spawnNumber, player.getLocation());
                                        player.sendMessage(Main.Prefix + "§aDu hast die Spawn-Location §6" + spawnNumber + "§afür die Map §6" + map.getName() + " §a gesetzt.");
                                    }else
                                        player.sendMessage(Main.Prefix + "§cBitte gib eine Zahl §6zwischen 1 und " + + LobbyState.MAX_PLAYERS + "§can.");
                                } catch (NumberFormatException e) {
                                    if(args[2].equalsIgnoreCase("spectator")) {
                                        map.setSpectatorLocation(player.getLocation());
                                        player.sendMessage(Main.Prefix + "§aDu hast die §6Spectator-Location §afür die Map §6" + map.getName() + " §a gesetzt.");
                                    } else
                                        player.sendMessage(Main.Prefix + "§cBitte benutze §6/setup set <NAME> <1-" + LobbyState.MAX_PLAYERS + " // SPECTATOR>");
                                }

                            } else
                                player.sendMessage(Main.Prefix + "§cDiese Map existiert noch nicht.");
                        } else
                            player.sendMessage(Main.Prefix + "§cBitte benutze §6/setup set <NAME> <1-" + LobbyState.MAX_PLAYERS + " // SPECTATOR>");
                    } else
                        player.sendMessage(Main.Prefix + "§cBitte benutze §6/setup <LOBBY/SETUP>§c!");
                }
            }else
                player.sendMessage(Main.NO_PERMISSION);
        }
        return false;
    }
}
